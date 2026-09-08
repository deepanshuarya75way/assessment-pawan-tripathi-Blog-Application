package com.blogapp.service;

import com.blogapp.dto.CommentRequest;
import com.blogapp.exception.AccessDeniedCustomException;
import com.blogapp.exception.ResourceNotFoundException;
import com.blogapp.model.Comment;
import com.blogapp.repository.CommentRepository;
import com.blogapp.security.UserPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;

    public CommentService(CommentRepository commentRepository, PostService postService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
    }

    public List<Comment> getCommentsForPost(String postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
    }

    public Comment addComment(String postId, CommentRequest request, UserPrincipal principal) {
        // Ensures the post exists before allowing a comment
        postService.getPostById(postId);

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setContent(request.getContent());
        comment.setAuthorId(principal.getId());
        comment.setAuthorName(principal.getUser().getName());
        return commentRepository.save(comment);
    }

    public void deleteComment(String commentId, UserPrincipal principal) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        boolean isAdmin = principal.getUser().getRoles().stream()
                .anyMatch(role -> role.name().equals("ADMIN"));
        boolean isOwner = comment.getAuthorId().equals(principal.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedCustomException("You do not have permission to delete this comment");
        }

        commentRepository.deleteById(commentId);
    }
}
