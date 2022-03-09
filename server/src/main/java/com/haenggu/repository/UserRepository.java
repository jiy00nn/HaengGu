package com.haenggu.repository;

import com.haenggu.domain.entity.Users;
import com.haenggu.domain.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findBySocialTypeAndPrincipal(SocialType socialType, String principal);
}
