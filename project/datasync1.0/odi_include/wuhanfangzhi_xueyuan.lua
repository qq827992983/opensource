----------------------------------------
-- lua script to load ODI data in to mysql database
-- author: quanli
-- date:   2014-01-17
--
----------------------------------------
luasql = require "luasql.mysql"
local iconv = require("iconv")
cd = iconv.new("utf8", "gbk")

-----------------------------
--include json openfire lib
-----------------------------
package.path = '/usr/local/whistle/openfire/resources/auth/lua/odi_include/?.lua'
json = require('json')

--odi_wuhan.log
odi_interface_log_path = "/usr/local/whistle/openfire/resources/auth/lua/log/odi_wuhan.log"
os.remove(odi_interface_log_path)

--get_insertdata.txt
get_insertdata_path = "/usr/local/whistle/openfire/resources/auth/lua/get_insertdata.txt"

--get_deletedata.txt
get_deletedata_path = "/usr/local/whistle/openfire/resources/auth/lua/get_deletedata.txt"

--get_updatedata.txt
get_updatedata_path = "/usr/local/whistle/openfire/resources/auth/lua/get_updatedata.txt"

-----------------------------
--lua write log function
-----------------------------
function log_write(log_str)
	local tab = os.date("*t")
	local lua_log_time = string.format("["..tab.year.."-"..tab.month.."-"..tab.day.." "..tab.hour..":"..tab.min..":"..tab.sec.."]".."lua_print:	")
	local log_file = io.open(odi_interface_log_path,"a")
	log_file:write(lua_log_time..log_str.."\n")
	log_file:close()
end


------------------------------ mysql server config here -----------------------------------------
local host = "192.168.254.100"
local port = 3306
local user = "odi"
local password = "odiodi"
local database = "odi_data_sync"

function getvalue(value)
if value then
local ret, error = cd:iconv(value)
if error then
   return ""
   else
   return ret
end
else
return ""
end
end

function getXB(value)
local xb = getvalue(value)
if xb == "1" then
    return "boy"
elseif xb == "2" then
    return "girl"
else
    return xb
end

end

function getZJLB(value)
    return "id"
end

function getSF(value)
local xb = getvalue(value)

if xb == "学生" then
    return "student"
elseif xb == "教职工" then
    return "teacher"
else
    return xb
end
end

----------------------------------------
-- return value true/false
-- prepare the work table and prework table
-- the previously data in work table will be backuped to prework table
-- if no data exist in user_data table return false
----------------------------------------
function preparedata()
local env = assert (luasql.mysql())
local con = assert (env:connect(database, user, password, host, port))
assert(con:execute"set names gbk")

print(string.format("preparedata:----->1.Conneted database %s@%s:%s", database, host, port))
log_write(string.format("preparedata:----->1.Conneted database "..database..host..port))

-- query data from database
local cur = assert(con:execute"SELECT ora_uid, card_type, card_number, identity, name, sex, birthday, blood_type, nativeplace_nation, nativeplace_province, nativeplace_city, nativeplace_district, address_nation, address_province, address_city, address_district, address_extend, address_postcode, email, landline, cellphone, student_number, 1st_orgnizationid as oid1, 1st_orgnizationname as oname1, 2nd_orgnizationid as oid2, 2nd_orgnizationname as oname2, 3rd_orgnizationid as oid3, 3rd_orgnizationname as oname3, 4th_orgnizationid as oid4, 4th_orgnizationname as oname4, 5th_orgnizationid as oid5, 5th_orgnizationname as oname5, title from user_data")

