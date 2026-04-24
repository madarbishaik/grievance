package com.university.grievance.Repository;// GrievanceRepository.java

import com.university.grievance.entity.Grievance;
import com.university.grievance.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GrievanceRepository extends JpaRepository<Grievance, Long> {
    List<Grievance> findByStudentId(Long studentId);
    List<Grievance> findByAssignedToId(Long staffId);
    List<Grievance> findByDepartmentAndStatus(String department, Status status);
    List<Grievance> findByDepartment(String department);
    List<Grievance> findByStatus(Status status);
    long countByDepartment(String department);
    long countByDepartmentAndStatus(String department, Status status);
}