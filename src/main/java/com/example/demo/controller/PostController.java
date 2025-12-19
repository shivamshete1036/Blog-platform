package com.example.demo.controller;

import com.example.demo.Post;
import com.example.demo.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller // 1. Marks this class as a Spring MVC Controller
public class PostController {

    private final PostService postService;

    // 2. Inject the PostService
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Handles GET requests to the root URL (homepage).
     * Maps the URL "/" to this method.
     */
    @GetMapping("/")
    public String listPosts(Model model) {
        // 3. Use the service layer to retrieve all posts
        //    (The service uses the repository to talk to the DB)
        
        // 4. Add the list of posts to the Model object
        //    The Model is what passes data from the Controller to the View (Thymeleaf).
        model.addAttribute("posts", postService.findAllPosts());

        // 5. Return the name of the Thymeleaf template file (without the .html extension)
        //    Spring will look for 'src/main/resources/templates/index.html'
        return "index"; 
    }

    @GetMapping("/new-post")
    public String showNewPostForm(Model model) {
        // If this line is missing, the 'th:object="${post}"' in your HTML will cause a 500 error
        model.addAttribute("post", new Post());
        return "new-post";
    }

    // --- NEW: Handle Form Submission ---
    /**
     * Handles POST request from the new-post form.
     * @ModelAttribute binds the form data to the Post object.
     */
    @PostMapping("/new-post")
    public String savePost(@ModelAttribute("post") Post post) {
        // 1. Save the post using the service
        postService.savePost(post);

        // 2. Redirect back to the homepage
        // Note: Use "redirect:/" to tell the browser to load the home page again
        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable("id") Long id, Model model) {
        // 1. Fetch the post from the service
        // .orElseThrow handles cases where someone types an ID that doesn't exist
        Post post = postService.findPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));

        // 2. Add the post object to the model
        model.addAttribute("post", post);

        // 3. Return the name of the new HTML file
        return "post-details";
    }

    @GetMapping("/post/delete/{id}")
    public String deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id);
        return "redirect:/"; // Go back to homepage after deleting
    }
}

