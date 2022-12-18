package com.purdue.priceanalysis.repository;

import com.purdue.priceanalysis.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserId(Long userId);
    Optional<UserEntity> findByUserIdAndAndIsActive(Long userId, Integer isActive);
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByUsernameAndIsActive(String username, Integer isActive);

    Boolean existsByUsername(String username);

//    List<UserEntity> findAllByIsActive(Integer isActive);
//    List<UserEntity> findAllByIsActiveAndRoles(Integer isActive, Set<RoleEntity> roles);



}
