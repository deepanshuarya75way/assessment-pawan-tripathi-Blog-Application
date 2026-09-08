package com.blogapp.controller;

import com.blogapp.dto.PostRequest;
import com.blogapp.dto.PostResponse;
import com.blogapp.model.Post;
import com.blogapp.security.UserPrincipal;
import com.blogapp.service.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> getPosts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Post> posts;
        if (search != null && !search.isBlank()) {
            posts = postService.searchPublishedPosts(search, pageable);
        } else if (tag != null && !tag.isBlank()) {
            posts = postService.getPublishedPostsByTag(tag, pageable);
        } else {
            posts = postService.getPublishedPosts(pageable);
        }

        return ResponseEntity.ok(posts.map(PostResponse::fromEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable String id) {
        return ResponseEntity.ok(PostResponse.fromEntity(postService.getPostById(id)));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<Page<PostResponse>> getPostsByAuthor(
            @PathVariable String authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(postService.getPostsByAuthor(authorId, pageable).map(PostResponse::fromEntity));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest request,
                                                     @AuthenticationPrincipal UserPrincipal principal) {
        Post created = postService.createPost(request, principal);
        return ResponseEntity.ok(PostResponse.fromEntity(created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostResponse> updatePost(@PathVariable String id,
                                                     @Valid @RequestBody PostRequest request,
                                                     @AuthenticationPrincipal UserPrincipal principal) {
        Post updated = postService.updatePost(id, request, principal);
        return ResponseEntity.ok(PostResponse.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deletePost(@PathVariable String id,
                                            @AuthenticationPrincipal UserPrincipal principal) {
        postService.deletePost(id, principal);
        return ResponseEntity.noContent().build();
    }
}
