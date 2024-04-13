package com.example.VolunteerWebApp.repository;

import com.example.VolunteerWebApp.entity.Comment;
import com.example.VolunteerWebApp.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByCommentId(Long commentId);
    List<Comment> findByPost(Post post);
}
