package com.example.VolunteerWebApp.controller;

import com.example.VolunteerWebApp.DTO.CommentRequest;
import com.example.VolunteerWebApp.DTO.CommentResponse;
import com.example.VolunteerWebApp.entity.Comment;
import com.example.VolunteerWebApp.exception.CommentNotFoundException;
import com.example.VolunteerWebApp.exception.PostNotFoundException;
import com.example.VolunteerWebApp.exception.VolunteeringSystemException;
import com.example.VolunteerWebApp.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("api/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest commentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(commentRequest));
    }

    @GetMapping("by-post/{postId}")
    public ResponseEntity<Object> getCommentsByPost(@PathVariable Long postId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentsByPostName(postId));
        } catch (PostNotFoundException postNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error, post with name: "+postId+" not found!");
        }
    }

    @GetMapping("by-user/{username}")
    public ResponseEntity<Object> getCommentsByPost(@PathVariable String username) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentsByUserName(username));
        } catch (VolunteeringSystemException volunteeringSystemException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error, user: "+username+" not found!");
        }
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getAllComments() {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllComments());
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<Object> getCommentsById(@PathVariable Long commentId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentById(commentId));
        } catch (CommentNotFoundException commentNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error comment not found!");
        }
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Object> updateComment(@PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(commentId, commentRequest));
        } catch (CommentNotFoundException commentNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error comment not found!");
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Object> deleteComment(@PathVariable Long commentId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(commentService.deleteComment(commentId));
        } catch (CommentNotFoundException commentNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error comment not found!");
        }
    }
}
