package com.project.back_end.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Service;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private Service service;

    @GetMapping("/{token}")
    public ResponseEntity<Map<String, Object>> getPatient(@PathVariable String token) {
        Map<String, Object> response = new HashMap<>();

        // Validate the token for the "patient" user type
        var status = service.validateToken(token, "patient");

        if (status.getStatusCode() != HttpStatus.OK) {
            response.put("message", "Invalid token or user type");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Fetch and return the patient details
        var patient = patientService.getPatientDetails(token);
        if (patient != null) {
            response.put("patient", patient);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Patient not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping()
    public ResponseEntity<Map<String, String>> createPatient(@Valid @RequestBody Patient patient) {
        Map<String, String> response = new HashMap<>();

        // Check if patient already exists
        var validForRegistration = service.validatePatient(patient);
        if (!validForRegistration) {
            response.put("error", "Patient with given email or username already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        // Create the patient
        int result = patientService.createPatient(patient);
        if (result == 1) {
            response.put("message", "Patient created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response.put("error", "Failed to create patient");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Login login) {
        return service.validatePatientLogin(login);
    }

    @GetMapping("/{id}/{token}")
    public ResponseEntity<Map<String, Object>> getPatientAppointment(@PathVariable Long id,
            @PathVariable String token) {
        Map<String, Object> response = new HashMap<>();

        // Validate the token for the "patient" user type
        var status = service.validateToken(token, "patient");

        if (status.getStatusCode() != HttpStatus.OK) {
            response.put("message", "Invalid token or user type");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Fetch and return the patient's appointments
        return patientService.getPatientAppointment(id);
    }

    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<Map<String, Object>> filterPatientAppointment(
            @PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token) {
        Map<String, Object> response = new HashMap<>();

        // Validate the token for the "patient" user type
        var status = service.validateToken(token, "patient");

        if (status.getStatusCode() != HttpStatus.OK) {
            response.put("message", "Invalid token or user type");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return service.filterPatient(condition, name, token);
    }

}
