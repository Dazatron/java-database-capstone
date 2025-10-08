package com.project.back_end.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.back_end.models.Appointment;

import jakarta.transaction.Transactional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

      //@Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor d LEFT JOIN FETCH d.availabilities WHERE a.doctor.id = :doctorId AND a.appointmentTime BETWEEN :start AND :end ORDER BY a.appointmentTime ASC")
      public List<Appointment> findByDoctor_IdAndAppointmentTimeBetween(Long doctorId, LocalDateTime start,
                  LocalDateTime end);

      //@Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor d LEFT JOIN FETCH d.availabilities WHERE a.doctor.id = :doctorId AND LOWER(a.patient.name) LIKE LOWER(CONCAT('%', :patientName, '%')) AND a.appointmentTime BETWEEN :start AND :end ORDER BY a.appointmentTime ASC")
      public List<Appointment> findByDoctor_IdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(Long doctorId,
                  String patientName, LocalDateTime start, LocalDateTime end);

      @Modifying
      @Transactional
      public void deleteAllByDoctor_Id(Long doctorId);

      public List<Appointment> findByPatient_Id(Long patientId);

      public List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(Long patientId, int status);

      public List<Appointment> findByDoctor_NameContainingIgnoreCaseAndPatient_Id(String doctorName, Long patientId);

      public List<Appointment> findByDoctor_NameContainingIgnoreCaseAndPatient_IdAndStatus(String doctorName,
                  Long patientId, int status);

      @Modifying
      @Transactional
      @Query("UPDATE Appointment a SET a.status = :status WHERE a.id = :id")
      void updateStatus(@Param("status") int status, @Param("id") long id);

      @Query("""
                      SELECT a
                      FROM Appointment a
                      LEFT JOIN FETCH a.doctor d
                      WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :doctorName, '%'))
                        AND a.patient.id = :patientId
                      ORDER BY a.appointmentTime ASC
                  """)
      List<Appointment> filterByDoctorNameAndPatientId(@Param("doctorName") String doctorName,
                  @Param("patientId") Long patientId);

      @Query("""
                      SELECT a
                      FROM Appointment a
                      LEFT JOIN FETCH a.doctor d
                      WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :doctorName, '%'))
                        AND a.patient.id = :patientId
                        AND a.status = :status
                      ORDER BY a.appointmentTime ASC
                  """)
      List<Appointment> filterByDoctorNameAndPatientIdAndStatus(@Param("doctorName") String doctorName,
                  @Param("patientId") Long patientId,
                  @Param("status") int status);

}
