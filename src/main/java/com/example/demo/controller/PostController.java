package com.example.demo.controller;

import com.example.demo.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("http://localhost:8080")
@Controller
public class PostController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private PostRepository postRepository;
}
