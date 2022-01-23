package cmpe275.vms.services.impl;

import cmpe275.vms.models.Role;
import cmpe275.vms.repositories.RoleRepository;
import cmpe275.vms.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
}
