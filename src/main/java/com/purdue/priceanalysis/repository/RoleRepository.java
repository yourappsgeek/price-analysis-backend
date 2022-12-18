package com.purdue.priceanalysis.repository;


import com.purdue.priceanalysis.enums.RoleName;
import com.purdue.priceanalysis.model.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, RoleName> {

    Optional<RoleEntity> findByRoleName(RoleName roleName);
}
