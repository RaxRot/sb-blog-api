package com.raxrot.blog.service.impl;

import com.raxrot.blog.dto.PostDTO;
import com.raxrot.blog.dto.PostResponse;
import com.raxrot.blog.entity.Post;
import com.raxrot.blog.exception.ResourceNotFoundException;
import com.raxrot.blog.repository.PostRepository;
import com.raxrot.blog.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        log.info("Creating post with title: {}", postDTO.getTitle());

        Post post = modelMapper.map(postDTO, Post.class);
        Post savedPost = postRepository.save(post);

        log.debug("Post saved: {}", savedPost);
        return modelMapper.map(savedPost, PostDTO.class);
    }

    @Override
    public PostResponse getAllPosts(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Post> posts = postRepository.findAll(pageable);

        List<PostDTO> content = posts.getContent()
                .stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .toList();

        PostResponse response = new PostResponse();
        response.setContent(content);
        response.setPageNumber(posts.getNumber());
        response.setPageSize(posts.getSize());
        response.setTotalElements(posts.getTotalElements());
        response.setTotalPages(posts.getTotalPages());
        response.setLast(posts.isLast());

        return response;
    }

    @Override
    public PostDTO getPostById(Long id) {
        log.info("Fetching post with ID: {}", id);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Post not found with ID: {}", id);
                    return new ResourceNotFoundException("Post not found with id " + id);
                });

        log.debug("Post found: {}", post);
        return modelMapper.map(post, PostDTO.class);
    }

    @Override
    public PostDTO updatePost(Long id, PostDTO postDTO) {
        log.info("Updating post with ID: {}", id);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Post not found for update with ID: {}", id);
                    return new ResourceNotFoundException("Post not found with id " + id);
                });

        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());

        Post updatedPost = postRepository.save(post);

        log.debug("Post updated: {}", updatedPost);
        return modelMapper.map(updatedPost, PostDTO.class);
    }

    @Override
    public void deletePostById(Long id) {
        log.info("Deleting post with ID: {}", id);

        if (!postRepository.existsById(id)) {
            log.warn("Post not found for deletion with ID: {}", id);
            throw new ResourceNotFoundException("Post not found with id " + id);
        }

        postRepository.deleteById(id);
        log.info("Post deleted with ID: {}", id);
    }
}
