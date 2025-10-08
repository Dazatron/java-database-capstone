package com.project.back_end.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.project.back_end.models.Appointment;
import com.project.back_end.repo.jpa.AppointmentRepository;
import com.project.back_end.repo.jpa.DoctorRepository;

import jakarta.transaction.Transactional;

@org.springframework.stereotype.Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private Service service;

    @Autowired
    private TokenService tokenService;

    // @Autowired
    // private PatientRepository patientRepository;

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
    public Appointment getAppointmentById(Long id) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);
        return appointmentOptional.orElse(null);
    }

    @Transactional
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
        Map<String, String> response = new HashMap<>();
        Optional<Appointment> existingAppointmentOptional = appointmentRepository.findById(appointment.getId());

        if (existingAppointmentOptional.isEmpty()) {
            response.put("error", "Appointment not found");
            return ResponseEntity.status(404).body(response);
        }

        Appointment existingAppointment = existingAppointmentOptional.get();

        // validate appointment
        int status = service.validateAppointment(appointment);
        if (status != 1) {
            response.put("error", "Appointment is not available");
            return ResponseEntity.badRequest().body(response);
        }

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

        // proceed with update — update allowed fields on the managed entity
        existingAppointment.setDoctor(appointment.getDoctor());
        existingAppointment.setAppointmentTime(appointment.getAppointmentTime());
        existingAppointment.setStatus(appointment.getStatus());
        appointmentRepository.save(existingAppointment);

        response.put("message", "Appointment updated successfully");
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<Map<String, String>> cancelAppointment(Long appointmentId, String token) {
        Map<String, String> response = new HashMap<>();
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointmentId);
        if (appointmentOptional.isPresent()) {
            Appointment appointment = appointmentOptional.get();
            Boolean tokenValid = tokenService.validateToken(token, "patient");

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
            Boolean tokenValid = tokenService.validateToken(token, "doctor");
            if (!tokenValid) {
                response.put("error", "Invalid token");
                return ResponseEntity.status(403).body(response);
            }

            String identifier = tokenService.extractIdentifier(token);
            var doctor = doctorRepository.findByEmail(identifier);

            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.plusDays(1).atStartOfDay();
            var appointments = appointmentRepository.findByDoctor_IdAndAppointmentTimeBetween(doctor.getId(), start, end);
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
