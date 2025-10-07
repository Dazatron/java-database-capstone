package com.project.back_end.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.back_end.models.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

   public Doctor findByEmail(String email);

   @Query("SELECT d FROM Doctor d WHERE d.name LIKE CONCAT('%', :name, '%')")
   public List<Doctor> findByNameLike(String name);

   @Query("SELECT d FROM Doctor d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))")
   public List<Doctor> findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(String name, String specialty);

   public List<Doctor> findBySpecialtyIgnoreCase(String specialty);

}