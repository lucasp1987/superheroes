package com.example.superheroes.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "\"users_roles\"")
@NoArgsConstructor
@Getter
@Setter
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Role role;

    @OneToMany(mappedBy = "userRole")
    private List<RolePrivilege> rolePrivileges;

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
    }
}