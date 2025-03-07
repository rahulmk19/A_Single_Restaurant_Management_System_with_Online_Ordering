package com.foodtaste.serviceImpl;

import org.springframework.stereotype.Service;

import com.foodtaste.model.Role;
import com.foodtaste.repository.RoleRepo;
import com.foodtaste.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;

    public RoleServiceImpl(RoleRepo roleRepo){
        this.roleRepo=roleRepo;
    }


    @Override
    public Role createNewRole(Role role) {
        return roleRepo.save(role);
    }
}
