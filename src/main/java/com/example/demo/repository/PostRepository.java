package com.example.demo.repository;

import com.example.demo.entity.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PostRepository extends CrudRepository<Post, Integer> {
    Optional<Post> findById(Integer id);
}
