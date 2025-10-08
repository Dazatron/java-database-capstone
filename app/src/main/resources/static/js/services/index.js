import { openModal } from "./modal.js";
import { BASE_API_URL } from "./config.js";

const ADMIN_API = `${BASE_API_URL}/admin/login`;
const DOCTOR_API = `${BASE_API_URL}/doctor/login`;

// Run after DOM loads
window.onload = () => {
  const adminBtn = document.getElementById("adminBtn");
  const patientBtn = document.getElementById("patientBtn");
  const doctorBtn = document.getElementById("doctorBtn");

  if (adminBtn) {
    adminBtn.addEventListener("click", () => openModal("adminLogin"));
  }

  if (doctorBtn) {
    doctorBtn.addEventListener("click", () => openModal("doctorLogin"));
  }

  if (patientBtn) {
    patientBtn.addEventListener("click", () => openModal("patientLogin"));
  }
};


// ===============================
// Admin Login Handler
// ===============================
window.adminLoginHandler = async function () {
  const username = document.getElementById("adminUsername")?.value?.trim();
  const password = document.getElementById("adminPassword")?.value?.trim();

  if (!username || !password) {
    alert("Please enter both username and password.");
    return;
  }

  const admin = { username, password };

  try {
    const response = await fetch(ADMIN_API, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(admin),
    });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem("token", data.token);
      localStorage.setItem("userRole", "admin");
      alert("Login successful.");
      selectRole("admin");
    } else {
      alert("Invalid credentials. Please try again.");
    }
  } catch (error) {
    console.error("Error logging in admin:", error);
    alert("An error occurred while logging in. Please try again later.");
  }
};

// ===============================
// Doctor Login Handler
// ===============================
window.doctorLoginHandler = async function () {
  const email = document.getElementById("doctorEmail")?.value?.trim();
  const password = document.getElementById("doctorPassword")?.value?.trim();

  if (!email || !password) {
    alert("Please enter both email and password.");
    return;
  }

  const doctor = { email, password };

  try {
    const response = await fetch(DOCTOR_API, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(doctor),
    });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem("token", data.token);
      localStorage.setItem("userRole", "doctor");
      alert("Login successful.");
      selectRole("doctor");
    } else {
      alert("Invalid credentials. Please try again.");
    }
  } catch (error) {
    console.error("Error logging in doctor:", error);
    alert("An error occurred while logging in. Please try again later.");
  }
};
