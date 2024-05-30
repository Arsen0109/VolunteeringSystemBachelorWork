package com.example.VolunteerWebApp.controller;

import com.example.VolunteerWebApp.DTO.ParsedPostModel;
import com.example.VolunteerWebApp.entity.ParsedPost;
import com.example.VolunteerWebApp.exception.VolunteeringSystemException;
import com.example.VolunteerWebApp.service.ParsedPostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/parsed-post")
@AllArgsConstructor
public class AdminController {

    ParsedPostService parsedPostService;

    @GetMapping
    public ResponseEntity<List<ParsedPost>> getAllParsedPosts() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(parsedPostService.getAllParsedPosts());
    }

    @PostMapping
    public ResponseEntity<List<ParsedPost>> refreshParsedPosts() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(parsedPostService.refreshParsedPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Object> getParsedPostById(@PathVariable Long postId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(parsedPostService.getParsedPost(postId));
        } catch (VolunteeringSystemException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error, post not found");
        }
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Object> updateParsedPost(@PathVariable Long postId, @RequestBody ParsedPostModel parsedPostModel) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(parsedPostService.updateParsedPost(postId, parsedPostModel));
        } catch (VolunteeringSystemException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error post not found");
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Object> deleteParsedPost(@PathVariable Long postId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(parsedPostService.deleteParsedPost(postId));
        } catch (VolunteeringSystemException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error, post not found");
        }
    }

}

