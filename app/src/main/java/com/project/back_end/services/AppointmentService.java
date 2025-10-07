package com.project.back_end.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import jakarta.transaction.Transactional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private Service service;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
        Map<String, String> response = new HashMap<>();
        Optional<Appointment> existingAppointmentOptional = appointmentRepository.findById(appointment.getId());

        if (existingAppointmentOptional.isPresent()) {
            Appointment existingAppointment = existingAppointmentOptional.get();

            // validate appointment
            boolean isValid = service.validateAppointment(appointment);
            if (!isValid) {
                response.put("error", "Appointment is not available");
                return ResponseEntity.badRequest().body(response);
            } else {
                // check patient match
                if (!existingAppointment.getPatient().getId().equals(appointment.getPatient().getId())) {
                    response.put("error", "You can only update your own appointments");
                    return ResponseEntity.status(403).body(response);
                }

                // check if appointment is available for updating
                if (existingAppointment.getStatus() != 0) {
                    response.put("error", "Appointment cannot be updated");
                    return ResponseEntity.badRequest().body(response);
                }

                // proceed with update
                existingAppointment.setDoctor(appointment.getDoctor());
                existingAppointment.setPatient(appointment.getPatient());
                existingAppointment.setAppointmentTime(appointment.getAppointmentTime());
                existingAppointment.setStatus(appointment.getStatus());
                appointmentRepository.save(existingAppointment);
                response.put("message", "Appointment updated successfully");
                return ResponseEntity.ok(response);
            }
        }
    }

    @Transactional
    public ResponseEntity<Map<String, String>> cancelAppointment(Long appointmentId, String token) {
        Map<String, String> response = new HashMap<>();
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointmentId);
        if (appointmentOptional.isPresent()) {
            Appointment appointment = appointmentOptional.get();
            Boolean tokenValid = tokenService.validateToken(token);

            if (!tokenValid) {
                response.put("error", "Invalid token");
                return ResponseEntity.status(403).body(response);
            }

            try {
                appointmentRepository.delete(appointment);
                response.put("message", "Appointment cancelled successfully");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                response.put("error", "Failed to cancel appointment");
                return ResponseEntity.status(500).body(response);
            }
        } else {
            response.put("error", "Appointment not found");
            return ResponseEntity.status(404).body(response);
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> getAppointments(String pname, LocalDate date, String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Hint: Use appointmentRepository.findByDoctorIdAndAppointmentTimeBetween()
            Boolean tokenValid = tokenService.validateToken(token);
            if (!tokenValid) {
                response.put("error", "Invalid token");
                return ResponseEntity.status(403).body(response);
            }

            Long doctorId = tokenService.getUserIdFromToken(token);
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.plusDays(1).atStartOfDay();
            var appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);
            if (pname != null && !pname.isEmpty()) {
                appointments = appointments.stream()
                        .filter(app -> app.getPatient().getName().toLowerCase().contains(pname.toLowerCase()))
                        .toList();
            }
            response.put("appointments", appointments);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Failed to retrieve appointments");
            return ResponseEntity.status(500).body(response);
        }
    }

    @Transactional
    public ResponseEntity<Map<String, String>> changeStatus(Long appointmentId, int status) {
        Map<String, String> response = new HashMap<>();
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointmentId);
        if (appointmentOptional.isPresent()) {
            Appointment appointment = appointmentOptional.get();
            appointment.setStatus(status);
            appointmentRepository.save(appointment);
            response.put("message", "Appointment status updated successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Appointment not found");
            return ResponseEntity.status(404).body(response);
        }
    }

}
