function renderHeader() {
  const headerDiv = document.getElementById("header");

  if (window.location.pathname.endsWith("/")) {
    localStorage.removeItem("userRole");
    headerDiv.innerHTML = `
           <header class="header">
             <div class="logo-section">
               <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
               <span class="logo-title">Hospital CMS</span>
             </div>
           </header>`;
    return;
  }

  const role = localStorage.getItem("userRole");
  const token = localStorage.getItem("token");

  let headerContent = `<header class="header">
         <div class="logo-section">
           <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
           <span class="logo-title">Hospital CMS</span>
         </div>
         <nav>`;

  if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
    localStorage.removeItem("userRole");
    alert("Session expired or invalid login. Please log in again.");
    window.location.href = "/";
    return;
  }

  if (role === "admin") {
    headerContent += `
        <button id="addDocBtn" class="adminBtn" onclick="openModal('addDoctor')">Add Doctor</button>
        <a href="#" onclick="logout()">Logout</a>`;
  } else if (role === "doctor") {
    headerContent += `
           <button class="adminBtn"  onclick="selectRole('doctor')">Home</button>
           <a href="#" onclick="logout()">Logout</a>`;
  } else if (role === "patient") {
    headerContent += `
           <button id="patientLogin" class="adminBtn">Login</button>
           <button id="patientSignup" class="adminBtn">Sign Up</button>`;
  } else if (role === "loggedPatient") {
    headerContent += `
           <button id="home" class="adminBtn" onclick="window.location.href='/pages/loggedPatientDashboard.html'">Home</button>
           <button id="patientAppointments" class="adminBtn" onclick="window.location.href='/pages/patientAppointments.html'">Appointments</button>
           <a href="#" onclick="logoutPatient()">Logout</a>`;
  }

  headerDiv.innerHTML = headerContent;
  attachHeaderButtonListeners();
}

// Attach event listeners for header buttons
function attachHeaderButtonListeners() {
  const addDocBtn = document.getElementById("addDocBtn");
  const patientLoginBtn = document.getElementById("patientLogin");
  const patientSignupBtn = document.getElementById("patientSignup");
  

  // Patient login modal
  if (patientLoginBtn) {
    patientLoginBtn.addEventListener("click", () => {
      openModal(`<h3>Patient Login</h3>
                 <p>Login form goes here...</p>`);
    });
  }

  // Patient signup modal
  if (patientSignupBtn) {
    patientSignupBtn.addEventListener("click", () => {
      openModal(`<h3>Patient Sign Up</h3>
                 <p>Signup form goes here...</p>`);
    });
  }

  // Admin add doctor modal
  if (addDocBtn) {
    addDocBtn.addEventListener("click", () => {
      openModal(`<h3>Add Doctor</h3>
                 <p>Doctor creation form goes here...</p>`);
    });
  }
}

// Logout for Admin or Doctor
function logout() {
  localStorage.removeItem("userRole");
  localStorage.removeItem("token");
  window.location.href = "/";
}

// Logout for Logged Patient
function logoutPatient() {
  localStorage.setItem("userRole", "patient");
  localStorage.removeItem("token");
  window.location.href = "/pages/patientDashboard.html";
}

// Initialize header rendering when page loads
document.addEventListener("DOMContentLoaded", renderHeader);


