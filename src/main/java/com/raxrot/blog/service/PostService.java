package com.raxrot.blog.service;

import com.raxrot.blog.dto.PostDTO;

import java.util.List;

public interface PostService {
    PostDTO createPost(PostDTO postDTO);
    List<PostDTO> getAllPosts();
    PostDTO getPostById(Long id);
    PostDTO updatePost(Long id, PostDTO postDTO);
    void deletePostById(Long id);
}
