package com.hahn.supportticketsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hahn.supportticketsystem.model.Comment;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
}
