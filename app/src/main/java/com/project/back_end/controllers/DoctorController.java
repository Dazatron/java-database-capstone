package com.project.back_end.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private Service service;

    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable String date,
            @PathVariable String token) {
        Map<String, Object> response = new HashMap<>();

        // Validate the token for the specified user type
        var status = service.validateToken(token, user);

        if (status.getStatusCode() != HttpStatus.OK) {
            response.put("message", "Invalid token or user type");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Fetch and return the doctor's availability
        LocalDate dateLocal = LocalDate.parse(date);
        List<String> availableList = doctorService.getDoctorAvailability(doctorId, dateLocal);

        // check if date exists in availableList
        boolean isAvailable = availableList != null && !availableList.isEmpty();

        if (!isAvailable) {
            response.put("message", "Doctor is not available on the given date");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        response.put("doctorId", doctorId);
        response.put("date", date);
        response.put("isAvailable", isAvailable);
        response.put("availableSlots", availableList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getDoctor() {
        Map<String, Object> response = new HashMap<>();
        List<?> doctors = doctorService.getDoctors();
        response.put("doctors", doctors);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/save/{token}")
    public ResponseEntity<Map<String, Object>> saveDoctor(
            @RequestBody @Valid Doctor doctor,
            @PathVariable String token) {
        Map<String, Object> response = new HashMap<>();

        // Validate the token for the "admin" user type
        var status = service.validateToken(token, "admin");

        if (status.getStatusCode() != HttpStatus.OK) {
            response.put("message", "Invalid token or user type");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Check if the doctor already exists
        var existingDoctors = doctorService.getDoctors();
        boolean exists = existingDoctors.stream()
                .anyMatch(d -> ((Doctor) d).getEmail().equalsIgnoreCase(doctor.getEmail()));

        if (exists) {
            response.put("message", "Doctor already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        // Save the new doctor
        int isSaved = doctorService.saveDoctor(doctor);

        if (isSaved == 1) {
            response.put("message", "Doctor registered successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response.put("message", "Failed to register doctor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(@RequestBody Login login) {
        return doctorService.validateDoctor(login);
    }

    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(
            @RequestBody @Valid Doctor doctor,
            @PathVariable String token) {
        Map<String, String> response = new HashMap<>();

        // Validate the token for the "admin" user type
        var status = service.validateToken(token, "admin");

        if (status.getStatusCode() != HttpStatus.OK) {
            response.put("message", "Invalid token or user type");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Check if the doctor exists
        var existingDoctors = doctorService.getDoctors();
        boolean exists = existingDoctors.stream()
                .anyMatch(d -> ((Doctor) d).getId().equals(doctor.getId()));

        if (!exists) {
            response.put("message", "Doctor not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Update the doctor
        int isUpdated = doctorService.updateDoctor(doctor);

        if (isUpdated == 1) {
            response.put("message", "Doctor updated successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Failed to update doctor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(
            @PathVariable Long id,
            @PathVariable String token) {
        Map<String, String> response = new HashMap<>();

        // Validate the token for the "admin" user type
        var status = service.validateToken(token, "admin");

        if (status.getStatusCode() != HttpStatus.OK) {
            response.put("message", "Invalid token or user type");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Check if the doctor exists
        var existingDoctors = doctorService.getDoctors();
        boolean exists = existingDoctors.stream()
                .anyMatch(d -> ((Doctor) d).getId().equals(id));

        if (!exists) {
            response.put("message", "Doctor not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Delete the doctor
        int isDeleted = doctorService.deleteDoctor(id);

        if (isDeleted == 1) {
            response.put("message", "Doctor deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Failed to delete doctor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, Object>> filter(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality) {
        return service.filterDoctor(name, speciality, time);
    }

    ///appointments/${doctorId}/${token}
    @GetMapping("/appointments/{doctorId}/{token}")
    public ResponseEntity<Map<String, Object>> getDoctorAppointments(
            @PathVariable Long doctorId,
            @PathVariable String token) {
        Map<String, Object> response = new HashMap<>();

        // Validate the token for the "doctor" user type
        var status = service.validateToken(token, "doctor");

        if (status.getStatusCode() != HttpStatus.OK) {
            response.put("message", "Invalid token or user type");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Fetch and return the doctor's appointments
        return doctorService.getDoctorAppointments(doctorId);
    }
}
