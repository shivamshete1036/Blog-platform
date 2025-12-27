package com.example.demo.service;

import com.example.demo.entity.Post; // Assuming you fixed the import and kept Post in com.example.demo
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service // 1. Marks this class as a Spring Service Component
public class PostService {

    private final PostRepository postRepository;

    // 2. Dependency Injection via Constructor
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // --- CRUD Service Methods ---

    // READ: Retrieve all posts
    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    // READ: Retrieve a single post by ID
    public Optional<Post> findPostById(Long id) {
        return postRepository.findById(id);
    }

    // CREATE / UPDATE: Save or update a post
    public Post savePost(Post post) {
        // Business Logic Example: Automatically set the creation date if it's a new post
        if (post.getDateCreated() == null) {
            post.setDateCreated(LocalDateTime.now());
        }
        return postRepository.save(post);
    }

    // DELETE: Delete a post by ID
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}