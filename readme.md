for presentation >>>>>> https://chat.deepseek.com/share/v7eljn6pu55ip6sryj

for database >>>>>

password to all users>>>> 0NlwViEz

Method 1: Quick Way (Most Common)
Open pgAdmin and connect to your server

Expand your server (click the dropdown arrow)

Expand "Databases"

Right-click on your database name (e.g., "grievance_portal" or whatever you named it)

Select "Query Tool" from the menu





-- Insert all users with the old password
INSERT INTO users (username, password, email, full_name, role, department, active, created_at) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'admin@university.edu', 'System Administrator', 'ADMIN', NULL, true, NOW()),
('hod_admissions', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'hod.admissions@university.edu', 'Dr. Sarah Johnson', 'HOD', 'Admissions', true, NOW()),
('hod_academics', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'hod.academics@university.edu', 'Dr. Michael Chen', 'HOD', 'Academics', true, NOW()),
('hod_finance', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'hod.finance@university.edu', 'Dr. Robert Williams', 'HOD', 'Finance', true, NOW()),
('hod_srs', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'hod.srs@university.edu', 'Dr. Emily Davis', 'HOD', 'SRS', true, NOW()),
('hod_transport', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'hod.transport@university.edu', 'Mr. James Wilson', 'HOD', 'Transport', true, NOW()),
('hod_research', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'hod.research@university.edu', 'Dr. Lisa Anderson', 'HOD', 'Research', true, NOW()),
('hod_hostel', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'hod.hostel@university.edu', 'Ms. Patricia Martinez', 'HOD', 'Hostel', true, NOW()),
('hod_library', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'hod.library@university.edu', 'Dr. David Brown', 'HOD', 'Library', true, NOW()),
('hod_examination', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'hod.exam@university.edu', 'Dr. Jennifer Taylor', 'HOD', 'Examination', true, NOW()),
('hod_it', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'hod.it@university.edu', 'Mr. Thomas Clark', 'HOD', 'IT Services', true, NOW()),
('staff_admissions1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'staff1.admissions@university.edu', 'Alice Johnson', 'STAFF', 'Admissions', true, NOW()),
('staff_admissions2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'staff2.admissions@university.edu', 'Bob Williams', 'STAFF', 'Admissions', true, NOW()),
('staff_academics1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'staff1.academics@university.edu', 'Carol Martinez', 'STAFF', 'Academics', true, NOW()),
('staff_academics2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'staff2.academics@university.edu', 'David Lee', 'STAFF', 'Academics', true, NOW()),
('staff_finance1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'staff1.finance@university.edu', 'Emma Davis', 'STAFF', 'Finance', true, NOW()),
('staff_finance2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'staff2.finance@university.edu', 'Frank Miller', 'STAFF', 'Finance', true, NOW()),
('staff_srs1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'staff1.srs@university.edu', 'Grace Wilson', 'STAFF', 'SRS', true, NOW()),
('staff_srs2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'staff2.srs@university.edu', 'Henry Moore', 'STAFF', 'SRS', true, NOW()),
('staff_transport1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'staff1.transport@university.edu', 'Ivy Taylor', 'STAFF', 'Transport', true, NOW()),
('staff_transport2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'staff2.transport@university.edu', 'Jack Anderson', 'STAFF', 'Transport', true, NOW()),
('staff_research1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'staff1.research@university.edu', 'Karen Thomas', 'STAFF', 'Research', true, NOW()),
('staff_research2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'staff2.research@university.edu', 'Larry Jackson', 'STAFF', 'Research', true, NOW()),
('staff_hostel1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'staff1.hostel@university.edu', 'Maria Garcia', 'STAFF', 'Hostel', true, NOW()),
('staff_hostel2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'staff2.hostel@university.edu', 'Nathan White', 'STAFF', 'Hostel', true, NOW()),
('staff_library1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'staff1.library@university.edu', 'Olivia Harris', 'STAFF', 'Library', true, NOW()),
('staff_library2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'staff2.library@university.edu', 'Paul Martin', 'STAFF', 'Library', true, NOW()),
('staff_examination1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'staff1.exam@university.edu', 'Quinn Robinson', 'STAFF', 'Examination', true, NOW()),
('staff_examination2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'staff2.exam@university.edu', 'Rachel Clark', 'STAFF', 'Examination', true, NOW()),
('staff_it1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'staff1.it@university.edu', 'Steven Rodriguez', 'STAFF', 'IT Services', true, NOW()),
('staff_it2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU0J4.Sq', 'staff2.it@university.edu', 'Tina Lewis', 'STAFF', 'IT Services', true, NOW());

-- Update all passwords to the new one
UPDATE users SET password = '$2a$10$OOIztNk0VyXCbM2iPAT9ruiGZw57jhSCBR.PCSfDjlkjHehP0Rox.' WHERE active = true;