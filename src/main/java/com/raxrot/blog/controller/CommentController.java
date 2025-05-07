package com.raxrot.blog.controller;

import com.raxrot.blog.dto.CommentDTO;
import com.raxrot.blog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Create a comment for a post")
    @ApiResponse(responseCode = "201", description = "Comment created successfully")
    @PostMapping
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentDTO commentDTO
    ) {
        log.info("POST /api/posts/{}/comments - Creating comment", postId);
        CommentDTO created = commentService.createComment(postId, commentDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all comments for a post")
    @ApiResponse(responseCode = "200", description = "List of comments")
    @GetMapping
    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@PathVariable Long postId) {
        log.info("GET /api/posts/{}/comments - Fetching comments", postId);
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    @Operation(summary = "Get a single comment by ID for a post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment found"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        log.info("GET /api/posts/{}/comments/{} - Fetching single comment", postId, commentId);
        return ResponseEntity.ok(commentService.getCommentById(postId, commentId));
    }

    @Operation(summary = "Update a comment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment updated"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentDTO commentDTO
    ) {
        log.info("PUT /api/posts/{}/comments/{} - Updating comment", postId, commentId);
        return ResponseEntity.ok(commentService.updateComment(postId, commentId, commentDTO));
    }

    @Operation(summary = "Delete a comment by ID")
    @ApiResponse(responseCode = "204", description = "Comment deleted")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        log.info("DELETE /api/posts/{}/comments/{} - Deleting comment", postId, commentId);
        commentService.deleteComment(postId, commentId);
        return ResponseEntity.noContent().build();
    }
}