local row = cur:fetch({},"a")
if row then
   print(string.format("preparedata:----->2.Data exist in table user_data"))
	log_write(string.format("preparedata:----->2.Data exist in table user_data"))
   -- drop prework table
   print(string.format("preparedata:----->3.Drop prework table"))
   log_write(string.format("preparedata:----->3.Drop prework table"))
   assert(con:execute"DROP TABLE IF EXISTS prework")
   -- drop prework table

   -- rename work table to prework table
   local cur_table = assert(con:execute"SHOW TABLES LIKE \'work\'")
   local row_table = cur_table:fetch({},"a")
   if row_table then
        print(string.format("INFO: work table exist, backup it to prework table"))
		log_write(string.format("INFO: work table exist, backup it to prework table"))
	assert(con:execute"RENAME TABLE work to prework")
   else
        print(string.format("INFO: work table not exist"))
		log_write(string.format("INFO: work table not exist"))
	end
   cur_table:close()
   -- rename work table to pre-work table

   -- generate work table
   print(string.format("preparedata:----->4.Create work table"))
   log_write(string.format("preparedata:----->4.Create work table"))
   assert(con:execute"CREATE TABLE work (ora_uid text,card_type text, card_number text, identity text, name text, sex text,birthday date, blood_type text,nativeplace_nation text,nativeplace_province text,nativeplace_city text,nativeplace_district text,address_nation text,address_province text,address_city text,address_district text,address_extend text,address_postcode text,email text,landline text,cellphone text,student_number text,1st_orgnizationid text, 1st_orgnizationname text, 2nd_orgnizationid text, 2nd_orgnizationname text, 3rd_orgnizationid text, 3rd_orgnizationname text, 4th_orgnizationid text, 4th_orgnizationname text, 5th_orgnizationid text, 5th_orgnizationname text, title text) ENGINE=InnoDB DEFAULT CHARSET=utf8")
   -- generate work table

   -- insert data into work table
   print(string.format("preparedata:----->5.Insert data to work table"))
   log_write(string.format("preparedata:----->5.Insert data to work table"))
   assert(con:execute"set names utf8")

   while row do
