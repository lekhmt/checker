CREATE TABLE if NOT EXISTS users (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    full_name varchar(64) NOT NULL,
    email varchar(64) NOT NULL UNIQUE,
    password varchar(64) NOT NULL,
    role varchar(16) NOT NULL
);

CREATE TABLE if NOT EXISTS student_groups (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(64) NOT NULL UNIQUE,
    course int NOT NULL,
    institute int NOT NULL
);

CREATE TABLE if NOT EXISTS students (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id int NOT NULL UNIQUE REFERENCES users(id),
    group_id int NOT NULL REFERENCES student_groups(id)
);

CREATE TABLE if NOT EXISTS curators (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id int NOT NULL UNIQUE REFERENCES users(id)
);

CREATE TABLE if NOT EXISTS curator_student_group_mapping (
    curator_id int NOT NULL REFERENCES curators(id),
    student_group_id int NOT NULL REFERENCES student_groups(id)
);
