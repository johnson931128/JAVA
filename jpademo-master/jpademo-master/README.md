# System Requirements:
- JDK 17 or later  
- Maven 3.4.5

# SQL Query
```sql

/* Search Data */
SELECT `clients`.`id`,
    `clients`.`address`,
    `clients`.`created_at`,
    `clients`.`email`,
    `clients`.`first_name`,
    `clients`.`last_name`,
    `clients`.`phone`,
    `clients`.`status`
FROM `jpademo`.`clients`;

/* Insert Sample Data */
INSERT INTO clients (first_name, last_name, email, phone, address, status, created_at) VALUES
('Simon', 'Walsh', 'simon.walsh@outlook.com', '1-785-387-9840', 'Jonesboro, USA', 'Occasional', CURRENT_TIMESTAMP),
('Harry', 'Russell', 'harry.russell@gmail.com', '1-566-905-1747', 'Florida, USA', 'Permanent', CURRENT_TIMESTAMP),
('Angela', 'Knox', 'angela.knox@yahoo.com', '1-585-897-0754', 'Grand Rapids, USA', 'Inactive', CURRENT_TIMESTAMP),
('Maria', 'Paterson', 'maria.paterson@gmail.com', '1-339-932-9342', 'Frederick, USA', 'Lead', CURRENT_TIMESTAMP),
('Vanessa', 'Lambert', 'vanessa.lambert@gmail.com', '1-441-498-4553', 'New York, USA', 'Occasional', CURRENT_TIMESTAMP),
('Chloe', 'Mills', 'chloe.mills@gmail.com', '1-785-387-9835', 'Grand Rapids, USA', 'New', CURRENT_TIMESTAMP),
('Andrea', 'May', 'andrea.may@outlook.com', '1-566-905-1765', 'Frederick, USA', 'Lead', CURRENT_TIMESTAMP),
('Sam', 'Grant', 'sam.grant@gmail.com', '1-585-897-0773', 'New York, USA', 'Occasional', CURRENT_TIMESTAMP),
('Jan', 'Lambert', 'jan.lambert@yahoo.com', '1-339-932-9375', 'Rochester, USA', 'Permanent', CURRENT_TIMESTAMP),
('Peter', 'Harris', 'peter.harris@gmail.com', '1-441-498-4573', 'Indianapolis, USA', 'Lead', CURRENT_TIMESTAMP),
('Frank', 'Hodges', 'frank.hodges@outlook.com', '1-785-387-9880', 'Rochester, USA', 'Permanent', CURRENT_TIMESTAMP),
('Ava', 'Wallace', 'ava.wallace@gmail.com', '1-566-905-1715', 'Indianapolis, USA', 'Lead', CURRENT_TIMESTAMP),
('Neil', 'Wallace', 'neil.wallace@yahoo.com', '1-585-897-0746', 'Jonesboro, USA', 'Inactive', CURRENT_TIMESTAMP),
('Sebastian', 'Grant', 'sebastian.grant@outlook.com', '1-339-932-9323', 'Florida, USA', 'Permanent', CURRENT_TIMESTAMP);

/* Delete All Data */
DELETE FROM clients WHERE id > 0;
```
