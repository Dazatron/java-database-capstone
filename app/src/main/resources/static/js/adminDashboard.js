import { getDoctors, filterDoctors, saveDoctor } from "../services/doctorService.js";
import { openModal, closeModal } from "../scripts/modal.js";

// Attach event listener to "Add Doctor" button
document.getElementById("addDoctorBtn")?.addEventListener("click", () => {
  openModal("addDoctor");
});

// Load all doctors when DOM is ready
document.addEventListener("DOMContentLoaded", async () => {
  await loadDoctorCards();
});

// Function: loadDoctorCards
// Purpose: Fetch and display all doctors
async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
  } catch (error) {
    console.error("Error loading doctors:", error);
  }
}

// Attach search/filter listeners
document.getElementById("searchDoctor")?.addEventListener("input", filterDoctorsOnChange);
document.getElementById("filterTime")?.addEventListener("change", filterDoctorsOnChange);
document.getElementById("filterSpecialty")?.addEventListener("change", filterDoctorsOnChange);

// Function: filterDoctorsOnChange
// Purpose: Fetch and render filtered doctors
async function filterDoctorsOnChange() {
  const name = document.getElementById("searchDoctor")?.value.trim() || null;
  const time = document.getElementById("filterTime")?.value || null;
  const specialty = document.getElementById("filterSpecialty")?.value || null;

  try {
    const result = await filterDoctors(name, time, specialty);

    if (result.doctors && result.doctors.length > 0) {
      renderDoctorCards(result.doctors);
    } else {
      document.getElementById("content").innerHTML = `
        <p class="no-results">No doctors found with the given filters.</p>
      `;
    }
  } catch (error) {
    console.error("Error filtering doctors:", error);
    alert("Failed to filter doctors. Please try again.");
  }
}

// Function: renderDoctorCards
// Purpose: Display doctor cards
function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById("content");
  contentDiv.innerHTML = "";

  doctors.forEach((doctor) => {
    const card = createDoctorCard(doctor);
    contentDiv.appendChild(card);
  });
}

// Function: createDoctorCard
// Purpose: Create a doctor card element
function createDoctorCard(doctor) {
  const card = document.createElement("div");
  card.classList.add("doctor-card");
  card.innerHTML = `
    <h3>${doctor.name}</h3>
    <p><strong>Specialty:</strong> ${doctor.specialty}</p>
    <p><strong>Email:</strong> ${doctor.email}</p>
    <p><strong>Phone:</strong> ${doctor.phone}</p>
    <p><strong>Available Time:</strong> ${doctor.availableTime || "N/A"}</p>
  `;
  return card;
}

// Function: adminAddDoctor
// Purpose: Add a new doctor
export async function adminAddDoctor() {
  const name = document.getElementById("doctorName").value.trim();
  const email = document.getElementById("doctorEmail").value.trim();
  const phone = document.getElementById("doctorPhone").value.trim();
  const password = document.getElementById("doctorPassword").value.trim();
  const specialty = document.getElementById("doctorSpecialty").value.trim();
  const availableTime = document.getElementById("doctorAvailableTime").value.trim();

  const token = localStorage.getItem("token");
  if (!token) {
    alert("Authentication token not found. Please log in again.");
    return;
  }

  const doctor = { name, email, phone, password, specialty, availableTime };

  try {
    const result = await saveDoctor(doctor, token);
    if (result.success) {
      alert("Doctor added successfully!");
      closeModal();
      location.reload();
    } else {
      alert("Failed to add doctor: " + result.message);
    }
  } catch (error) {
    console.error("Error adding doctor:", error);
    alert("Error adding doctor. Please try again.");
  }
}
