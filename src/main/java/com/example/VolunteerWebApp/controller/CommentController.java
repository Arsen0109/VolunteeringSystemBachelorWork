package com.example.VolunteerWebApp.controller;

import com.example.VolunteerWebApp.DTO.CommentRequest;
import com.example.VolunteerWebApp.DTO.CommentResponse;
import com.example.VolunteerWebApp.exception.PostNotFoundException;
import com.example.VolunteerWebApp.exception.VolunteeringSystemException;
import com.example.VolunteerWebApp.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
}
