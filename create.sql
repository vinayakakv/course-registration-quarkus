create sequence hibernate_sequence start 1 increment 1;
create table Course (id varchar(255) not null, name varchar(255), primary key (id));
create table Enrollment (student_id varchar(255) not null, course_id varchar(255) not null, primary key (student_id, course_id));
create table Student (id varchar(255) not null, email varchar(255), name varchar(255), primary key (id));
create table users (id int8 not null, password varchar(255), role varchar(255), userName varchar(255), primary key (id));
alter table if exists Enrollment add constraint FKh9495fmmc7x1heiww4cus16kc foreign key (course_id) references Course;
alter table if exists Enrollment add constraint FKosl3nkqhbp2hx32nn652hlun2 foreign key (student_id) references Student;
INSERT INTO users(id, username, password, role) VALUES (0, 'admin', 'admin', 'admin');
