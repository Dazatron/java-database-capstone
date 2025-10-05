# User Stories – Healthcare Appointment Platform

## Admin User Stories

### 1. Manage Platform Login
**As an** ADMIN, **I want** to log in using a username and password, **so that** I can manage the platform securely.  

**Acceptance Criteria:**
1. Admin can log in using valid credentials.  
2. Invalid credentials return an appropriate error message.  
3. Successful login redirects to the Admin Dashboard.  

**Priority:** Medium  
**Story Points:** 3  
**Notes:** Uses Spring Security authentication flow.

---

### 2. Log Out of Platform
**As an** ADMIN, **I want** to log out of the portal, **so that** unauthorized users can’t access the system.  

**Acceptance Criteria:**
1. Admin can log out successfully.  
2. Session is invalidated after logout.  

**Priority:** Medium  
**Story Points:** 1  

---

### 3. Add Doctors
**As an** ADMIN, **I want** to add new doctors to the system, **so that** patients can book appointments with them.  

**Acceptance Criteria:**
1. Admin can add a doctor profile with name, specialization, and contact info.  
2. Validation ensures required fields are present.  

**Priority:** Low  
**Story Points:** 3  

---

### 4. Delete Doctors
**As an** ADMIN, **I want** to delete doctor profiles from the system, **so that** inactive or unavailable doctors are removed from booking options.  

**Acceptance Criteria:**
1. Admin can remove doctor profiles.  
2. Deleting a doctor prevents new appointments from being scheduled with them.  

**Priority:** Low  
**Story Points:** 3  

---

### 5. Generate Usage Reports
**As an** ADMIN, **I want** to run a stored procedure in MySQL, **so that** I can view appointment counts per month and track platform usage.  

**Acceptance Criteria:**
1. Stored procedure returns monthly appointment counts.  
2. Results display clearly in the Admin Dashboard.  

**Priority:** Low  
**Story Points:** 3  

---

## 🩺 Patient User Stories

### 1. View List of Doctors
**As a** PATIENT, **I want** to view a list of doctors without logging in, **so that** I can explore options before registering.  

**Acceptance Criteria:**
1. List of doctors is publicly viewable.  
2. Doctor name, specialization, and availability are displayed.  

**Priority:** Medium  
**Story Points:** 3  

---

### 2. Sign Up
**As a** PATIENT, **I want** to register using my email and password, **so that** I can book appointments.  

**Acceptance Criteria:**
1. User can create an account.  
2. Email and password validation is enforced.  
3. Passwords are hashed before storage.  

**Priority:** Medium  
**Story Points:** 3  

---

### 3. Log In
**As a** PATIENT, **I want** to log into the portal, **so that** I can manage my appointments.  

**Acceptance Criteria:**
1. User can log in using registered credentials.  
2. Invalid credentials display an error message.  

**Priority:** Medium  
**Story Points:** 3  

---

### 4. Log Out
**As a** PATIENT, **I want** to log out of the portal, **so that** my account remains secure.  

**Acceptance Criteria:**
1. User can log out successfully.  
2. Session is terminated.  

**Priority:** Medium  
**Story Points:** 1  

---

### 5. Book Appointment
**As a** PATIENT, **I want** to log in and book an hour-long appointment, **so that** I can consult with a doctor.  

**Acceptance Criteria:**
1. User must be logged in to book.  
2. Appointment duration defaults to one hour.  
3. Confirmation message is displayed upon success.  

**Priority:** Medium  
**Story Points:** 3  

---

### 6. View Upcoming Appointments
**As a** PATIENT, **I want** to view my upcoming appointments, **so that** I can plan accordingly.  

**Acceptance Criteria:**
1. Upcoming appointments are listed with date, time, and doctor info.  

**Priority:** Medium  
**Story Points:** 3  

---

## 🩺 Doctor User Stories

### 1. Log In
**As a** DOCTOR, **I want** to log into the portal, **so that** I can manage my appointments.  

**Acceptance Criteria:**
1. Doctor can log in using valid credentials.  

**Priority:** Medium  
**Story Points:** 3  

---

### 2. Log Out
**As a** DOCTOR, **I want** to log out of the portal, **so that** my session remains secure.  

**Acceptance Criteria:**
1. Logout terminates session.  

**Priority:** Medium  
**Story Points:** 1  

---

### 3. View Appointment Calendar
**As a** DOCTOR, **I want** to view my appointment calendar, **so that** I can stay organized.  

**Acceptance Criteria:**
1. Calendar shows all upcoming appointments by date/time.  

**Priority:** Medium  
**Story Points:** 3  

---

### 4. Mark Unavailability
**As a** DOCTOR, **I want** to mark my unavailability, **so that** patients can only book available slots.  

**Acceptance Criteria:**
1. Doctor can specify unavailable time slots.  
2. System prevents bookings during those times.  

**Priority:** Medium  
**Story Points:** 3  

---

### 5. Update Profile
**As a** DOCTOR, **I want** to update my specialization and contact information, **so that** patients have accurate details.  

**Acceptance Criteria:**
1. Doctor can update specialization and contact info.  
2. Changes are reflected in the patient view.  

**Priority:** Medium  
**Story Points:** 3  

---

### 6. View Patient Details
**As a** DOCTOR, **I want** to view patient details for upcoming appointments, **so that** I can prepare in advance.  

**Acceptance Criteria:**
1. Doctor can view patient name, contact info, and reason for visit.  

**Priority:** Medium  
**Story Points:** 3  
