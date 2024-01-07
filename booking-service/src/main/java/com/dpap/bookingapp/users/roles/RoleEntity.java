package com.dpap.bookingapp.users.roles;

import com.dpap.bookingapp.users.UserEntity;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private Role type;

    public RoleEntity() {
    }

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users;

    public RoleEntity(Long id, Role type) {
        this.id = id;
        this.type = type;
    }

    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getType() {
        return type;
    }

    public void setType(Role type) {
        this.type = type;
    }
}
