// CommentRepository.java
package com.university.grievance.Repository;

import com.university.grievance.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByGrievanceIdOrderByCreatedAtDesc(Long grievanceId);
}