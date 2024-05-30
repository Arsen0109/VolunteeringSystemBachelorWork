package com.example.VolunteerWebApp.controller;

import com.example.VolunteerWebApp.DTO.PostRequest;
import com.example.VolunteerWebApp.DTO.PostResponse;
import com.example.VolunteerWebApp.exception.NotValidCardNumberException;
import com.example.VolunteerWebApp.exception.PostNotFoundException;
import com.example.VolunteerWebApp.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/post")
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPosts());
    }

    @PostMapping
    public ResponseEntity<Object> createPost(@RequestBody PostRequest postRequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(postRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error bad request.");
        }
    }

    @GetMapping("by-name/{postName}")
    public ResponseEntity<Object> getPostByName(@PathVariable String postName) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(postService.getPostByPostName(postName));
        } catch (PostNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error, post with name " + postName + " not found");
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Object> getPostById(@PathVariable Long postId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(postService.getPostById(postId));
        } catch (PostNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error, post with id=" + postId + " not found");
        }
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Object> updatePost(@PathVariable Long postId, @RequestBody PostRequest postRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(postService.updatePost(postId, postRequest));
        } catch (PostNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error, post with id=" + postId + " not found");
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Object> deletePost(@PathVariable Long postId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(postService.deletePost(postId));
        } catch (PostNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error, post with id=" + postId + " not found");
        }
    }

    @GetMapping("by-user/{username}")
    public ResponseEntity<Object> getPostByUserName(@PathVariable String username) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsByUserName(username));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error, post with username=" + username + " not found");
        }
    }
}
