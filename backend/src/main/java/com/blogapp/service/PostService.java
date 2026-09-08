package com.blogapp.service;

import com.blogapp.dto.PostRequest;
import com.blogapp.exception.AccessDeniedCustomException;
import com.blogapp.exception.ResourceNotFoundException;
import com.blogapp.model.Post;
import com.blogapp.model.PostStatus;
import com.blogapp.model.User;
import com.blogapp.repository.CommentRepository;
import com.blogapp.repository.PostRepository;
import com.blogapp.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public Page<Post> getPublishedPosts(Pageable pageable) {
        return postRepository.findByStatus(PostStatus.PUBLISHED, pageable);
    }

    public Page<Post> searchPublishedPosts(String keyword, Pageable pageable) {
        return postRepository.findByStatusAndTitleContainingIgnoreCase(PostStatus.PUBLISHED, keyword, pageable);
    }

    public Page<Post> getPublishedPostsByTag(String tag, Pageable pageable) {
        return postRepository.findByStatusAndTagsContainingIgnoreCase(PostStatus.PUBLISHED, tag, pageable);
    }

    public Page<Post> getPostsByAuthor(String authorId, Pageable pageable) {
        return postRepository.findByAuthorId(authorId, pageable);
    }

    public Post getPostById(String id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
    }

    public Post createPost(PostRequest request, UserPrincipal principal) {
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setExcerpt(buildExcerpt(request.getExcerpt(), request.getContent()));
        post.setTags(request.getTags() != null ? request.getTags() : new ArrayList<>());
        post.setStatus(request.getStatus() != null ? request.getStatus() : PostStatus.PUBLISHED);
        post.setAuthorId(principal.getId());
        post.setAuthorName(principal.getUser().getName());
        return postRepository.save(post);
    }

    public Post updatePost(String id, PostRequest request, UserPrincipal principal) {
        Post post = getPostById(id);
        assertOwnerOrAdmin(post, principal);

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setExcerpt(buildExcerpt(request.getExcerpt(), request.getContent()));
        if (request.getTags() != null) {
            post.setTags(request.getTags());
        }
        if (request.getStatus() != null) {
            post.setStatus(request.getStatus());
        }
        return postRepository.save(post);
    }

    public void deletePost(String id, UserPrincipal principal) {
        Post post = getPostById(id);
        assertOwnerOrAdmin(post, principal);
        postRepository.deleteById(id);
        commentRepository.deleteByPostId(id);
    }

    public void assertOwnerOrAdmin(Post post, UserPrincipal principal) {
        boolean isAdmin = principal.getUser().getRoles().stream()
                .anyMatch(role -> role.name().equals("ADMIN"));
        boolean isOwner = post.getAuthorId().equals(principal.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedCustomException("You do not have permission to modify this post");
        }
    }

    private String buildExcerpt(String providedExcerpt, String content) {
        if (providedExcerpt != null && !providedExcerpt.isBlank()) {
            return providedExcerpt;
        }
        if (content == null) return "";
        return content.length() > 180 ? content.substring(0, 180) + "..." : content;
    }
}
