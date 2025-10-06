package com.project.back_end.models;

import java.beans.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

@Entity
public class Appointment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Doctor object cannot be null")
  @ManyToOne
  @JoinColumn(name = "doctorId", nullable = false)
  private Doctor doctor;

  @NotNull(message = "Patient object cannot be null")
  @ManyToOne
  @JoinColumn(name = "patientId", nullable = false)
  private Patient patient;

  @Future(message = "Appointment time must be in the future")
  @NotNull(message = "Appointment time cannot be null")
  private LocalDateTime appointmentTime;

  @NotNull(message = "Status cannot be null")
  private int status = 0; // Default to 0 (Scheduled)

  @Transient
  private LocalDateTime getEndTime() {
    return this.appointmentTime.plusHours(1);
  }

  @Transient
  private LocalDate getAppointmentDateOnly() {
    return this.appointmentTime.toLocalDate();
  }

  @Transient
  private LocalTime getAppointmentTimeOnly() {
    return this.appointmentTime.toLocalTime();
  }

  public Appointment() {
  }

  public Appointment(Doctor doctor, Patient patient, LocalDateTime appointmentTime, int status) {
    this.doctor = doctor;
    this.patient = patient;
    this.appointmentTime = appointmentTime;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Doctor getDoctor() {
    return doctor;
  }

  public void setDoctor(Doctor doctor) {
    this.doctor = doctor;
  }

  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public LocalDateTime getAppointmentTime() {
    return appointmentTime;
  }

  public void setAppointmentTime(LocalDateTime appointmentTime) {
    this.appointmentTime = appointmentTime;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }
}
