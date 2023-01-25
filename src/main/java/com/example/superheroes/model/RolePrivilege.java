package com.example.superheroes.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "\"roles_privileges\"")
@NoArgsConstructor
@Getter
@Setter
public class RolePrivilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Privilege privilege;
    @ManyToOne
    private UserRole userRole;

    public RolePrivilege(UserRole userRole, Privilege privilege) {
        this.userRole = userRole;
        this.privilege = privilege;
    }
}