assert(con:execute(string.format("INSERT INTO work VALUES (\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")", getvalue(row.ora_uid), getZJLB(row.card_type), getvalue(row.card_number), getSF(row.identity), getvalue(row.name), getXB(row.sex), getvalue(row.birthday), getvalue(row.blood_type), getvalue(row.nativeplace_nation), getvalue(row.nativeplace_province), getvalue(row.nativeplace_city), getvalue(row.nativeplace_district), getvalue(row.address_nation), getvalue(row.address_province), getvalue(row.address_city), getvalue(row.address_district), getvalue(row.address_extend), getvalue(row.address_postcode), getvalue(row.email), getvalue(row.landline), getvalue(row.cellphone), getvalue(row.student_number), getvalue(row.oid1), getvalue(row.oname1), getvalue(row.oid2), getvalue(row.oname2), getvalue(row.oid3), getvalue(row.oname3), getvalue(row.oid4), getvalue(row.oname4), getvalue(row.oid5), getvalue(row.oname5), getvalue(row.title))))
        -- reusing the table of results
        row = cur:fetch (row, "a")
   end
   -- insert data into work table
else
print(string.format("Error: no data exist in user_data table, return false"))
log_write(string.format("Error: no data exist in user_data table, return false"))
return false
end
cur:close()
-- query data from database

-- deleetedata
-- assert(con:execute"delete from work where ora_uid=0")

-- close database
print(string.format("preparedata:----->6.Close everything and return true"))
log_write(string.format("preparedata:----->6.Close everything and return true"))
con:close()
env:close()
return true
end


------------------
-- get all the new data should insert to openfire server
-- select * from work where work.ora_uid not in (select ora_uid from prework);
------------------
function get_insertdata()
local env = assert (luasql.mysql())
local con = assert (env:connect(database, user, password, host, port))
assert(con:execute"set names utf8")

local ret = {};

-- check if prework table exist, if not exist, all the data in work table should be return
local cur_table = assert(con:execute"SHOW TABLES LIKE \'prework\'")
os.remove(get_insertdata_path)
local get_insertdata_File = io.open(get_insertdata_path ,"a+");

local row_table = cur_table:fetch({},"a")
   if row_table then
        print(string.format("get_insertdata:------>1. compare work and prework table"))
		log_write(string.format("get_insertdata:------>1. compare work and prework table"))
	-- query data from database
	local cur = assert(con:execute"SELECT ora_uid, card_type, card_number, identity, name, sex, birthday, blood_type, nativeplace_nation, nativeplace_province, nativeplace_city, nativeplace_district, address_nation, address_province, address_city, address_district, address_extend, address_postcode, email, landline, cellphone, student_number, 1st_orgnizationid as oid1, 1st_orgnizationname as oname1, 2nd_orgnizationid as oid2, 2nd_orgnizationname as oname2, 3rd_orgnizationid as oid3, 3rd_orgnizationname as oname3, 4th_orgnizationid as oid4, 4th_orgnizationname as oname4, 5th_orgnizationid as oid5, 5th_orgnizationname as oname5, title from work where work.ora_uid not in (select ora_uid from prework)")

	local row = cur:fetch({},"a")
	while row do
      	      ret = {ora_uid=row.ora_uid, card_type=row.card_type, card_number=row.card_number, identity=row.identity, name=row.name, sex=row.sex, birthday=row.birthday, blood_type=row.blood_type, nativeplace_nation=row.nativeplace_nation, nativeplace_province=row.nativeplace_province, nativeplace_city=row.nativeplace_city, nativeplace_district=row.nativeplace_district, address_nation=row.address_nation, address_province=row.address_province, address_city=row.address_city, address_district=row.address_district, address_extend=row.address_extend, address_postcode=row.address_postcode, email=row.email, landline=row.landline, cellphone=row.cellphone, student_number=row.student_number, oid1=row.oid1, oname1=row.oname1, oid2=row.oid2, oname2=row.oname2, oid3=row.oid3, oname3=row.oname3, oid4=row.oid4, oname4=row.oname4, oid5=row.oid5, oname5=row.oname5, title=row.title }

			  get_insertdata_File:write(json.encode(ret).."\n")
			  print("new id %s", row.ora_uid )
      	      row = cur:fetch (row, "a")
	end
	cur:close()
   else
        print(string.format("get_insertdata:------>1. prework table not exist, return all the data in work table"))
		log_write(string.format("get_insertdata:------>1. prework table not exist, return all the data in work table"))
	local cur = assert(con:execute"SELECT ora_uid, card_type, card_number, identity, name, sex, birthday, blood_type, nativeplace_nation, nativeplace_province, nativeplace_city, nativeplace_district, address_nation, address_province, address_city, address_district, address_extend, address_postcode, email, landline, cellphone, student_number, 1st_orgnizationid as oid1, 1st_orgnizationname as oname1, 2nd_orgnizationid as oid2, 2nd_orgnizationname as oname2, 3rd_orgnizationid as oid3, 3rd_orgnizationname as oname3, 4th_orgnizationid as oid4, 4th_orgnizationname as oname4, 5th_orgnizationid as oid5, 5th_orgnizationname as oname5, title from work")

	local row = cur:fetch({},"a")
	while row do
      	      ret = {ora_uid=row.ora_uid, card_type=row.card_type, card_number=row.card_number, identity=row.identity, name=row.name, sex=row.sex, birthday=row.birthday, blood_type=row.blood_type, nativeplace_nation=row.nativeplace_nation, nativeplace_province=row.nativeplace_province, nativeplace_city=row.nativeplace_city, nativeplace_district=row.nativeplace_district, address_nation=row.address_nation, address_province=row.address_province, address_city=row.address_city, address_district=row.address_district, address_extend=row.address_extend, address_postcode=row.address_postcode, email=row.email, landline=row.landline, cellphone=row.cellphone, student_number=row.student_number, oid1=row.oid1, oname1=row.oname1, oid2=row.oid2, oname2=row.oname2, oid3=row.oid3, oname3=row.oname3, oid4=row.oid4, oname4=row.oname4, oid5=row.oid5, oname5=row.oname5, title=row.title }
			  get_insertdata_File:write(json.encode(ret).."\n")
			  log_write("new id"..row.ora_uid)
      	      print("new id %s", row.ora_uid )
      	      row = cur:fetch (row, "a")
	end
	cur:close()

   end
cur_table:close()

con:close()
env:close()
get_insertdata_File:close()
return ret
end

------------------
-- get all the data should delete from openfire server
-- select * from prework where ora_uid not in (select ora_uid from work);
------------------
function get_deletedata()
local env = assert (luasql.mysql())
local con = assert (env:connect(database, user, password, host, port))
assert(con:execute"set names utf8")
os.remove(get_deletedata_path)
local get_deletedata_File = io.open(get_deletedata_path ,"a+");
local ret = {};

-- check if prework table exist, if not exist, all the data in work table should be return
local cur_table = assert(con:execute"SHOW TABLES LIKE \'prework\'")
local row_table = cur_table:fetch({},"a")
   if row_table then
        print(string.format("get_deletedata:------>1. compare work and prework table"))

	-- query data from database
	local cur = assert(con:execute"SELECT ora_uid, card_type, card_number, identity, name, sex, birthday, blood_type, nativeplace_nation, nativeplace_province, nativeplace_city, nativeplace_district, address_nation, address_province, address_city, address_district, address_extend, address_postcode, email, landline, cellphone, student_number, 1st_orgnizationid as oid1, 1st_orgnizationname as oname1, 2nd_orgnizationid as oid2, 2nd_orgnizationname as oname2, 3rd_orgnizationid as oid3, 3rd_orgnizationname as oname3, 4th_orgnizationid as oid4, 4th_orgnizationname as oname4, 5th_orgnizationid as oid5, 5th_orgnizationname as oname5, title from prework where ora_uid not in (select ora_uid from work)")

	local row = cur:fetch({},"a")
	while row do
      	      ret = {ora_uid=row.ora_uid, card_type=row.card_type, card_number=row.card_number, identity=row.identity, name=row.name, sex=row.sex, birthday=row.birthday, blood_type=row.blood_type, nativeplace_nation=row.nativeplace_nation, nativeplace_province=row.nativeplace_province, nativeplace_city=row.nativeplace_city, nativeplace_district=row.nativeplace_district, address_nation=row.address_nation, address_province=row.address_province, address_city=row.address_city, address_district=row.address_district, address_extend=row.address_extend, address_postcode=row.address_postcode, email=row.email, landline=row.landline, cellphone=row.cellphone, student_number=row.student_number, oid1=row.oid1, oname1=row.oname1, oid2=row.oid2, oname2=row.oname2, oid3=row.oid3, oname3=row.oname3, oid4=row.oid4, oname4=row.oname4, oid5=row.oid5, oname5=row.oname5, title=row.title }

			  get_deletedata_File:write(json.encode(ret).."\n")
			  print("new id %s", row.ora_uid )
      	      row = cur:fetch (row, "a")
	end
	cur:close()
   end
cur_table:close()

con:close()
env:close()


get_deletedata_File:close()
return ret
end




------------------
-- update the data in work table to openfire server
-- select work.ora_uid, work.card_type from work join prework on work.ora_uid=prework.ora_uid where work.card_type!=prework.card_type;
------------------
function get_updatedata()
local env = assert (luasql.mysql())
local con = assert (env:connect(database, user, password, host, port))
assert(con:execute"set names utf8")
os.remove(get_updatedata_path)
local get_updatedata_File = io.open(get_updatedata_path ,"a+");

local ret = {};
-- check if prework table exist, if not exist, all the data in work table should be return
local cur_table = assert(con:execute"SHOW TABLES LIKE \'prework\'")
local row_table = cur_table:fetch({},"a")
   if row_table then
        print(string.format("get_updatedata:------>1. compare work and prework table find the different row"))

	-- query data from database
	local cur = assert(con:execute"select work.ora_uid, work.card_type, work.card_number, work.identity, work.name, work.sex, work.birthday, work.blood_type, work.nativeplace_nation, work.nativeplace_province, work.nativeplace_city, work.nativeplace_district, work.address_nation, work.address_province, work.address_city, work.address_district, work.address_extend, work.address_postcode, work.email, work.landline, work.cellphone, work.student_number, work.1st_orgnizationid as oid1, work.1st_orgnizationname as oname1, work.2nd_orgnizationid as oid2, work.2nd_orgnizationname as oname2, work.3rd_orgnizationid as oid3, work.3rd_orgnizationname as oname3, work.4th_orgnizationid as oid4, work.4th_orgnizationname as oname4, work.5th_orgnizationid as oid5, work.5th_orgnizationname as oname5, work.title from work join prework on work.ora_uid=prework.ora_uid where work.card_type!=prework.card_type or work.card_number!=prework.card_number or work.identity!=prework.identity or work.name!=prework.name or work.sex!=prework.sex or work.birthday!=prework.birthday or work.blood_type!=prework.blood_type or work.nativeplace_nation!=prework.nativeplace_nation or work.nativeplace_province!=prework.nativeplace_province or work.nativeplace_city!=prework.nativeplace_city or work.nativeplace_district!=prework.nativeplace_district or work.address_nation!=prework.address_nation or work.address_province!=prework.address_province or work.address_city!=prework.address_city or work.address_district!=prework.address_district or work.address_extend!=prework.address_extend or work.address_postcode!=prework.address_postcode or work.email!=prework.email or work.landline!=prework.landline or work.cellphone!=prework.cellphone or work.student_number!=prework.student_number or work.1st_orgnizationid!=prework.1st_orgnizationid or work.1st_orgnizationname!=prework.1st_orgnizationname or work.2nd_orgnizationid!=prework.2nd_orgnizationid or work.2nd_orgnizationname!=prework.2nd_orgnizationname or work.3rd_orgnizationid!=prework.3rd_orgnizationid or work.3rd_orgnizationname!=prework.3rd_orgnizationname or work.4th_orgnizationid!=prework.4th_orgnizationid or work.4th_orgnizationname!=prework.4th_orgnizationname or work.5th_orgnizationid!=prework.5th_orgnizationid or work.5th_orgnizationname!=prework.5th_orgnizationname or work.title!=prework.title")

	local row = cur:fetch({},"a")
	while row do
      	      ret = {ora_uid=row.ora_uid, card_type=row.card_type, card_number=row.card_number, identity=row.identity, name=row.name, sex=row.sex, birthday=row.birthday, blood_type=row.blood_type, nativeplace_nation=row.nativeplace_nation, nativeplace_province=row.nativeplace_province, nativeplace_city=row.nativeplace_city, nativeplace_district=row.nativeplace_district, address_nation=row.address_nation, address_province=row.address_province, address_city=row.address_city, address_district=row.address_district, address_extend=row.address_extend, address_postcode=row.address_postcode, email=row.email, landline=row.landline, cellphone=row.cellphone, student_number=row.student_number, oid1=row.oid1, oname1=row.oname1, oid2=row.oid2, oname2=row.oname2, oid3=row.oid3, oname3=row.oname3, oid4=row.oid4, oname4=row.oname4, oid5=row.oid5, oname5=row.oname5, title=row.title}
			  get_updatedata_File:write(json.encode(ret).."\n")
			  print("new id %s", row.ora_uid )
      	      row = cur:fetch (row, "a")
	end
	cur:close()
   end
cur_table:close()

con:close()
env:close()

get_updatedata_File:close()
return ret
end


preparedata()
get_insertdata()
get_deletedata()
get_updatedata()
log_file:close()
