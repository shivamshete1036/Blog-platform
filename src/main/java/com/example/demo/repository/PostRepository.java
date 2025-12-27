package com.example.demo.repository;

import com.example.demo.entity.Post; // Import your Post entity
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Repository interface for Post entity.
 * It extends JpaRepository to inherit standard CRUD methods.
 * * JpaRepository<T, ID>
 * T: The Entity class (Post)
 * ID: The type of the Entity's primary key (Long)
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    // Spring Data JPA automatically provides methods like:
    // - save(Post post): Saves a new post or updates an existing one.
    // - findById(Long id): Retrieves a single post by its ID.
    // - findAll(): Retrieves all posts.
    // - deleteById(Long id): Deletes a post by its ID.
    
    // You can add custom methods here by following Spring Data naming conventions,
    // for example: List<Post> findByTitleContaining(String title);
}