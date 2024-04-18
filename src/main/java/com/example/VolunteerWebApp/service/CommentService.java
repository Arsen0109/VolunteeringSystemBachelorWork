package com.example.VolunteerWebApp.service;

import com.example.VolunteerWebApp.DTO.CommentRequest;
import com.example.VolunteerWebApp.DTO.CommentResponse;
import com.example.VolunteerWebApp.entity.Comment;
import com.example.VolunteerWebApp.entity.Post;
import com.example.VolunteerWebApp.exception.CommentNotFoundException;
import com.example.VolunteerWebApp.exception.PostNotFoundException;
import com.example.VolunteerWebApp.exception.VolunteeringSystemException;
import com.example.VolunteerWebApp.repository.CommentRepository;
import com.example.VolunteerWebApp.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public CommentResponse createComment(CommentRequest commentRequest) {
        Comment comment = commentRepository.save(mapCommentReqToComment(commentRequest));
        return mapCommentToCommentRes(comment);
    }

    @Transactional
    public List<CommentResponse> getAllComments() {
        return commentRepository.findAll()
                .stream()
                .map(this::mapCommentToCommentRes)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse getCommentById(Long commentId) throws CommentNotFoundException{
        return mapCommentToCommentRes(commentRepository.findByCommentId(commentId).orElseThrow(() ->
                new CommentNotFoundException("Error, comment with id=" + commentId + "not found!")));
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, CommentRequest commentRequest)
            throws CommentNotFoundException, VolunteeringSystemException {
        Comment comment = commentRepository.findByCommentId(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Error, comment with id=" + commentId + "not found!"));

        // Throw exception if creator user is not current user
        if (!comment.getUser().equals(authService.getCurrentUser())) {
            throw new VolunteeringSystemException("Error, to edit comments can only person who left them!");
        }

        comment.setText(commentRequest.getText());
        return mapCommentToCommentRes(commentRepository.save(comment));
    }

    @Transactional
    public CommentResponse deleteComment(Long commentId) throws CommentNotFoundException {
        Comment comment = commentRepository.findByCommentId(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Error, comment with id=" + commentId + "not found!"));
        commentRepository.delete(comment);
        return mapCommentToCommentRes(comment);
    }

    @Transactional
    public List<CommentResponse> getCommentsByPostName(Long postId) throws PostNotFoundException {
        Post post = postRepository.findByPostId(postId).orElseThrow(() ->
                new PostNotFoundException("Error post with name " + postId + " not found"));
        return commentRepository.findByPost(post)
                .stream()
                .map(this::mapCommentToCommentRes)
                .collect(Collectors.toList());
    }

    public Comment mapCommentReqToComment(CommentRequest commentRequest) {
        return Comment.builder()
                .text(commentRequest.getText())
                .post(postRepository.findByPostId(commentRequest.getPostId())
                        .orElseThrow(() -> new PostNotFoundException("Error, post with id="
                                + commentRequest.getPostId() + " not found!")))
                .user(authService.getCurrentUser())
                .createdDate(Instant.now())
                .build();
    }

    public CommentResponse mapCommentToCommentRes(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .text(comment.getText())
                .postId(comment.getPost().getPostId())
                .username(comment.getUser().getUsername())
                .createdDate(comment.getCreatedDate())
                .build();
    }
}
