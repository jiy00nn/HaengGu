package com.haenggu.repository;

import com.haenggu.domain.entity.UserImage;
import com.haenggu.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage, UUID> {
    UserImage findUserImageByUser(Users user);
}
