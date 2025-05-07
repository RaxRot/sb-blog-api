package com.raxrot.blog.service;

import com.raxrot.blog.dto.PostDTO;
import com.raxrot.blog.dto.PostResponse;

public interface PostService {
    PostDTO createPost(PostDTO postDTO);
    PostResponse getAllPosts(int page, int size, String sortBy, String sortDir);
    PostDTO getPostById(Long id);
    PostDTO updatePost(Long id, PostDTO postDTO);
    void deletePostById(Long id);
}
