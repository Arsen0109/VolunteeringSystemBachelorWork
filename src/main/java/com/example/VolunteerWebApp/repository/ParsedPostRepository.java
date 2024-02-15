package com.example.VolunteerWebApp.repository;

import com.example.VolunteerWebApp.entity.ParsedPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParsedPostRepository extends JpaRepository<ParsedPost, Long> {
}
