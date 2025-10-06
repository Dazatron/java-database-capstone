import { BASE_API_URL } from "./config.js";

const DOCTOR_API = `${BASE_API_URL}/doctor`;

// ===============================
// Fetch All Doctors
// ===============================
export async function getDoctors() {
  try {
    const response = await fetch(`${DOCTOR_API}/getAll`);
    if (!response.ok) throw new Error("Failed to fetch doctors");
    const data = await response.json();
    return data.doctors || [];
  } catch (error) {
    console.error("Error fetching doctors:", error);
    return [];
  }
}

// ===============================
// Delete Doctor
// ===============================
export async function deleteDoctor(doctorId, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/delete/${doctorId}/${token}`, {
      method: "DELETE",
    });
    const data = await response.json();
    return {
      success: response.ok,
      message: data.message || "Delete request completed.",
    };
  } catch (error) {
    console.error("Error deleting doctor:", error);
    return { success: false, message: "Error deleting doctor." };
  }
}

// ===============================
// Save (Add) Doctor
// ===============================
export async function saveDoctor(doctor, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/save/${token}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(doctor),
    });
    const data = await response.json();
    return {
      success: response.ok,
      message: data.message || "Doctor saved successfully.",
    };
  } catch (error) {
    console.error("Error saving doctor:", error);
    return { success: false, message: "Error saving doctor." };
  }
}

// ===============================
// Filter Doctors
// ===============================
export async function filterDoctors(name, time, specialty) {
  try {
    const response = await fetch(
      `${DOCTOR_API}/filter/${name}/${time}/${specialty}`
    );
    if (response.ok) {
      const data = await response.json();
      return data;
    } else {
      console.error("Error filtering doctors:", response.status);
      return { doctors: [] };
    }
  } catch (error) {
    console.error("Error during doctor filter request:", error);
    alert("Error fetching filtered doctor list.");
    return { doctors: [] };
  }
}
