# Schema Design

## MySQL Database Design

### Tables
1. `patients`
2. `doctors`
3. `appointments`
4. `admin`
5. `payments`

---

### **Table: patients**
| Column | Type | Constraints | Description |
|---------|------|-------------|--------------|
| id | INT | Primary Key, Auto Increment | Unique identifier for patient |
| first_name | VARCHAR(255) | NOT NULL | Patient’s first name |
| last_name | VARCHAR(255) | NOT NULL | Patient’s last name |
| date_of_birth | DATE | NOT NULL | Date of birth |
| user_name | VARCHAR(255) | NOT NULL | Login username |
| password_hash | VARCHAR(255) | NOT NULL | Hashed password (bcrypt or Argon2) |

---

### **Table: doctors**
| Column | Type | Constraints | Description |
|---------|------|-------------|--------------|
| id | INT | Primary Key, Auto Increment | Unique identifier for doctor |
| first_name | VARCHAR(255) | NOT NULL | Doctor’s first name |
| last_name | VARCHAR(255) | NOT NULL | Doctor’s last name |
| user_name | VARCHAR(255) | NOT NULL | Login username |
| password_hash | VARCHAR(255) | NOT NULL | Hashed password |
| specialization | VARCHAR(255) | NULL | Doctor’s area of specialization |
| contact_email | VARCHAR(255) | NULL | Contact details (email) |
| contact_phone | VARCHAR(255) | NULL | Contact details (phone) |

---

### **Table: appointments**
| Column | Type | Constraints | Description |
|---------|------|-------------|--------------|
| id | INT | Primary Key, Auto Increment | Unique identifier for appointment |
| doctor_id | INT | Foreign Key → `doctors(id)` | Linked doctor |
| patient_id | INT | Foreign Key → `patients(id)` | Linked patient |
| appointment_time | DATETIME | NOT NULL | Scheduled time |
| status | INT | DEFAULT 0 | 0 = Scheduled, 1 = Completed, 2 = Cancelled |

---

### **Table: admin**
| Column | Type | Constraints | Description |
|---------|------|-------------|--------------|
| id | INT | Primary Key, Auto Increment | Unique identifier for admin user |
| user_name | VARCHAR(255) | NOT NULL | Admin login username |
| password_hash | VARCHAR(255) | NOT NULL | Hashed password |

---

### **Table: payments**
| Column | Type | Constraints | Description |
|---------|------|-------------|--------------|
| id | INT | Primary Key, Auto Increment | Unique identifier for payment record |
| patient_id | INT | Foreign Key → `patients(id)` | Associated patient |
| appointment_id | INT | Foreign Key → `appointments(id)` | Associated appointment |
| amount_due | DECIMAL(10,2) | NOT NULL | Total payment due |
| amount_paid | DECIMAL(10,2) | NOT NULL | Amount received |

---

## MongoDB Collection Design

### Collections
1. `prescriptions`
2. `feedback`
3. `messages`

---

### **Collection: prescriptions**
```json
{
  "_id": "ObjectId('64abc123456')",
  "patientId": 1,
  "appointmentId": 51,
  "medication": "Paracetamol",
  "dosage": "500mg",
  "doctorNotes": "Take 1 tablet every 6 hours.",
  "refillCount": 2,
  "pharmacy": {
    "name": "Walgreens SF",
    "location": "Market Street"
  }
}
```

---

### **Collection: feedback**
```json
{
  "_id": "ObjectId('64abc123456')",
  "patientId": 1,
  "rating": 5,
  "comment": "Great service",
  "date": "2025-12-01"
}
```

---

### **Collection: messages**
```json
{
  "_id": "ObjectId('64abc123456')",
  "patientId": 1,
  "doctorId": 7,
  "conversation": [
    {
      "from": "patient",
      "text": "Thanks",
      "datetime": "2025-01-15T15:06:00Z"
    },
    {
      "from": "doctor",
      "text": "No worries",
      "datetime": "2025-01-15T15:07:00Z"
    }
  ]
}
```


