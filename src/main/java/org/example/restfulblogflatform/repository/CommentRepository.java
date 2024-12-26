package org.example.restfulblogflatform.repository;

import org.example.restfulblogflatform.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}