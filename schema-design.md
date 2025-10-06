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
| name | VARCHAR(100) | NOT NULL | Patient’s name |
| email | VARCHAR(255) | NULL | Contact details (email) |
| password | VARCHAR(255) | NOT NULL | Hashed password |
| phone | VARCHAR(10) | NOT NULL | Contact details (phone) |
| address | VARCHAR(255) | NOT NULL | Patirnts address |

---

### **Table: doctors**
| Column | Type | Constraints | Description |
|---------|------|-------------|--------------|
| id | INT | Primary Key, Auto Increment | Unique identifier for doctor |
| name | VARCHAR(100) | NOT NULL | Doctor’s name |
| specialty | VARCHAR(255) | NOT NULL | Doctor’s area of specialization |
| email | VARCHAR(255) | NOT NULL | Contact details (email) |
| password | VARCHAR(255) | NOT NULL | Hashed password |
| phone | VARCHAR(10) | NOT NULL | Contact details (phone) |
| availableTimes | List<String> | NULL | Doctors availability |

---

### **Table: appointments**
| Column | Type | Constraints | Description |
|---------|------|-------------|--------------|
| id | INT | Primary Key, Auto Increment | Unique identifier for appointment |
| doctor | INT | Foreign Key → `doctors(id)` | Linked doctor |
| patientId | INT | Foreign Key → `patients(id)` | Linked patient |
| appointmentTime | DATETIME | NOT NULL | Scheduled time |
| status | INT | DEFAULT 0 | 0 = Scheduled, 1 = Completed, 2 = Cancelled |

---

### **Table: admin**
| Column | Type | Constraints | Description |
|---------|------|-------------|--------------|
| id | INT | Primary Key, Auto Increment | Unique identifier for admin user |
| username | VARCHAR(255) | NOT NULL | Admin login username |
| password | VARCHAR(255) | NOT NULL | Hashed password |

---

### **Table: payments**
| Column | Type | Constraints | Description |
|---------|------|-------------|--------------|
| id | INT | Primary Key, Auto Increment | Unique identifier for payment record |
| patientId | INT | Foreign Key → `patients(id)` | Associated patient |
| appointmentId | INT | Foreign Key → `appointments(id)` | Associated appointment |
| amountDue | DECIMAL(10,2) | NOT NULL | Total payment due |
| amountPaid | DECIMAL(10,2) | NOT NULL | Amount received |

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
  "patientName": "John Doe",
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


