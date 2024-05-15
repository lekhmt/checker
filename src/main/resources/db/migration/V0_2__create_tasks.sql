CREATE TABLE if NOT EXISTS subjects (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(128) NOT NULL,
    description varchar(2018)
);

CREATE TABLE if NOT EXISTS subject_curator_mapping (
    subject_id int NOT NULL REFERENCES subjects(id),
    curator_id int NOT NULL REFERENCES curators(id)
);

CREATE TABLE if NOT EXISTS subject_student_group_mapping (
    subject_id int NOT NULL REFERENCES subjects(id),
    student_group_id int NOT NULL REFERENCES student_groups(id)
);

CREATE TABLE if NOT EXISTS tasks (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    display_name varchar(256) NOT NULL,
    goal varchar(256),
    description varchar(4096) NOT NULL,
    input_description varchar(4096),
    output_description varchar(4096),
    time_limit int,
    memory_limit int,
    visible_from timestamp NOT NULL,
    due_to timestamp,
    subject_id int NOT NULL REFERENCES subjects(id)
);

CREATE TABLE if NOT EXISTS tests (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    input bytea,
    output bytea,
    task_id int NOT NULL REFERENCES tasks(id)
);

CREATE TABLE if NOT EXISTS submits (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    task_id int NOT NULL REFERENCES tasks(id),
    student_id int NOT NULL REFERENCES students(id),
    submitted_at timestamp,
    code bytea,
    is_single_file boolean,
    submit_status varchar(32),
    compiler varchar(16),
    message text
);