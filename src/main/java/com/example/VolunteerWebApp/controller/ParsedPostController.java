package com.example.VolunteerWebApp.controller;

import com.example.VolunteerWebApp.entity.ParsedPost;
import com.example.VolunteerWebApp.model.ParsedPostModel;
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
public class ParsedPostController {
    private ParsedPostService parsedPostService;

    @GetMapping
    public ResponseEntity<List<ParsedPost>> getAllParsedPosts() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(parsedPostService.getAllParsedPosts());
    }

    @PutMapping
    public ResponseEntity<List<ParsedPost>> refreshParsedPosts() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(parsedPostService.refreshParsedPosts());
    }
}
