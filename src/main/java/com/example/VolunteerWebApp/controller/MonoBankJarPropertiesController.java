package com.example.VolunteerWebApp.controller;

import com.example.VolunteerWebApp.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/monobank/jar-props")
@AllArgsConstructor
public class MonoBankJarPropertiesController {
    private final PostService postService;

    @GetMapping("/{jarId}")
    public ResponseEntity<Object> getMonoBankJarProps(@PathVariable String jarId) {
        return postService.getMonoBankJarProgress(jarId);
    }
}
