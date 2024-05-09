package com.example.VolunteerWebApp.service;

import com.example.VolunteerWebApp.DTO.PostRequest;
import com.example.VolunteerWebApp.DTO.PostResponse;
import com.example.VolunteerWebApp.entity.User;
import com.example.VolunteerWebApp.exception.PostNotFoundException;
import com.example.VolunteerWebApp.entity.Post;
import com.example.VolunteerWebApp.repository.PostRepository;
import com.example.VolunteerWebApp.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final AuthService authService;

    @Transactional
    public PostResponse createPost(PostRequest postRequest) {
        Post post = postRepository.save(mapPostRequestToPost(postRequest));
        return mapPostToPostResponse(post);
    }

    @Transactional
    public PostResponse getPostById (Long postId) throws PostNotFoundException {
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
    public PostResponse updatePost(Long postId, PostRequest postRequest) throws PostNotFoundException {
        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new PostNotFoundException("Error, can not find post with postId: " + postId));
        post.setPostName(postRequest.getPostName());
        post.setDescription(postRequest.getDescription());
        post.setMonobankJarLink(postRequest.getMonoBankJarLink());
        post.setIsOpened(postRequest.getIsOpened());
        return mapPostToPostResponse(postRepository.save(post));
    }

    @Transactional
    public PostResponse deletePost(Long postId) throws PostNotFoundException {
        Post post = postRepository.findByPostId(postId).orElseThrow(() ->
                new PostNotFoundException("Error, can not find post with postId: " + postId));
        postRepository.delete(post);
        return mapPostToPostResponse(post);
    }

    @Transactional
    public List<PostResponse> getPostsByUserName(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new RuntimeException("Error user not found"));
        return postRepository.findByUser(user)
                .stream()
                .map(this::mapPostToPostResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse getPostByPostName(String postName) {
        return mapPostToPostResponse(postRepository.findByPostName(postName).orElseThrow(() ->
                new PostNotFoundException("Error post with name " + postName + " not found!")));
    }

    public Post mapPostRequestToPost(PostRequest postRequest) {
        return Post.builder()
                .postName(postRequest.getPostName())
                .monobankJarLink(postRequest.getMonoBankJarLink())
                .description(postRequest.getDescription())
                .user(authService.getCurrentUser())
                .isOpened(true)
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
                .isOpened(post.getIsOpened())
                .createdDate(post.getCreatedDate())
                .build();
    }
}
