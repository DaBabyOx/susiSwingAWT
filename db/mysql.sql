create database binusmaya;

use binusmaya;

create table student(
    fullName varchar(255) not null,
    email varchar(255) not null check (email like '%@%'),
    birthdate date not null check (timestampdiff(year, birthdate, curdate())>=16), -- you could also use the datediff function if you want [datediff(curdate(), birthdate)>= (16*365)
    gender varchar(6) not null,
    cohort varchar(3) not null,
    password varchar(255) not null,
    nim varchar(255) not null check (nim not like '%[a-Z]%'));
