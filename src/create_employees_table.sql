USE moviedb;

CREATE TABLE if not exists employees(
    email VARCHAR(50),
    password VARCHAR(20) not null,
    fullname VARCHAR(100),
    PRIMARY KEY(email)
    );