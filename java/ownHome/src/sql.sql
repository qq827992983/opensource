create database if not exists ownhome;

create table if not exists log(id int primary key auto_increment, msg varchar(256));

create table if not exists users(userId int primary key auto_increment,userName varchar(100),password varchar(100) ,realName varchar(100),sex varchar(10));

create table if not exists type(typeId int primary key auto_increment, type varchar(100));

create table if not exists article(articleId int primary key,title varchar(100), content varchar(4096), writer varchar(100), writeDate DateTime, typeId int, foreign key(typeId) references type(typeId));

create table if not exists book(id int primary key auto_increment,name varchar(100),sex varchar(20),phone varchar(20), address varchar(100), mobilePhone varchar(100), company varchar(100), comPhone varchar(100), comAddress varchar(100), relation varchar(100), userId int, foreign key(userId) references users(userId));
