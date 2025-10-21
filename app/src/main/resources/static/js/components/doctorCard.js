// ===============================
// Imports
// ===============================
import { showBookingOverlay } from "/js/loggedPatient.js";
import { deleteDoctor } from "/js/services/doctorServices.js";
import { getPatientData } from "/js/services/patientServices.js";

// ===============================
// Function: createDoctorCard
// ===============================
export function createDoctorCard(doctor) {
  // Main card container
  const card = document.createElement("div");
  card.classList.add("doctor-card");

  // Get user role from localStorage
  const userRole = localStorage.getItem("userRole");

  // ===============================
  // Doctor Info Section
  // ===============================
  const infoContainer = document.createElement("div");
  infoContainer.classList.add("doctor-info");

  const name = document.createElement("h3");
  name.textContent = doctor.name;

  const specialization = document.createElement("p");
  specialization.textContent = `Specialization: ${doctor.specialization}`;

  const email = document.createElement("p");
  email.textContent = `Email: ${doctor.email}`;

  const times = document.createElement("p");
  times.textContent = `Available Times: ${doctor.availableTimes?.join(", ") || "N/A"}`;

  infoContainer.append(name, specialization, email, times);

  // ===============================
  // Actions Section
  // ===============================
  const actionsContainer = document.createElement("div");
  actionsContainer.classList.add("doctor-actions");

  // ===============================
  // ADMIN ACTIONS
  // ===============================
  if (userRole === "admin") {
    const deleteBtn = document.createElement("button");
    deleteBtn.textContent = "Delete";
    deleteBtn.classList.add("delete-btn");

    deleteBtn.addEventListener("click", async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        alert("Admin authentication required.");
        return;
      }

      if (!confirm(`Are you sure you want to delete Dr. ${doctor.name}?`)) return;

      try {
        const response = await deleteDoctor(doctor.id, token);
        if (response.success) {
          alert("Doctor deleted successfully.");
          card.remove();
        } else {
          alert("Failed to delete doctor: " + (response.message || "Unknown error."));
        }
      } catch (error) {
        console.error("Error deleting doctor:", error);
        alert("An error occurred while deleting the doctor.");
      }
    });

    actionsContainer.appendChild(deleteBtn);
  }

  // ===============================
  // GUEST / NOT LOGGED-IN PATIENT ACTIONS
  // ===============================
  else if (userRole === "patient" && !localStorage.getItem("token")) {
    const bookBtn = document.createElement("button");
    bookBtn.textContent = "Book Now";
    bookBtn.classList.add("book-btn");

    bookBtn.addEventListener("click", () => {
      alert("Please log in to book an appointment.");
    });

    actionsContainer.appendChild(bookBtn);
  }

  // ===============================
  // LOGGED-IN PATIENT ACTIONS
  // ===============================
  else if (userRole === "patient" && localStorage.getItem("token")) {
    const bookBtn = document.createElement("button");
    bookBtn.textContent = "Book Now";
    bookBtn.classList.add("book-btn");

    bookBtn.addEventListener("click", async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        alert("Session expired. Please log in again.");
        return;
      }

      try {
        const patient = await getPatientData(token);
        if (!patient) {
          alert("Unable to fetch patient details. Please log in again.");
          return;
        }

        // Show booking overlay using existing modal styles
        showBookingOverlay(doctor, patient);
      } catch (error) {
        console.error("Error fetching patient details:", error);
        alert("An error occurred while preparing your booking.");
      }
    });

    actionsContainer.appendChild(bookBtn);
  }

  // ===============================
  // Assemble Card
  // ===============================
  card.append(infoContainer, actionsContainer);

  return card;
}
