ownhome sql
log:
create table log(id int primary key, msg varchar(256) );

users:
create table users(userId int primary key auto_increment,userName varchar(100),password varchar(100) ,realName varchar(100),sex varchar(10));

article:
create table article(articleId int primary key,title varchar(100),int type, content varchar(4096), writer varchar(100) writeDate DateTime);

type:
create table type(typeId int primary key, type varchar(100));


create table emb_t_dictBusType
(
   emb_c_busTypeID      int not null auto_increment,
   emb_c_busTypeEnName  varchar(255) not null,
   emb_c_busTypeZhName  varchar(255) not null,
   primary key(emb_c_busTypeID)  
)engine=INNODB  default charset=gbk;