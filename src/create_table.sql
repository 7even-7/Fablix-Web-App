create database if not exists moviedb;
use moviedb;
CREATE TABLE if not exists movies(
    id VARCHAR(10) DEFAULT '' not null,
    title VARCHAR(100) DEFAULT '' not null,
    year INTEGER NOT NULL,
    director VARCHAR(100) DEFAULT '' not null,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS stars(
    id varchar(10) primary key not null,
    name varchar(100) not null,
    birthYear integer
    );


CREATE TABLE IF NOT EXISTS stars_in_movies(
    starId VARCHAR(10) DEFAULT '',
    movieId VARCHAR(10) DEFAULT '',
    FOREIGN KEY (starId) REFERENCES stars(id),
    FOREIGN KEY (movieId) REFERENCES movies(id)
    );


CREATE TABLE IF NOT EXISTS genres(
        id integer primary key not null auto_increment,
        name varchar(32)
    );

CREATE TABLE IF NOT EXISTS genres_in_movies(
    genreId integer not null,
    movieId varchar(10) default '' not null,
    foreign key(genreId) references genres(id),
    foreign key(movieId) references movies(id)
    );

CREATE TABLE IF NOT EXISTS creditcards(
    id varchar(20) not null primary key,
    firstName varchar(50) not null,
    lastName varchar(50) not null,
    expiration date not null
    );

CREATE TABLE IF NOT EXISTS customers(
    id integer primary key not null auto_increment,
    firstName varchar(50) not null,
    lastName varchar(50) not null,
    ccId varchar(20) not null,
    foreign key(ccId) references creditcards(id),
    address varchar(200) not null,
    email varchar(50) not null,
    password varchar(20) not null
    );


CREATE TABLE IF NOT EXISTS sales(
    id integer primary key not null auto_increment,
    customerId integer not null,
    foreign key (customerId) references customers(id),
    movieId varchar(10) not null,
    foreign key (movieId) references movies(id),
    saleDate date not null
    );

CREATE TABLE IF NOT EXISTS ratings (
    movieId varchar(10) not null,
    foreign key(movieId) references movies(id),
    rating float not null,
    numVotes integer not null
    );