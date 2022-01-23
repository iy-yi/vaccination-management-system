package cmpe275.vms.services.impl;

import cmpe275.vms.exceptions.DuplicateEntryException;
import cmpe275.vms.exceptions.RecordNotFoundException;
import cmpe275.vms.repositories.TokenRepository;
import cmpe275.vms.repositories.UserRepository;
import cmpe275.vms.services.EmailService;
import cmpe275.vms.services.RoleService;
import cmpe275.vms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import cmpe275.vms.models.*;

import java.util.HashSet;
import java.util.Set;

@Service(value = "userService")
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final
    BCryptPasswordEncoder encoder;

    private final
    TokenRepository tokenRepository;

    final
    EmailService emailService;


    public UserServiceImpl(UserRepository userRepository, RoleService roleService, @Lazy BCryptPasswordEncoder encoder, TokenRepository tokenRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.encoder = encoder;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }


    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });
        return authorities;
    }

    @Override
    public User create(UserDTO user) {

        User nUser = user.getUserFromDTO();
        User n = userRepository.findByEmail(nUser.getEmail());
        if(n != null) {
            throw new DuplicateEntryException("User existed");
        }
        nUser.setPassword(encoder.encode(user.getPassword()));
        Role role = roleService.findByName("Patient");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        if(nUser.getEmail().split("@")[1].equals("sjsu.edu")){
            role = roleService.findByName("Admin");
            roleSet.add(role);
        }

        nUser.setRole(roleSet);
        User cur = userRepository.save(nUser);
        Token t = new Token(cur);
        tokenRepository.save(t);

        String msg = "Open this link to verify your account: http://13.52.100.140:8080/user/confirm?token="+t.getToken();

        emailService.sendEmail(nUser.getEmail(), "Verify your account", msg);

        return nUser;
    }


    @Override
    public User find(String mrn) {
        User user = userRepository.findById(mrn).orElseThrow(() -> new RecordNotFoundException("Invalid User MRN"));
        return user;
    }

    @Override
    public User findEmail(String email) {
        User u = userRepository.findByEmail(email);
        if(u == null) {
            throw new RecordNotFoundException("email not found");
        }
        return u;
    }

    @Override
    public User update(User user) {
        return userRepository.saveAndFlush(user);
    }


}
