package com.example.superheroes.config;

import com.example.superheroes.model.Privilege;
import com.example.superheroes.model.Role;
import com.example.superheroes.model.User;
import com.example.superheroes.model.UserRole;
import com.example.superheroes.repository.SuperheroRepository;
import com.example.superheroes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    boolean alreadySetup = false;

    @Autowired
    public SetupDataLoader(SuperheroRepository superheroRepository, UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) return;

        /*Faker faker = new Faker();
        for (int i = 0; i < 10; i++) {
            Superhero superhero = new Superhero();
            superhero.setName(faker.superhero().name());
            superhero.setPrefix(faker.superhero().prefix());
            superhero.setSuffix(faker.superhero().suffix());
            superhero.setPower(faker.superhero().power());
            superhero.setDescriptor(faker.superhero().descriptor());
            superheroRepository.save(superhero);
        }*/

        Privilege readPrivilege = userService.savePrivilege(new Privilege("PRIVILEGE_READ"));
        Privilege writePrivilege = userService.savePrivilege(new Privilege("PRIVILEGE_WRITE"));

        Role roleStaff = userService.saveRole(new Role("ROLE_STAFF"));
        Role roleUser = userService.saveRole(new Role("ROLE_USER"));
        Role roleAdmin = userService.saveRole(new Role("ROLE_ADMIN"));

        // user1
        User user1 = new User();
        user1.setFirstName("Test");
        user1.setLastName("Testing");
        user1.setPassword(passwordEncoder.encode("Testing$1"));
        user1.setEmail("test@test.com");
        user1.setAge(18);
        user1.setEnabled(true);

        user1 = userService.save(user1);

        UserRole userRoleStaff = userService.addRoleToUser(user1, roleStaff);
        UserRole userRoleUser = userService.addRoleToUser(user1, roleUser);

        userService.addPrivilegeToRole(userRoleStaff, readPrivilege);
        userService.addPrivilegeToRole(userRoleUser, readPrivilege);

        userService.save(user1);

        // user2
        User user2 = new User();
        user2.setFirstName("Admin");
        user2.setLastName("Administrator");
        user2.setPassword(passwordEncoder.encode("Admin$123"));
        user2.setEmail("admin@admin.com");
        user2.setAge(25);
        user2.setEnabled(true);

        user2 = userService.save(user2);

        UserRole userRoleAdmin = userService.addRoleToUser(user2, roleAdmin);

        userService.addPrivilegeToRole(userRoleAdmin, readPrivilege);
        userService.addPrivilegeToRole(userRoleAdmin, writePrivilege);

        userService.save(user2);

        alreadySetup = true;

    }
}
