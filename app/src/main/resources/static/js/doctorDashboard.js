
import { getAllAppointments } from "/js/services/appointmentRecordService.js";
import { createPatientRow } from '/js/components/patientRows.js';



const tableBody = document.getElementById("patientTableBody");
let selectedDate = new Date().toISOString().split("T")[0];
const token = localStorage.getItem("token");
let patientName = "null"; // Default to "null" for backend compatibility




document.getElementById("searchBar")?.addEventListener("input", async (event) => {
  const input = event.target.value.trim();
  patientName = input !== "" ? input : "null";
  await loadAppointments();
});



document.getElementById("todayButton")?.addEventListener("click", async () => {
  selectedDate = new Date().toISOString().split("T")[0];
  document.getElementById("datePicker").value = selectedDate;
  await loadAppointments();
});



document.getElementById("datePicker")?.addEventListener("change", async (event) => {
  selectedDate = event.target.value;
  await loadAppointments();
});



async function loadAppointments() {
  try {
    const data = await getAllAppointments(selectedDate, patientName, token);
    const appointments = data.appointments || [];
    tableBody.innerHTML = "";

    if (appointments.length === 0) {
      tableBody.innerHTML = `<tr><td colspan="5" style="text-align:center;">No Appointments found for today.</td></tr>`;
      return;
    }

    appointments.forEach(appointment => {
      const patient = {
        id: appointment.patient?.id,
        name: appointment.patient?.name,
        phone: appointment.patient?.phone,
        email: appointment.patient?.email
      };
      const row = createPatientRow(patient, appointment.id, appointment.doctor?.id);
      tableBody.appendChild(row);
    });
  } catch (error) {
    console.error("Error loading appointments:", error);
    tableBody.innerHTML = `<tr><td colspan="5" style="text-align:center;">Error loading appointments. Try again later.</td></tr>`;
  }
}

document.addEventListener("DOMContentLoaded", async () => {
  renderContent();
  await loadAppointments();
});


