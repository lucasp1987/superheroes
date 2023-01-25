package com.example.superheroes.service;

import com.example.superheroes.model.Role;
import com.example.superheroes.model.UserRole;
import com.example.superheroes.repository.RolePrivilegeRepository;
import com.example.superheroes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    final private UserRepository userRepository;

    final private RolePrivilegeRepository rolePrivilegeRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, RolePrivilegeRepository rolePrivilegeRepository) {
        this.userRepository = userRepository;
        this.rolePrivilegeRepository = rolePrivilegeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.example.superheroes.model.User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return new org.springframework.security.core.userdetails.User(
                    " ", " ", true, true, true, true,
                    List.of(new SimpleGrantedAuthority("ROLE_NONE")));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), user.isEnabled(), true, true,
                true, getAuthorities(user.getUserRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<UserRole> userRoles) {

        return getGrantedAuthorities(getPrivileges(userRoles));
    }

    private List<String> getPrivileges(Collection<UserRole> userRoles) {

        List<String> privileges = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            Role role = userRole.getRole();
            privileges.add(role.getName());
            privileges.addAll(rolePrivilegeRepository.findByUserRoleId(userRole.getId()).stream().map(rp -> role.getName() + "_" + rp.getPrivilege().getName()).toList());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}