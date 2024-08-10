package org.example.lab5_6_7.repositories;

import org.example.lab5_6_7.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentRepository extends JpaRepository<Student,Long> {
    List<Student> findStudentById(long kw);
    //List<Student> findStudentByName(String name);
}
