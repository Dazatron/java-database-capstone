// modals.js
import { saveDoctor } from "/js/services/doctorServices.js";

// Open modal with dynamic content
export function openModal(type) {
  const modal = document.getElementById("modal");
  const modalBody = document.getElementById("modal-body");

  // Clear existing content
  modalBody.innerHTML = "";

  // Build modal content
  switch (type) {

    case "addDoctor":
      modalBody.innerHTML = `
        <h2>Add Doctor</h2>
        <input type="text" id="doctorName" placeholder="Doctor Name" class="input-field">
        <select id="specialization" class="input-field select-dropdown">
          <option value="">Specialization</option>
          <option value="cardiologist">Cardiologist</option>
          <option value="dermatologist">Dermatologist</option>
          <option value="neurologist">Neurologist</option>
          <option value="pediatrician">Pediatrician</option>
          <option value="orthopedic">Orthopedic</option>
          <option value="gynecologist">Gynecologist</option>
          <option value="psychiatrist">Psychiatrist</option>
          <option value="dentist">Dentist</option>
          <option value="ophthalmologist">Ophthalmologist</option>
          <option value="ent">ENT Specialist</option>
          <option value="urologist">Urologist</option>
          <option value="oncologist">Oncologist</option>
          <option value="gastroenterologist">Gastroenterologist</option>
          <option value="general">General Physician</option>
        </select>
        <input type="email" id="doctorEmail" placeholder="Email" class="input-field">
        <input type="password" id="doctorPassword" placeholder="Password" class="input-field">
        <input type="text" id="doctorPhone" placeholder="Mobile No." class="input-field">
        <div class="availability-container">
          <label class="availabilityLabel">Select Availability:</label>
          <div class="checkbox-group">
            <label><input type="checkbox" name="availability" value="09:00-10:00"> 9:00 AM - 10:00 AM</label>
            <label><input type="checkbox" name="availability" value="10:00-11:00"> 10:00 AM - 11:00 AM</label>
            <label><input type="checkbox" name="availability" value="11:00-12:00"> 11:00 AM - 12:00 PM</label>
            <label><input type="checkbox" name="availability" value="12:00-13:00"> 12:00 PM - 1:00 PM</label>
          </div>
        </div>
        <button class="dashboard-btn" id="saveDoctorBtn">Save</button>
      `;

      // Attach Save button listener
      document.getElementById("saveDoctorBtn").addEventListener("click", async () => {
        const name = document.getElementById("doctorName").value.trim();
        const email = document.getElementById("doctorEmail").value.trim();
        const phone = document.getElementById("doctorPhone").value.trim();
        const password = document.getElementById("doctorPassword").value.trim();
        const specialty = document.getElementById("specialization").value.trim();
        const availableTime = [...document.querySelectorAll('input[name="availability"]:checked')].map(c => c.value);

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
            alert("Failed to add doctor: " + (result.message || "Unknown error"));
          }
        } catch (err) {
          console.error(err);
          alert("Error adding doctor. Please try again.");
        }
      });
      break;

    case "patientLogin":
      modalBody.innerHTML = `
        <h2>Patient Login</h2>
        <input type="email" id="patientEmail" placeholder="Email" class="input-field">
        <input type="password" id="patientPassword" placeholder="Password" class="input-field">
        <button class="dashboard-btn" id="loginBtn">Login</button>
      `;
      break;

    case "patientSignup":
      modalBody.innerHTML = `
        <h2>Patient Signup</h2>
        <input type="text" id="name" placeholder="Name" class="input-field">
        <input type="email" id="email" placeholder="Email" class="input-field">
        <input type="password" id="password" placeholder="Password" class="input-field">
        <input type="text" id="phone" placeholder="Phone" class="input-field">
        <input type="text" id="address" placeholder="Address" class="input-field">
        <button class="dashboard-btn" id="signupBtn">Signup</button>
      `;
      break;

    case "adminLogin":
      modalBody.innerHTML = `
        <h2>Admin Login</h2>
        <input type="text" id="username" placeholder="Username" class="input-field">
        <input type="password" id="password" placeholder="Password" class="input-field">
        <button class="dashboard-btn" id="adminLoginBtn">Login</button>
      `;
      break;

    case "doctorLogin":
      modalBody.innerHTML = `
        <h2>Doctor Login</h2>
        <input type="email" id="doctorEmail" placeholder="Email" class="input-field">
        <input type="password" id="doctorPassword" placeholder="Password" class="input-field">
        <button class="dashboard-btn" id="doctorLoginBtn">Login</button>
      `;
      break;

    default:
      modalBody.innerHTML = "<p>Unknown modal type</p>";
  }

  // Show modal
  modal.style.display = "flex";

  // Close modal button
  const closeBtn = document.getElementById("closeModal");
  if (closeBtn) closeBtn.onclick = () => { modal.style.display = "none"; };
}

// Close modal helper
export function closeModal() {
  const modal = document.getElementById("modal");
  if (modal) modal.style.display = "none";
}
