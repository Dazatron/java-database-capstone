package com.project.back_end.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.jpa.AppointmentRepository;
import com.project.back_end.repo.jpa.DoctorRepository;

import jakarta.transaction.Transactional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TokenService tokenService;

    @Transactional
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        try {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            var appointments = appointmentRepository.findByDoctor_IdAndAppointmentTimeBetween(doctorId, startOfDay,
                    endOfDay);
            var bookedSlots = appointments.stream()
                    .map(appointment -> appointment.getAppointmentTime().toLocalTime().toString())
                    .toArray(String[]::new);

            var doctorOptional = doctorRepository.findById(doctorId);
            var doctor = doctorOptional.orElseThrow(() -> new RuntimeException("Doctor not found"));
            var doctorTimeSlots = doctor.getAvailableTimes();

            return doctorTimeSlots.stream()
                    .filter(slot -> {
                        String slotStart = slot.split("-")[0];
                        for (String booked : bookedSlots) {
                            if (booked.compareTo(slotStart) >= 0 && booked.compareTo(slot.split("-")[1]) < 0) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .toList();

        } catch (Exception e) {
            return e.getMessage().isEmpty() ? List.of() : List.of(e.getMessage());
        }
    }

    @Transactional
    public int saveDoctor(Doctor doctor) {
        try {
            var existingDoctor = doctorRepository.findByEmail(doctor.getEmail());
            if (existingDoctor != null) {
                return -1;
            }
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public int updateDoctor(Doctor doctor) {
        try {
            var existingDoctor = doctorRepository.findById(doctor.getId());
            if (existingDoctor.isEmpty()) {
                return -1;
            }
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public List<Doctor> getDoctors() {
        try {
            return doctorRepository.findAll();
        } catch (Exception e) {
            return List.of();
        }
    }

    @Transactional
    public int deleteDoctor(Long doctorId) {
        try {
            var existingDoctor = doctorRepository.findById(doctorId);
            if (existingDoctor.isEmpty()) {
                return -1;
            }
            appointmentRepository.deleteAllByDoctor_Id(doctorId);
            doctorRepository.deleteById(doctorId);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public ResponseEntity<Map<String, String>> validateDoctor(Login login) {
        Map<String, String> response = new HashMap<>();
        try {
            var doctor = doctorRepository.findByEmail(login.getIdentifier());
            if (doctor == null) {
                response.put("error", "Doctor not found");
                return ResponseEntity.status(404).body(response);
            }
            if (!doctor.getPassword().equals(login.getPassword())) {
                response.put("error", "Invalid password");
                return ResponseEntity.status(401).body(response);
            }
            String token = tokenService.generateToken(login.getIdentifier());
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Internal server error");
            return ResponseEntity.status(500).body(response);
        }
    }

    @Transactional
    public Map<String, Object> findDoctorByName(String name) {
        try {
            var doctors = doctorRepository.findByNameLike(name);
            Map<String, Object> response = new HashMap<>();
            response.put("doctors", doctors);
            return response;
        } catch (Exception e) {
            return Map.of("error", "Internal server error");
        }
    }

    @Transactional
    public Map<String, Object> filterDoctorsByNameSpecialityandTime(String name, String speciality, String amOrPm) {
        try {
            var doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, speciality);
            if (amOrPm != null && !amOrPm.isEmpty()) {
                doctors = filterDoctorByTime(doctors, amOrPm);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("doctors", doctors);
            return response;
        } catch (Exception e) {
            return Map.of("error", "Internal server error");
        }
    }

    @Transactional
    private List<Doctor> filterDoctorByTime(List<Doctor> doctors, String amOrPm) {
        return doctors.stream()
                .filter(doctor -> doctor.getAvailableTimes().stream().anyMatch(timeSlot -> {
                    String startTime = timeSlot.split("-")[0];
                    int hour = Integer.parseInt(startTime.split(":")[0]);
                    return (amOrPm.equalsIgnoreCase("AM") && hour < 12) ||
                            (amOrPm.equalsIgnoreCase("PM") && hour >= 12);
                }))
                .toList();
    }

    @Transactional
    public Map<String, Object> filterDoctorByNameAndTime(String name, String amOrPm) {
        try {
            var doctors = doctorRepository.findByNameLike(name);
            if (amOrPm != null && !amOrPm.isEmpty()) {
                doctors = filterDoctorByTime(doctors, amOrPm);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("doctors", doctors);
            return response;
        } catch (Exception e) {
            return Map.of("error", "Internal server error");
        }
    }

    @Transactional
    public Map<String, Object> filterDoctorByNameAndSpecility(String name, String specility) {
        try {
            var doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specility);
            Map<String, Object> response = new HashMap<>();
            response.put("doctors", doctors);
            return response;
        } catch (Exception e) {
            return Map.of("error", "Internal server error");
        }
    }

    @Transactional
    public Map<String, Object> filterDoctorByTimeAndSpecility(String specility, String amOrPm) {
        try {
            var doctors = doctorRepository.findBySpecialtyIgnoreCase(specility);
            if (amOrPm != null && !amOrPm.isEmpty()) {
                doctors = filterDoctorByTime(doctors, amOrPm);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("doctors", doctors);
            return response;
        } catch (Exception e) {
            return Map.of("error", "Internal server error");
        }
    }

    @Transactional
    public Map<String, Object> filterDoctorBySpecility(String specility) {
        try {
            var doctors = doctorRepository.findBySpecialtyIgnoreCase(specility);
            Map<String, Object> response = new HashMap<>();
            response.put("doctors", doctors);
            return response;
        } catch (Exception e) {
            return Map.of("error", "Internal server error");
        }
    }

    @Transactional
    public Map<String, Object> filterDoctorsByTime(String amOrPm) {
        try {
            var doctors = doctorRepository.findAll();
            if (amOrPm != null && !amOrPm.isEmpty()) {
                doctors = filterDoctorByTime(doctors, amOrPm);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("doctors", doctors);
            return response;
        } catch (Exception e) {
            return Map.of("error", "Internal server error");
        }
    }

    @Transactional
    public List<Doctor> filterDoctorByName(List<Doctor> doctors, String name) {
        return doctors.stream()
                .filter(d -> d.getName() != null && d.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    @Transactional
    public List<Doctor> filterDoctorBySpeciality(List<Doctor> doctors, String speciality) {
        return doctors.stream()
                .filter(d -> d.getSpecialty() != null && d.getSpecialty().equalsIgnoreCase(speciality))
                .toList();
    }

    @Transactional
    public List<Doctor> filterDoctorByExactTime(List<Doctor> doctors, String timeRange) {
        return doctors.stream()
                .filter(doctor -> doctor.getAvailableTimes() != null &&
                        doctor.getAvailableTimes().stream()
                                .anyMatch(slot -> slot.equalsIgnoreCase(timeRange)))
                .toList();
    }

    @Transactional
    public List<Doctor> filterDoctorsByTimePeriod(List<Doctor> doctors, String amOrPm) {
        return doctors.stream()
                .filter(doctor -> doctor.getAvailableTimes() != null &&
                        doctor.getAvailableTimes().stream().anyMatch(available_times -> {
                            String startTime = available_times.split("-")[0];
                            int hour = Integer.parseInt(startTime.split(":")[0]);
                            return (amOrPm.equalsIgnoreCase("AM") && hour < 12) ||
                                    (amOrPm.equalsIgnoreCase("PM") && hour >= 12);
                        }))
                .toList();
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> getDoctorAppointments(Long doctorId)
    {
        try {
            var appointments = appointmentRepository.findByDoctor_Id(doctorId);
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
}
