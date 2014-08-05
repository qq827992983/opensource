ownhome sql
log:
create table log(id int primary key, msg varchar(256) );

users:
create table users(userId int primary key,userName varchar(100),password varchar(100) ,realName varchar(100),sex bool);

article:
create table article(articleId int primary key,title varchar(100),int type, content varchar(4096), writer varchar(100) writeDate DateTime);

type:
create table type(typeId int primary key, type varchar(100));


