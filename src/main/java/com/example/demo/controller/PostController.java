package com.example.demo.controller;

import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    // Inject both the PostService and UserRepository
    public PostController(PostService postService, UserRepository userRepository) {
        this.postService = postService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String listPosts(Model model) {
        model.addAttribute("posts", postService.findAllPosts());
        return "index";
    }

    @GetMapping("/new-post")
    public String showNewPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "new-post";
    }

    /**
     * Updated: Captures the 'Authentication' object to identify the logged-in user.
     */
    @PostMapping("/new-post")
    public String savePost(@ModelAttribute("post") Post post, Authentication authentication) {
        // 1. Get the username of the logged-in user
        String username = authentication.getName();

        // 2. Find the User entity from the DB
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Link the user as the author
        post.setAuthor(user);

        // 4. Save the post
        postService.savePost(post);

        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable("id") Long id, Model model) {
        Post post = postService.findPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
        model.addAttribute("post", post);
        return "post-details";
    }

    @GetMapping("/post/delete/{id}")
    public String deletePost(@PathVariable("id") Long id, Authentication authentication) {
        Post post = postService.findPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));

        // BACKEND SECURITY: Only allow the author to delete
        if (post.getAuthor().getUsername().equals(authentication.getName())) {
            postService.deletePost(id);
        }

        return "redirect:/";
    }

    @GetMapping("/post/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Post post = postService.findPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));

        // BACKEND SECURITY: Only allow the author to see the edit form
        if (!post.getAuthor().getUsername().equals(authentication.getName())) {
            return "redirect:/";
        }

        model.addAttribute("post", post);
        return "edit-post";
    }

    @PostMapping("/post/edit/{id}")
    public String updatePost(@PathVariable("id") Long id, @ModelAttribute("post") Post post, Authentication authentication) {
        // Fetch existing post to preserve the original author and ID
        Post existingPost = postService.findPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));

        // BACKEND SECURITY: Verify authorship again on submission
        if (existingPost.getAuthor().getUsername().equals(authentication.getName())) {
            existingPost.setTitle(post.getTitle());
            existingPost.setContent(post.getContent());
            postService.savePost(existingPost);
        }

        return "redirect:/post/" + id;
    }
}