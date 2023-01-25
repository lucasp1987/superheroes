package com.example.superheroes.service;

import com.example.superheroes.dto.PrivilegeDto;
import com.example.superheroes.dto.UserDto;
import com.example.superheroes.exception.PasswordInvalidException;
import com.example.superheroes.exception.UserAlreadyExistException;
import com.example.superheroes.exception.UserNotExistException;
import com.example.superheroes.model.*;
import com.example.superheroes.repository.*;
import com.example.superheroes.validation.PasswordValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    private final UserRoleRepository userRoleRepository;

    private final RolePrivilegeRepository rolePrivilegeRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository, UserRoleRepository userRoleRepository, RolePrivilegeRepository rolePrivilegeRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.userRoleRepository = userRoleRepository;
        this.rolePrivilegeRepository = rolePrivilegeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> login(String email, String password) {
        return userRepository.login(email, password);
    }

    public User createUser(UserDto userDto) throws RuntimeException {
        if (userDto == null) {
            throw new UserNotExistException();
        } else if (userRepository.findByEmail(userDto.email()).isPresent()) {
            throw new UserAlreadyExistException();
        }
        if (!PasswordValidation.isValid(userDto.password())) {
            throw new PasswordInvalidException();
        }

        User user = new User();
        user.setEmail(userDto.email());
        user.setPassword(passwordEncoder.encode(userDto.password()));
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setAge(userDto.age());
        user.setEnabled(true);
        user.setUserRoles(new ArrayList<>());
        user = userRepository.save(user);

        User finalUser = user;
        userDto.rolesDto().forEach(roleDto -> {
            Role role = new Role(roleDto.name());
            role = saveRole(role);
            UserRole userRole = addRoleToUser(finalUser, role);
            for (PrivilegeDto privilegeDto : roleDto.privilegesDto()) {
                Privilege privilege = new Privilege(privilegeDto.name());
                privilege = savePrivilege(privilege);
                addPrivilegeToRole(userRole, privilege);
            }
        });

        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public User save(User user) {
        log.info("Saving new user {} to the database", user.getEmail());
        return userRepository.findByEmail(user.getEmail()).orElse(userRepository.save(user));
    }

    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        Role rolePersisted = roleRepository.findByName(role.getName());
        return rolePersisted == null ? roleRepository.save(role) : rolePersisted;
    }

    public Privilege savePrivilege(Privilege privilege) {
        log.info("Saving new privilege {} to the database", privilege.getName());
        Privilege privilegePersisted = privilegeRepository.findByName(privilege.getName());
        return privilegePersisted == null ? privilegeRepository.save(privilege) : privilegePersisted;
    }

    public UserRole addRoleToUser(User user, Role role) {
        log.info("Adding role {} to user with id {}", role.getName(), user.getId());
        return userRoleRepository.save(new UserRole(user, role));
    }

    public RolePrivilege addPrivilegeToRole(UserRole userRole, Privilege privilege) {
        log.info("Adding privilege {} to role {} to user with id {}", privilege.getName(), userRole.getRole().getName(), userRole.getUser().getId());
        return rolePrivilegeRepository.save(new RolePrivilege(userRole, privilege));
    }

}