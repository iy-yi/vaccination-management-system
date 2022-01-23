package cmpe275.vms.controllers;

import cmpe275.vms.config.TokenProvider;
import cmpe275.vms.exceptions.AttributeErrorException;
import cmpe275.vms.exceptions.DuplicateEntryException;
import cmpe275.vms.models.*;
import cmpe275.vms.repositories.TokenRepository;
import cmpe275.vms.services.UserService;
import cmpe275.vms.services.VaccinationEntryService;
import cmpe275.vms.services.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private VaccineService vaccineService;

    @Autowired
    private VaccinationEntryService vaccinationEntryService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Autowired
    private TokenRepository tokenRepository;

    @GetMapping("/status")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String test() {

        return "test";
    }

    @GetMapping("/{mrn}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable String mrn, HttpServletRequest request) {
        User user = userService.find(mrn);
        return user;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody LoginUser loginUser) throws AuthenticationException {
        User user = userService.findEmail(loginUser.getEmail());
        if(!user.isActivated()) {
            throw new AttributeErrorException("Verify your email");
        }
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getEmail(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        Map<String, String> map = new HashMap<>();
        map.put("token", new AuthToken(token).getToken());
        boolean admin = false;
        for(Role r : user.getRole()) {
            if(r.getName().equals("Admin")) {
                admin = true;
                map.put("role", "Admin");
            }
        }
        if(!admin) {
            map.put("role", "Patient");
        }
        return ResponseEntity.ok(map);
    }


    @RequestMapping(value="/signup", method = RequestMethod.POST)
    @ResponseBody
    public User saveUser(@RequestBody UserDTO user){
        User u = userService.create(user);
        List<Vaccination> vaccines = vaccineService.findAll();
        String defaultTime = "2020-01-01T00:00:00.000";
        LocalDateTime due = LocalDateTime.parse(defaultTime);
        List<VaccinationEntry> defaultVaccine = new ArrayList<>();
        for (Vaccination vaccine: vaccines) {
            VaccinationEntry entry = new VaccinationEntry(
                    vaccine, 1, false);
            entry.setDue(due);
            VaccinationEntry vn = vaccinationEntryService.create(entry);
            defaultVaccine.add(vn);
//            u.getVaccines().add(entry);
        }
        u.setVaccines(defaultVaccine);
        userService.update(u);
        return u;
    }

    @PreAuthorize("hasRole('Admin')")
    @RequestMapping(value="/adminping", method = RequestMethod.GET)
    public String adminPing(Authentication auth){
        System.out.println(auth.getName());
        return "Only Admins Can Read This";
    }

    @PreAuthorize("hasRole('Patient')")
    @RequestMapping(value="/userping", method = RequestMethod.GET)
    public String userPing(){
        return "Any User Can Read This";
    }

    @GetMapping("/confirm")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String confirmEmail(@RequestParam("token")String confirmationToken) {
        System.out.println(confirmationToken);
        try{
            Token t = tokenRepository.findByToken(confirmationToken);
            User u = userService.find(t.getUser().getMrn());
            u.setActivated(true);
            userService.update(u);
            tokenRepository.delete(t);
        } catch(Exception e) {
            return "Token error, please check your email";
        }


        return "Account verified, enjoy!";
    }
}
