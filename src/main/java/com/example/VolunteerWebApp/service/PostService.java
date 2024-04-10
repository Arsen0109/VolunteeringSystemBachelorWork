package com.example.VolunteerWebApp.service;

import com.example.VolunteerWebApp.DTO.PostRequest;
import com.example.VolunteerWebApp.DTO.PostResponse;
import com.example.VolunteerWebApp.Exception.PostNotFoundException;
import com.example.VolunteerWebApp.entity.Post;
import com.example.VolunteerWebApp.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public PostResponse createPost(PostRequest postRequest) {
        Post post = postRepository.save(mapPostRequestToPost(postRequest));
        return mapPostToPostResponse(post);
    }

    @Transactional
    public PostResponse getPostById(Long postId) {
        return mapPostToPostResponse(postRepository.findByPostId(postId).orElseThrow(() ->
                new PostNotFoundException("Error, can not find post with postId: " + postId)));
    }

    @Transactional
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::mapPostToPostResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse updatePost(Long postId, PostRequest postRequest) {
        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new PostNotFoundException("Error, can not find post with postId: " + postId));
        post.setPostName(postRequest.getPostName());
        post.setDescription(postRequest.getDescription());
        post.setMonobankJarLink(postRequest.getMonoBankJarLink());
        return mapPostToPostResponse(postRepository.save(post));
    }

    public PostResponse deletePost(Long postId){
        Post post = postRepository.findByPostId(postId).orElseThrow(() ->
                new PostNotFoundException("Error, can not find post with postId: " + postId));
        postRepository.delete(post);
        return mapPostToPostResponse(post);
    }


    public Post mapPostRequestToPost(PostRequest postRequest) {
        return Post.builder()
                .postName(postRequest.getPostName())
                .monobankJarLink(postRequest.getMonoBankJarLink())
                .description(postRequest.getDescription())
                .user(authService.getCurrentUser())
                .createdDate(Instant.now())
                .build();
    }
    public PostResponse mapPostToPostResponse(Post post){
        return PostResponse.builder()
                .postId(post.getPostId())
                .postName(post.getPostName())
                .description(post.getDescription())
                .monoBankJarLink(post.getMonobankJarLink())
                .username(post.getUser().getUsername())
                .createdDate(post.getCreatedDate())
                .build();
    }
}
