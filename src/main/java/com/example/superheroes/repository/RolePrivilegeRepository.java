package com.example.superheroes.repository;

import com.example.superheroes.model.RolePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, Long> {
    @Query("select rp from RolePrivilege rp where rp.userRole.id=?1")
    List<RolePrivilege> findByUserRoleId(Long userRoleId);

}
