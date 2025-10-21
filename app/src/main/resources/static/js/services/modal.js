// modal.js

// Function to open a modal with dynamic content
export function openModal(type) {
  const modal = document.getElementById("modal");
  const modalBody = document.getElementById("modal-body");
  const closeBtn = modal.querySelector(".close-btn");

  // Clear old content
  modalBody.innerHTML = "";

  // Decide what content to load
  switch (type) {
    case "adminLogin":
      modalBody.innerHTML = `
        <h2>Admin Login</h2>
        <input type="text" id="adminUsername" placeholder="Username">
        <input type="password" id="adminPassword" placeholder="Password">
        <button class="buttonLogin" id="adminLogin">Login</button>
      `;
      break;

    case "doctorLogin":
      modalBody.innerHTML = `
        <h2>Doctor Login</h2>
        <input type="email" id="doctorEmail" placeholder="Email">
        <input type="password" id="doctorPassword" placeholder="Password">
        <button class="buttonLogin" id="doctorLogin">Login</button>
      `;
      break;

    case "patientLogin":
      modalBody.innerHTML = `
        <h2>Patient Login</h2>
        <input type="email" id="patientEmail" placeholder="Email">
        <input type="password" id="patientPassword" placeholder="Password">
        <button class="buttonLogin" id="patientLogin">Login</button>
      `;
      break;

    default:
      modalBody.innerHTML = "<p>Unknown modal type</p>";
  }

  // Show modal
  modal.style.display = "flex";
  modal.setAttribute("aria-hidden", "false");

  // Attach login handlers if they exist globally
  const adminLoginBtn = document.getElementById("adminLogin");
  const doctorLoginBtn = document.getElementById("doctorLogin");
  const patientLoginBtn = document.getElementById("patientLogin");

  if (adminLoginBtn) adminLoginBtn.addEventListener("click", window.adminLoginHandler);
  if (doctorLoginBtn) doctorLoginBtn.addEventListener("click", window.doctorLoginHandler);
  if (patientLoginBtn) patientLoginBtn.addEventListener("click", window.patientLoginHandler);

  // Close modal logic
  closeBtn.onclick = closeModal;
  modal.onclick = (e) => {
    if (e.target === modal) closeModal(); // click outside closes modal
  };
}

// Function to close modal
export function closeModal() {
  const modal = document.getElementById("modal");
  modal.style.display = "none";
  modal.setAttribute("aria-hidden", "true");
}
