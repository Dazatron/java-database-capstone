package com.project.back_end.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Prescription;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.Service;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private Service sharedService;

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> savePrescription(
            @RequestBody @Valid Prescription prescription,
            @PathVariable String token) {
        try {
            boolean isTokenValid = sharedService.validateToken(token, "doctor").getStatusCode().is2xxSuccessful();
            if (!isTokenValid) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired token"));
            }

            // Update appointment status to indicate a prescription has been added
            Appointment appointment = appointmentService.getAppointmentById(prescription.getAppointmentId());
            appointmentService.updateAppointment(appointment);

            // Save the prescription
            prescriptionService.savePrescription(prescription);
            return ResponseEntity.ok(Map.of("message", "Prescription saved successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "An error occurred while saving the prescription"));
        }
    }

    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<?> getPrescription(
            @PathVariable Long appointmentId,
            @PathVariable String token) {
        try {
            boolean isTokenValid = sharedService.validateToken(token, "doctor").getStatusCode().is2xxSuccessful();
            if (!isTokenValid) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired token"));
            }

            return prescriptionService.getPrescription(appointmentId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", "An error occurred while retrieving the prescription"));
        }
    }

}
