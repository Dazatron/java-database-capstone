package com.project.back_end.controllers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private Service service;

    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<?> getAppointments(
            @PathVariable String date,
            @PathVariable String patientName,
            @PathVariable String token) {
        // Validate the token for "doctor" role
        ResponseEntity<?> tokenValidationResponse = service.validateToken(token, "doctor");
        if (tokenValidationResponse.getStatusCode().isError()) {
            return tokenValidationResponse; // Return error response if token is invalid
        }

        // Token is valid, proceed to fetch appointments
        LocalDate localDate = LocalDate.parse(date);
        return appointmentService.getAppointments(patientName, localDate, token);
    }

    @PostMapping("/{token}")
    public ResponseEntity<?> bookAppointment(@RequestBody Appointment appointment, @PathVariable String token) {
        // Validate the token for "patient" role
        ResponseEntity<?> tokenValidationResponse = service.validateToken(token, "patient");
        if (tokenValidationResponse.getStatusCode().isError()) {
            return tokenValidationResponse; // Return error response if token is invalid
        }

        int isAppointmentValid = service.validateAppointment(appointment);
        if (isAppointmentValid == -1) {
            return ResponseEntity.status(400).body("Invalid doctor ID.");
        } else if (isAppointmentValid == 0) {
            return ResponseEntity.status(409).body("The selected time slot is already booked.");
        }

        // Token is valid, proceed to book the appointment
        int status = appointmentService.bookAppointment(appointment);

        if (status == 1) {
            return ResponseEntity.ok("Appointment booked successfully.");
        } else {
            return ResponseEntity.status(500).body("An unexpected error occurred.");

        }
    }

    @PutMapping("/{token}")
    public ResponseEntity<?> updateAppointment(@RequestBody Appointment appointment, @PathVariable String token) {
        // Validate the token for "patient" role
        ResponseEntity<?> tokenValidationResponse = service.validateToken(token, "patient");
        if (tokenValidationResponse.getStatusCode().isError()) {
            return tokenValidationResponse; // Return error response if token is invalid
        }

        // Token is valid, proceed to update the appointment
        return appointmentService.updateAppointment(appointment);
    }

    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id, @PathVariable String token) {
        // Validate the token for "patient" role
        ResponseEntity<?> tokenValidationResponse = service.validateToken(token, "patient");
        if (tokenValidationResponse.getStatusCode().isError()) {
            return tokenValidationResponse; // Return error response if token is invalid
        }

        // Token is valid, proceed to cancel the appointment
        return appointmentService.cancelAppointment(id, token);
    }

}
