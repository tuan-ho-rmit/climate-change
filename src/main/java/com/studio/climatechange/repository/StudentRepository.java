package com.studio.climatechange.repository;
import com.studio.climatechange.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
//    Optional<StudentRepository> findByName(String name);

//    @Query("Select * from student where name = :name")
//    List<Student> getByName(String name);

}
