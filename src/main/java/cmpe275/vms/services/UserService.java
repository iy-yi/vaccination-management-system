package cmpe275.vms.services;


import cmpe275.vms.models.User;
import cmpe275.vms.models.UserDTO;

public interface UserService {
    User create(UserDTO user);
    User find(String mrn);
    User findEmail(String email);
    User update(User user);
}
