package com.example.VolunteerWebApp.repository;

import com.example.VolunteerWebApp.entity.Post;
import com.example.VolunteerWebApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByPostId(Long postId);
    Optional<Post> findByPostName(String postName);
    List<Post> findByUser(User user);
}
