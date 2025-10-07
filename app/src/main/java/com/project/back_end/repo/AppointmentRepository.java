package com.project.back_end.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.back_end.models.Appointment;

import jakarta.transaction.Transactional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {



   @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor d LEFT JOIN FETCH d.availabilities WHERE a.doctor.id = :doctorId AND a.appointmentTime BETWEEN :start AND :end ORDER BY a.appointmentTime ASC")
   public List<Appointment> findByDoctorIdAndAppointmentTimeBetween(Long doctorId, LocalDateTime start,
         LocalDateTime end);

   @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor d LEFT JOIN FETCH d.availabilities WHERE a.doctor.id = :doctorId AND LOWER(a.patient.name) LIKE LOWER(CONCAT('%', :patientName, '%')) AND a.appointmentTime BETWEEN :start AND :end ORDER BY a.appointmentTime ASC")
   public List<Appointment> findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(Long doctorId,
         String patientName, LocalDateTime start, LocalDateTime end);

   @Modifying
   @Transactional
   public void deleteAllByDoctorId(Long doctorId);

   public List<Appointment> findByPatientId(Long patientId);

   public List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(Long patientId, int status);

   public List<Appointment> findByDoctor_NameContainingIgnoreCaseAndPatient_Id(String doctorName, Long patientId);

   public List<Appointment> findByDoctor_NameContainingIgnoreCaseAndPatient_IdAndStatus(String doctorName,
         Long patientId, int status);

   @Modifying
   @Transactional
   public void updateStatus(int status, long id);

   @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor d LEFT JOIN FETCH d.availabilities WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :doctorName, '%')) AND a.patient.id = :patientId ORDER BY a.appointmentTime ASC")
   public List<Appointment> filterByDoctorNameAndPatientId(String doctorName, Long patientId);

   @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor d LEFT JOIN FETCH d.availabilities WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :doctorName, '%')) AND a.patient.id = :patientId AND a.status = :status ORDER BY a.appointmentTime ASC")
   public List<Appointment> filterByDoctorNameAndPatientIdAndStatus(String doctorName, Long patientId, int status);

}
