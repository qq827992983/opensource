create database if not exists ownhome;

create table if not exists log(id int primary key auto_increment, msg varchar(256));

create table if not exists users(userId int primary key auto_increment,userName varchar(100),password varchar(100) ,realName varchar(100),sex varchar(10));

create table if not exists type(typeId int primary key auto_increment, type varchar(100));

create table if not exists article(articleId int primary key,title varchar(100), content varchar(4096), writer varchar(100), writeDate DateTime, typeId int, foreign key(typeId) references type(typeId));

create table if not exists book(int id primary key auto_increment,


name,sex,id,phone,address,mobilePhone,company,comPhone,comAddress,relation,userId(foreign key)