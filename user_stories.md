# User Story Template

**Title:**
_As a [user role], I want [feature/goal], so that [reason]._

**Acceptance Criteria:**
1. [Criteria 1]
2. [Criteria 2]
3. [Criteria 3]

**Priority:** [High/Medium/Low]
**Story Points:** [Estimated Effort in Points]
**Notes:**
- [Additional information or edge cases]

## Admin User Stories

**Title: Manage Platform**
_As an ADMIN, I want login in to a portal using username and password, so that I can manage the platform securely._

**Acceptance Criteria:**
1. ADMIN can log in using valid username and password
2. Invalid credentials show an appropriate error message.
3. Successful login redirects to the Admin Dashboard.

**Priority:** [Medium]
**Story Points:** 3
**Notes:**
- Uses Spring Security authentication flow.


**Title: Log out of platform**
_As an ADMIN, I want log out of the portal, so that the system is protected from access._

**Acceptance Criteria:**
1. Enable log out

**Priority:** [Medium]
**Story Points:** 1
**Notes:**
- [Additional information or edge cases]


**Title: Add doctors in portal**
_As an ADMIN, I want add doctors to the portal, so that appointments can be booked with new doctor._

**Acceptance Criteria:**
1. Allow addin new doctors


**Priority:** [Low]
**Story Points:** 3
**Notes:**
- [Additional information or edge cases]


**Title: Delete doctors from portal**
_As an ADMIN, I want delete doctors prfile from the portal, so that no appointments can be booked with that doctor._

**Acceptance Criteria:**
1. Allow removal of doctor profiles


**Priority:** [Low]
**Story Points:** 3
**Notes:**
- [Additional information or edge cases]


**Title: Report - User statitsics**
_As an ADMIN, I want Run a stored procedure in MySQL CLI, so that I can get the number of appointments per month and track usage statistics._

**Acceptance Criteria:**
1. Create a stored procedure in MySQL that returns stats as specified above


**Priority:** Low
**Story Points:** 3
**Notes:**
- [Additional information or edge cases]


## Patient User Stories

**Title: View list of doctors**
_As a PATIENT, I want view a list of doctors without logging in, so that I can explore options before registering._

**Acceptance Criteria:**
1. So a list of doctors
2. Does not need to be logged in


**Priority:** [Medium]
**Story Points:** 3
**Notes:**
- [Additional information or edge cases]


**Title: Sign up to portal**
_As a PATIENT, I want Sign up using your email and password, so that I can book appointments._

**Acceptance Criteria:**
1. User can sign up

**Priority:** [Medium]
**Story Points:** 3
**Notes:**
- [Additional information or edge cases]


**Title: Log into portal**
_As a PATIENT, I want Log into the portal, so that so I can manage your bookings._

**Acceptance Criteria:**
1. User can log into portal


**Priority:** [Medium]
**Story Points:** 3
**Notes:**
- [Additional information or edge cases]


**Title: Log out of portal**
_As a PATIENT, I want Log out of the portal, so that I can secure my account._

**Acceptance Criteria:**
1. user can log out

**Priority:** [Medium]
**Story Points:** 3
**Notes:**
- [Additional information or edge cases]


**Title: Book an appointment**
_As a PATIENT, I want Log in and book an hour-long appointment, so that so I can consult with a doctor._

**Acceptance Criteria:**
1. Log into portal
2. Book an appointment


**Priority:** [Medium]
**Story Points:** 3
**Notes:**
- [Additional information or edge cases]


**Title: View upcoming appointments**
_As a PATIENT, I want View my upcoming appointments, so that I can prepare accordingly._

**Acceptance Criteria:**
1. User can view my upcoming appointments


**Priority:** [Medium]
**Story Points:** 3
**Notes:**
- [Additional information or edge cases]


## Doctor User Stories

**Title: Log into portal**
_As a DOCTOR, I want Log into the portal, so that I can manage your appointments._

**Acceptance Criteria:**
1. Can log into portal

**Priority:** [Medium]
**Story Points:** 3
**Notes:**
- [Additional information or edge cases]


**Title: Log out of portal**
_As a DOCTOR, I want Log out of the portal, so that I can protect my data._

**Acceptance Criteria:**
1. Logout of portal


**Priority:** [Medium]
**Story Points:** 3
**Notes:**
- [Additional information or edge cases]


**Title: View my appointment calendar**
_As a DOCTOR, I want View my appointment calendar, so that I can stay organized._

**Acceptance Criteria:**
1. View my appointment calendar


**Priority:** [Medium]
**Story Points:** 3
**Notes:**
- [Additional information or edge cases]


**Title: Mark my unavailability**
_As a DOCTOR, I want Mark my unavailability, so that to inform patients only the available slots._

**Acceptance Criteria:**
1. Mark my unavailability


**Priority:** [Medium]
**Story Points:** 3
**Notes:**
- [Additional information or edge cases]


**Title: Update your profile with specialization and contact information**
_As a DOCTOR, I want update your profile with specialization and contact information, so that patients have up-to-date information._

**Acceptance Criteria:**
1. update your profile with specialization
2. update your profile with contact info


**Priority:** [Medium]
**Story Points:** 3
**Notes:**
- [Additional information or edge cases]


**Title: View the patient details for upcoming appointments**
_As a DOCTOR, I want View the patient details for upcoming appointments, so that so that I can be prepared._

**Acceptance Criteria:**
1. View the patient details for upcoming appointments

**Priority:** [Medium]
**Story Points:** 3
**Notes:**
- [Additional information or edge cases]