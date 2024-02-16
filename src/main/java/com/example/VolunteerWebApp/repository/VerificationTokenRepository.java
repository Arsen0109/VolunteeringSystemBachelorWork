package com.example.VolunteerWebApp.repository;

import com.example.VolunteerWebApp.entity.User;
import com.example.VolunteerWebApp.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}
