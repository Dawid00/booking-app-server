package com.dpap.bookingapp.users;

import com.dpap.bookingapp.users.roles.Role;
import com.dpap.bookingapp.users.roles.RoleEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    List<RoleEntity> roleEntitiesFromRoles(List<Role> roles) {
        return roles.stream().map(this::mapRoleToEntity).collect(Collectors.toList());
    }

    List<Role> fromRoleEntities(List<RoleEntity> roleEntities) {
        return roleEntities.stream().map(RoleEntity::getType).collect(Collectors.toList());
    }

    private RoleEntity mapRoleToEntity(Role role) {
        return new RoleEntity(null, role);
    }

    UserEntity entityFromUser(User user, List<RoleEntity> roles) {
        return new UserEntity(
                user.getId(),
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getPassword(),
                user.getEmail(),
                user.getAccountNumber(),
                roles
        );
    }

    User fromUserEntity(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getPassword(),
                userEntity.getEmail(),
                userEntity.getAccountNumber(),
                userEntity.getRoles().stream().map(RoleEntity::getType).toList()
        );
    }
}
