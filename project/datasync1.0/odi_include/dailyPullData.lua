------------------------------------------------------------
-- dailyPullData.lua
-- author: quanli 2014-1-23
-----------------------------------------------------------
luasql = require "luasql.odbc"
odbcname = "oracle11"
env = luasql.odbc()
con = assert (env:connect(odbcname))
print("dailyPullData-------->1. Connected to odbc %s", odbcname)

local iconv = require("iconv")
cd = iconv.new("utf8", "gbk")

--odi_daily.log
odi_interface_log_path = "/usr/local/whistle/openfire/resources/auth/lua/log/odi_daily.log"
os.remove(odi_interface_log_path)
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
-----------------------------
--open lua log file
-----------------------------
--log_file = io.open(odi_interface_log_path,"a")

log_write("dailyPullData-------->1. Connected to odbc"..odbcname)

-----------------------------
local host = "192.168.254.100"
local port = 3306
local user = "odi"
local password = "odiodi"
local database = "odi_data_sync"
luasqlmysql = require "luasql.mysql"
envmysql = luasqlmysql.mysql()
conmysql = assert (envmysql:connect(database, user, password, host, port))
print(string.format("dailyPullData-------->2. Connected to mysql %s@%s:%s", database, host, port))
log_write("dailyPullData-------->2. Connected to mysql"..database..host..port)
assert(conmysql:execute"set names utf8")

function getXB(value)
 if value then
    return value
 else
    return ""
 end
end

function getvalue(value)
 if value then
         local ret = cd:iconv(value)
    return ret
 else
    return ""
 end
end

res = con:execute"select * from usr_gxsj.v_sam_user"
row = res:fetch ({}, "a")
if row then
   print(string.format("dailyPullData-------->3. Got data, import data to mysql database"))
   log_write(string.format("dailyPullData-------->3. Got data, import data to mysql database"))
   assert(conmysql:execute("delete from user_data"))
   while row do
   	 print(string.format("dailyPullData-------->INFO: %s %s", getvalue(row.XH), getvalue(row.XM)))
	 log_write(string.format("dailyPullData-------->INFO: %s %s", getvalue(row.XH), getvalue(row.XM)))
   	 assert(conmysql:execute(string.format("INSERT INTO user_data VALUES (\"%s\",\"%s\",\"%s\", 100, \"\", \"\", \"\", \"%s\", \"\", \"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\", \"\",\"\",\"\",\"\",\"%s\", \"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")", getvalue(row.XH), getvalue(row.SFZJLXDM), getvalue(row.SFZJH), getvalue(row.SF), getvalue(row.XM), getXB(row.XBDM), getvalue(row.birthday), getvalue(row.blood_type), getvalue(row.nativeplace_nation), getvalue(row.nativeplace_province), ""--[[getvalue(row.JGDM)]], getvalue(row.nativeplace_district), getvalue(row.address_nation), getvalue(row.address_province), getvalue(row.address_city), getvalue(row.address_district), getvalue(row.TXDZ), getvalue(row.TXYB), getvalue(row.DZXX), getvalue(row.LXDH), getvalue(row.SJH), getvalue(row.XH), getvalue(row.ZW), getvalue(row.YXSH), getvalue(row.YXMC), getvalue(row.ZYDM), getvalue(row.ZYMC), getvalue(row.BJDM), getvalue(row.BJMC), getvalue(row.oid4), getvalue(row.oid4), getvalue(row.oid5), getvalue(row.oid5))))
	 row = res:fetch (row, "a")
	 end
	 print(string.format("dailyPullData-------->3. import data to mysql database done, please check data user_data@%s", database))
else
	 print(string.format("dailyPullData-------->3. No data in odbc resource"))
	 log_write(string.format("dailyPullData-------->3. No data in odbc resource"))
end
res:close()
log_file:close()
conmysql:close()
envmysql:close()
con:close()
env:close()


