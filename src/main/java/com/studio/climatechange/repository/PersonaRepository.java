package com.studio.climatechange.repository;
import com.studio.climatechange.models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
//    Optional<StudentRepository> findByName(String name);

//    @Query("Select * from student where name = :name")
//    List<Student> getByName(String name);

}
