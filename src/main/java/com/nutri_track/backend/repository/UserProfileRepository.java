package com.nutri_track.backend.repository;

import com.nutri_track.backend.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository
        extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByAccount_Id(Long accountId);
}
