package com.raxrot.blog.service.impl;

import com.raxrot.blog.dto.CommentDTO;
import com.raxrot.blog.entity.Comment;
import com.raxrot.blog.entity.Post;
import com.raxrot.blog.exception.ResourceNotFoundException;
import com.raxrot.blog.repository.CommentRepository;
import com.raxrot.blog.repository.PostRepository;
import com.raxrot.blog.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    public CommentServiceImpl(CommentRepository commentRepository,PostRepository postRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentDTO createComment(Long postId, CommentDTO commentDTO) {
        log.info("Creating comment for post ID: {}", postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        Comment comment = modelMapper.map(commentDTO, Comment.class);
        comment.setPost(post);

        Comment saved = commentRepository.save(comment);
        return modelMapper.map(saved, CommentDTO.class);
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        log.info("Fetching comments for post ID: {}", postId);

        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .toList();
    }

    @Override
    public CommentDTO getCommentById(Long postId, Long commentId) {
        log.info("Fetching comment ID {} for post ID {}", commentId, postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new RuntimeException("Comment does not belong to post");
        }

        return modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    public CommentDTO updateComment(Long postId, Long commentId, CommentDTO commentDTO) {
        log.info("Updating comment ID {} for post ID {}", commentId, postId);

        Comment comment = getCommentEntity(postId, commentId);

        comment.setName(commentDTO.getName());
        comment.setEmail(commentDTO.getEmail());
        comment.setBody(commentDTO.getBody());

        Comment updated = commentRepository.save(comment);
        return modelMapper.map(updated, CommentDTO.class);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        log.info("Deleting comment ID {} for post ID {}", commentId, postId);

        Comment comment = getCommentEntity(postId, commentId);
        commentRepository.delete(comment);
    }

    private Comment getCommentEntity(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new RuntimeException("Comment does not belong to post");
        }

        return comment;
    }
}
