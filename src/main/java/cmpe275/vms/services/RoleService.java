package cmpe275.vms.services;

import cmpe275.vms.models.Role;

public interface RoleService {
    Role findByName(String name);
}
