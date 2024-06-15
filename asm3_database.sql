
create database asm3;
use asm3;
select * from hospital;
select * from department;
select * from doctor;
select * from appointment;
select * from user;
select * from extrainfos;
select * from account_lock_information;
select * from examination_result;


select d.* from doctor d join user u on d.user_id = u.id where u.email="an@gmail";

create table user(
`id` int(11) not null auto_increment,
`name` varchar(250),
`email` varchar(250),
`password` varchar(250),
`address` varchar(250),
`phone` varchar(250),
`avatar` varchar(250),
`gender` varchar(250),
`description` text,
`role` varchar(250),
`is_active` int,
`create_at` datetime,
`update_at` datetime,
`delete_at` datetime,
primary key(id),
 unique key `only` (`email`)
);
create table `hospital`(
`id` int(11) not null auto_increment,
`name` varchar(250),
`address` varchar(250),
`phone` varchar(250),
`description` text,
`status` int,
primary key(id)
);
create table `department`(
`id` int(11) not null auto_increment,
`name` varchar(250),
`phone` varchar(250),
`description` text,
`status` int,
`hospital_id` int(11),
primary key(id),
foreign key(hospital_id) references hospital(id)
);
create table `doctor`(
`id` int(11) not null auto_increment,
`specialization` varchar(250),
`user_id` int(11),
`department_id` int (11),
primary key(id),
foreign key(user_id) references user(id),
foreign key(department_id) references department(id)
);

create table `appointment`(
`id` int(11) not null auto_increment,
`doctor_id` int (11),
`user_id` int(11),
`email_doctor` varchar(250),
`title` varchar(250),
`description` text,
`appointment_date` date,
`registration_time` varchar(250),
`create_at` datetime,
`delete_at` datetime,
`status` int,
primary key(id),
foreign key(user_id) references user(id),
foreign key(doctor_id) references doctor(id)
);

create table `extrainfos`(
`id` int(11) not null auto_increment,
`appointment_id` int(11),
`doctor_id` int(11),
`description` text,
`status` int,
primary key(id),
foreign key(doctor_id) references doctor(id),
foreign key(appointment_id) references appointment(id)
);
create table `account_lock_information`(
`id` int(11) not null auto_increment,
`email` varchar(250),
`description` text,
`create_at` datetime,
primary key(id)
);
create table `examination_result`(
`id` int(11) not null auto_increment,
`file_name` varchar(250),
`email_doctor` varchar(250),
`user_id` int(11),
`time` datetime,
primary key(id),
foreign key(user_id) references user(id)
);