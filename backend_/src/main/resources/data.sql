INSERT INTO employees (name, email, manager_id)
VALUES ('Admin Manager', 'admin.manager@example.com', NULL);

INSERT INTO users (username, password_hash, employee_id)
SELECT 'admin', 'admin123', employee_id
FROM employees WHERE email = 'admin.manager@example.com';
