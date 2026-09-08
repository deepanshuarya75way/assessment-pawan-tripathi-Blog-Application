package com.blogapp.controller;

import com.blogapp.dto.CommentRequest;
import com.blogapp.dto.CommentResponse;
import com.blogapp.security.UserPrincipal;
import com.blogapp.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable String postId) {
        List<CommentResponse> comments = commentService.getCommentsForPost(postId).stream()
                .map(CommentResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/posts/{postId}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentResponse> addComment(@PathVariable String postId,
                                                        @Valid @RequestBody CommentRequest request,
                                                        @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(CommentResponse.fromEntity(commentService.addComment(postId, request, principal)));
    }

    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId,
                                               @AuthenticationPrincipal UserPrincipal principal) {
        commentService.deleteComment(commentId, principal);
        return ResponseEntity.noContent().build();
    }
}
