package org.example.restfulblogflatform.repository;

import org.example.restfulblogflatform.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}