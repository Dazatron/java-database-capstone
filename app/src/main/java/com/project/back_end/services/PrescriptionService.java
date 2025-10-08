package com.project.back_end.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.back_end.models.Prescription;
import com.project.back_end.repo.mongo.PrescriptionRepository;

import jakarta.transaction.Transactional;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Transactional
    public ResponseEntity<Map<String, String>> savePrescription(Prescription prescription) {
        try {
            var existingPrescriptions = prescriptionRepository.findByAppointmentId(prescription.getAppointmentId());
            if (existingPrescriptions != null) {
                return ResponseEntity.status(400)
                        .body(Map.of("error", "Prescription already exists for this appointment"));
            }

            prescriptionRepository.save(prescription);
            return ResponseEntity.status(201).body(Map.of("message", "Prescription saved successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "An error occurred while saving the prescription"));
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> getPrescription(Long appointmentId) {
        try {
            var prescriptions = prescriptionRepository.findByAppointmentId(appointmentId);
            return ResponseEntity.ok(Map.of("prescription", prescriptions));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", "An error occurred while fetching the prescription"));
        }

    }

}
