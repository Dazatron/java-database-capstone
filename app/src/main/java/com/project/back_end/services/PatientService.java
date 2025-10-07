package com.project.back_end.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;

import jakarta.transaction.Transactional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TokenService tokenService;

    @Transactional
    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception e) {
            System.out.println("Error creating patient: " + e.getMessage());
            return 0;
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> getPatientAppointment(Long patientId) {
        try {
            var appointments = appointmentRepository.findByPatientId(patientId);
            var appointmentDTOs = appointments.stream()
                    .map(appointment -> new AppointmentDTO(
                            appointment.getId(),
                            appointment.getDoctor().getId(),
                            appointment.getDoctor().getName(),
                            appointment.getPatient().getId(),
                            appointment.getPatient().getName(),
                            appointment.getPatient().getEmail(),
                            appointment.getPatient().getPhone(),
                            appointment.getPatient().getAddress(),
                            appointment.getAppointmentTime(),
                            appointment.getStatus()))
                    .toList();
            return ResponseEntity.ok(Map.of("appointments", appointmentDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error"));
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> filterByCondition(String condition, Long Id) {
        try {
            int status;
            if (condition.equalsIgnoreCase("future")) {
                status = 0;
            } else if (condition.equalsIgnoreCase("past")) {
                status = 1;
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid condition"));
            }

            var appointments = appointmentRepository.findByPatient_IdAndStatusOrderByAppointmentTimeAsc(Id, status);
            var appointmentDTOs = appointments.stream()
                    .map(appointment -> new AppointmentDTO(
                            appointment.getId(),
                            appointment.getDoctor().getId(),
                            appointment.getDoctor().getName(),
                            appointment.getPatient().getId(),
                            appointment.getPatient().getName(),
                            appointment.getPatient().getEmail(),
                            appointment.getPatient().getPhone(),
                            appointment.getPatient().getAddress(),
                            appointment.getAppointmentTime(),
                            appointment.getStatus()))
                    .toList();
            return ResponseEntity.ok(Map.of("appointments", appointmentDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error"));
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> filterByDoctor(String doctorName, Long patientId) {
        try {
            var appointments = appointmentRepository.filterByDoctorNameAndPatientId(doctorName, patientId);
            var appointmentDTOs = appointments.stream()
                    .map(appointment -> new AppointmentDTO(
                            appointment.getId(),
                            appointment.getDoctor().getId(),
                            appointment.getDoctor().getName(),
                            appointment.getPatient().getId(),
                            appointment.getPatient().getName(),
                            appointment.getPatient().getEmail(),
                            appointment.getPatient().getPhone(),
                            appointment.getPatient().getAddress(),
                            appointment.getAppointmentTime(),
                            appointment.getStatus()))
                    .toList();
            return ResponseEntity.ok(Map.of("appointments", appointmentDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error"));
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(String doctorName, String condition,
            Long patientId) {
        try {
            int status;
            if (condition.equalsIgnoreCase("future")) {
                status = 0;
            } else if (condition.equalsIgnoreCase("past")) {
                status = 1;
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid condition"));
            }

            var appointments = appointmentRepository.filterByDoctorNameAndPatientIdAndStatus(doctorName, patientId,
                    status);
            var appointmentDTOs = appointments.stream()
                    .map(appointment -> new AppointmentDTO(
                            appointment.getId(),
                            appointment.getDoctor().getId(),
                            appointment.getDoctor().getName(),
                            appointment.getPatient().getId(),
                            appointment.getPatient().getName(),
                            appointment.getPatient().getEmail(),
                            appointment.getPatient().getPhone(),
                            appointment.getPatient().getAddress(),
                            appointment.getAppointmentTime(),
                            appointment.getStatus()))
                    .toList();
            return ResponseEntity.ok(Map.of("appointments", appointmentDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error"));
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> getPatientDetails(String token) {
        try {
            String email = tokenService.extractEmail(token);
            if (email == null) {
                return ResponseEntity.status(403).body(Map.of("error", "Invalid token"));
            }

            Patient patient = patientRepository.findByEmail(email);
            if (patient == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Patient not found"));
            }

            return ResponseEntity.ok(Map.of("patient", patient));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error"));
        }
    }

}
