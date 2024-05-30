package com.example.VolunteerWebApp.service;

import com.example.VolunteerWebApp.DTO.PostRequest;
import com.example.VolunteerWebApp.DTO.PostResponse;
import com.example.VolunteerWebApp.entity.User;
import com.example.VolunteerWebApp.exception.NotValidCardNumberException;
import com.example.VolunteerWebApp.exception.PostNotFoundException;
import com.example.VolunteerWebApp.entity.Post;
import com.example.VolunteerWebApp.exception.VolunteeringSystemException;
import com.example.VolunteerWebApp.model.MonoBankJarRequest;
import com.example.VolunteerWebApp.repository.PostRepository;
import com.example.VolunteerWebApp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
        // Throw exception if creator user is not current user
        if (!post.getUser().equals(authService.getCurrentUser()) && !authService.getCurrentUser().isAdmin()) {
            throw new VolunteeringSystemException("Error, to edit comments can only person who left them!");
        }
        post.setPostName(postRequest.getPostName());
        post.setDescription(postRequest.getDescription());
        post.setMonobankJarLink(postRequest.getMonoBankJarLink());
        post.setIsOpened(postRequest.getIsOpened());
        post.setCardNumber(postRequest.getCardNumber());
        return mapPostToPostResponse(postRepository.save(post));
    }

    @Transactional
    public PostResponse deletePost(Long postId) throws PostNotFoundException {
        Post post = postRepository.findByPostId(postId).orElseThrow(() ->
                new PostNotFoundException("Error, can not find post with postId: " + postId));
        if (!post.getUser().equals(authService.getCurrentUser()) && !authService.getCurrentUser().isAdmin()) {
            throw new VolunteeringSystemException("Error, to edit comments can only person who left them!");
        }
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

    public ResponseEntity<Object> getMonoBankJarProgress(String monoBankClientId) {
        String url = "https://send.monobank.ua/api/handler";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        // Створення об'єкта HttpEntity з заголовками і тілом запиту
        HttpEntity<MonoBankJarRequest> request = new HttpEntity<>(new MonoBankJarRequest(monoBankClientId), headers);

        // Надсилання запиту
        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, request, Object.class);

        // Отримання і обробка відповіді
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        } else {
            throw new RuntimeException("Failed to send POST request: " + response.getStatusCode());
        }
    }
    public String validateCardNumber(String cardNumber) {
        String trimmedCardNumber = cardNumber.replace(" ", "");
        if (trimmedCardNumber.length() != 16) {
            throw new NotValidCardNumberException("Error, invalid format, card number must be a 16 digits number");
        }
        try {
            Long.parseLong(trimmedCardNumber);
        } catch (NumberFormatException e) {
            throw new NotValidCardNumberException("Error, invalid format, card must not contain any symbols " +
                    "except numbers");
        }
        return trimmedCardNumber;
    }

    public Post mapPostRequestToPost(PostRequest postRequest) {
        return Post.builder()
                .postName(postRequest.getPostName())
                .monobankJarLink(postRequest.getMonoBankJarLink())
                .description(postRequest.getDescription())
                .cardNumber(validateCardNumber(postRequest.getCardNumber()))
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
                .cardNumber(post.getCardNumber())
                .username(post.getUser().getUsername())
                .isOpened(post.getIsOpened())
                .createdDate(post.getCreatedDate())
                .build();
    }
}
