package com.raxrot.blog.controller;

import com.raxrot.blog.dto.PostDTO;
import com.raxrot.blog.dto.PostResponse;
import com.raxrot.blog.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Create a new post")
    @ApiResponse(responseCode = "201", description = "Post created successfully")
    @PostMapping
    public ResponseEntity<PostDTO> create(@Valid @RequestBody PostDTO postDto) {
        log.info("POST /api/posts - Creating post: {}", postDto.getTitle());
        PostDTO createdPost = postService.createPost(postDto);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all posts with pagination and sorting")
    @ApiResponse(responseCode = "200", description = "List of posts")
    @GetMapping
    public ResponseEntity<PostResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        log.info("GET /api/posts?page={}&size={}&sortBy={}&sortDir={}", page, size, sortBy, sortDir);
        return ResponseEntity.ok(postService.getAllPosts(page, size, sortBy, sortDir));
    }

    @Operation(summary = "Get a post by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post found"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getById(@PathVariable Long id) {
        log.info("GET /api/posts/{} - Fetching post by ID", id);
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @Operation(summary = "Update a post by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post updated successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> update(@PathVariable Long id, @Valid @RequestBody PostDTO postDto) {
        log.info("PUT /api/posts/{} - Updating post", id);
        return ResponseEntity.ok(postService.updatePost(id, postDto));
    }

    @Operation(summary = "Delete a post by ID")
    @ApiResponse(responseCode = "204", description = "Post deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/posts/{} - Deleting post", id);
        postService.deletePostById(id);
        return ResponseEntity.noContent().build();
    }
}
