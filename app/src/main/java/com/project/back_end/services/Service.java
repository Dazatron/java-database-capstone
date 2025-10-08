package com.project.back_end.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import jakarta.transaction.Transactional;

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Transactional
    public ResponseEntity<Map<String, String>> validateToken(String token, String userType) {
        try {
            boolean isValid = tokenService.validateToken(token, userType);
            if (isValid) {
                return ResponseEntity.ok(Map.of("message", "Token is valid"));
            } else {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired token"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "An error occurred during token validation"));
        }
    }

    @Transactional
    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {
        try {
            var admin = adminRepository.findByUsername(receivedAdmin.getUsername());
            if (admin != null) {
                if (admin.getPassword().equals(receivedAdmin.getPassword())) {
                    String token = tokenService.generateToken(admin.getUsername());
                    return ResponseEntity.ok(Map.of("token", token));
                } else {
                    return ResponseEntity.status(401).body(Map.of("error", "Invalid password"));
                }
            } else {
                return ResponseEntity.status(401).body(Map.of("error", "Admin not found"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "An error occurred during admin validation"));
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> filterDoctor(String name, String specialty, String time) {
        try {
            Map<String, Object> response = doctorService.filterDoctorsByNameSpecilityandTime(name, specialty, time);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "An error occurred during doctor filtering"));
        }
    }

    @Transactional
    public int validateAppointment(Appointment appointment) {
        var doctor = doctorRepository.findById(appointment.getDoctor().getId());
        if (doctor.isEmpty()) {
            return -1;
        } else {
            var availableSlots = doctorService.getDoctorAvailability(appointment.getDoctor().getId(),
                    appointment.getAppointmentTime().toLocalDate());
            for (var slot : availableSlots) {
                var slotLocalTime = java.time.LocalTime.parse(slot.split("-")[0]);
                if (slotLocalTime.equals(appointment.getAppointmentTime().toLocalTime())) {
                    return 1;
                }
            }
            return 0;
        }
    }

    @Transactional
    public boolean validatePatient(Patient patient) {
        var existingPatient = patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone());

        if (existingPatient != null) {
            return false;
        } else {
            return true;

        }
    }

    @Transactional
    public ResponseEntity<Map<String, String>> validatePatientLogin(Login login) {
        Map<String, String> response = new HashMap<>();
        try {
            var patient = patientRepository.findByEmail(login.getIdentifier());
            if (patient == null) {
                response.put("error", "Patient not found");
                return ResponseEntity.status(404).body(response);
            }
            if (!patient.getPassword().equals(login.getPassword())) {
                response.put("error", "Invalid password");
                return ResponseEntity.status(401).body(response);
            }
            String token = tokenService.generateToken(patient.getEmail());
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Internal server error");
            return ResponseEntity.status(500).body(response);
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> filterPatient(String condition, String name, String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = tokenService.extractIdentifier(token);
            if (email == null || email.isEmpty()) {
                response.put("error", "Invalid token");
                return ResponseEntity.status(403).body(response);
            }

            Patient patient = patientRepository.findByEmail(email);
            Long patientId = patient.getId();

            if ((condition == null || condition.isEmpty()) && (name == null || name.isEmpty())) {
                return patientService.getPatientAppointment(patientId);
            } else if (condition != null && !condition.isEmpty() && (name == null || name.isEmpty())) {
                return patientService.filterByCondition(condition, patientId);
            } else if ((condition == null || condition.isEmpty()) && name != null && !name.isEmpty()) {
                return patientService.filterByDoctor(name, patientId);
            } else {
                return patientService.filterByDoctorAndCondition(name, condition, patientId);
            }
            // return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Internal server error");
            return ResponseEntity.status(500).body(response);
        }
    }

}
