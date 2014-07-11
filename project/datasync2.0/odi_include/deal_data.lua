--************************************
-- 脚本功能:比对数据,生成要插入/删除/更新的数据
--************************************

package.path = package.path..";/usr/local/whistle/openfire/resources/auth/lua/odi_include/?.lua"
package.cpath = package.cpath..";/usr/lib64/lua/5.1/?.so;/usr/lib64/lua/5.1/luasql/?.so"

--[[引用]]
local global = require("global")
local string = require("string")
local table = require("table")
local os = require("os")
local io = require("io")
local mysql_db = require("luasql.mysql")
local json = require("json")
local base = _G
local mysql_env = nil

--[[全局变量]]
local deal_data_log = global.root_path.."log/deal_data.log"
local get_insertdata_path = global.root_path.."get_insertdata.txt"
local get_deletedata_path = global.root_path.."get_deletedata.txt"
local get_updatedata_path = global.root_path.."get_updatedata.txt"
local config = nil

module("deal_data")

local print = base.print
local assert = base.assert
local pcall = base.pcall
local config = global.config
local gbk_to_utf8 = global.gbk_to_utf8
local utf8_to_gbk = global.utf8_to_gbk

local function error_log(log)
        global.error_log(deal_data_log,log)
end

local function debug_log(log)
        global.debug_log(deal_data_log,log)
end

--[[ 初始化 ]]-- 
function deal_init()
        os.remove(deal_data_log)
        _,mysql_env = pcall(mysql_db.mysql)
        if(mysql_env == nil) then
                error_log("call mysql_db.mysql(),error")
                return nil
        end
        return "ok"
end

--[[ 获取 ]]-- 
function get_value(value)
        if value == nil then
                return ""
        else
                if(school_db_charset == "GBK" and sync_mode == false) then
                        value = gbk_to_utf8(value)
                end
                return value
        end
end

--[[ 获取性别 ]]--
function get_gender(value)
        local xb = (value)
        if xb == "1" then
                return "boy"
        elseif xb == "2" then
                return "girl"
        else
                return xb
        end
end

--[[ 获取性别 ]]--
function getZJLB(value)
        return "id"
end

--[[ 获取性别 ]]--
function get_identity(value)
        local xb = (value)

        if xb == "学生" then
                return "student"
        elseif xb == "教职工" then
                return "teacher"
        else
                return xb
        end
end



--[[ 准备数据,将数据从user_data表,导入work表 ]]--
function prepare_data()
        local ret = nil
        local con = nil
        local cur = nil
        _,con = pcall(mysql_env.connect,mysql_env,config["mysql_db_name"], config["mysql_db_username"], config["mysql_db_userpwd"], config["mysql_db_host"], config["mysql_db_port"])
        ret = pcall(con.execute,con,"set names utf8")--根据校方数据库进行设置
        if(not ret) then 
                error_log("set names error")
                return nil
        end
        _,ret = pcall(con.execute,con,config["user_data_sql"])
        if(not ret) then 
                error_log("create view user_data error,sql:"..config["user_data_sql"])
                return nil
        end
        _,cur = pcall(con.execute,con,"SELECT ora_uid, card_type, card_number, identity, name, sex, birthday, blood_type, nativeplace_nation, nativeplace_province, nativeplace_city, nativeplace_district, address_nation, address_province, address_city, address_district, address_extend, address_postcode, email, landline, cellphone, student_number, 1st_orgnizationid as oid1, 1st_orgnizationname as oname1, 2nd_orgnizationid as oid2, 2nd_orgnizationname as oname2, 3rd_orgnizationid as oid3, 3rd_orgnizationname as oname3, 4th_orgnizationid as oid4, 4th_orgnizationname as oname4, 5th_orgnizationid as oid5, 5th_orgnizationname as oname5, title from user_data")
        local row = cur:fetch({},"a")
        if row then
                _,ret = pcall(con.execute,con,"DROP TABLE IF EXISTS prework")
                if(not ret) then
                        error_log("drop table prework error")
                        return nil
                end

                local cur_table = nil
                _,cur_table = pcall(con.execute,con,"SHOW TABLES LIKE \'work\'")
                local row_table = cur_table:fetch({},"a")
                if row_table then
                        _,ret = pcall(con.execute,con,"RENAME TABLE work to prework")
                        if(not ret) then 
                                error_log("rename work to prework error")
                                return nil
                        end
                end
                cur_table:close()

                _,ret = pcall(con.execute,con,"CREATE TABLE work (ora_uid text,card_type text, card_number text, identity text, name text, sex text,birthday date, blood_type text,nativeplace_nation text,nativeplace_province text,nativeplace_city text,nativeplace_district text,address_nation text,address_province text,address_city text,address_district text,address_extend text,address_postcode text,email text,landline text,cellphone text,student_number text,1st_orgnizationid text, 1st_orgnizationname text, 2nd_orgnizationid text, 2nd_orgnizationname text, 3rd_orgnizationid text, 3rd_orgnizationname text, 4th_orgnizationid text, 4th_orgnizationname text, 5th_orgnizationid text, 5th_orgnizationname text, title text) ENGINE=InnoDB DEFAULT CHARSET=utf8")
                if(not ret) then 
                        error_log("create table error!")
                        return nil
                end
                while row do
                        --print(row.name..":"..gbk_to_utf8(row.name))
                        --local str = string.format("insert into work values (\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")",row.ora_uid,row.card_type,row.card_number,row.identity,row.name,row.sex);
                        _,ret = pcall(con.execute,con,string.format("INSERT INTO work VALUES (\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")", (row.ora_uid), getZJLB(row.card_type), (row.card_number), get_identity(row.identity), get_value(row.name), get_gender(row.sex), get_value(row.birthday), get_value(row.blood_type), get_value(row.nativeplace_nation), get_value(row.nativeplace_province), get_value(row.nativeplace_city), get_value(row.nativeplace_district), get_value(row.address_nation), get_value(row.address_province), get_value(row.address_city), get_value(row.address_district), get_value(row.address_extend), get_value(row.address_postcode), get_value(row.email), get_value(row.landline), get_value(row.cellphone), get_value(row.student_number), get_value(row.oid1), get_value(row.oname1), get_value(row.oid2), get_value(row.oname2), get_value(row.oid3), get_value(row.oname3), get_value(row.oid4), get_value(row.oname4), get_value(row.oid5), get_value(row.oname5), get_value(row.title)))
                        if(not ret) then
                                error_log("insert into work data error! to rollback")
                                _,ret = pcall(con.execute,con,"DROP TABLE IF EXISTS work")
                                if(not ret) then
                                        error_log("drop table work error")
                                        return nil
                                end

                                _,ret = pcall(con.execute,con,"RENAME TABLE prework to work")
                                if(not ret) then 
                                        error_log("rename prework to work error")
                                        return nil
                                end

                        end
                        row = cur:fetch (row, "a")
                end
        else
                error_log("user_data table is NULL!")
                return nil
        end
        cur:close()
        con:close()
        return "ok"
end

--[[ 获取需要插入的数据 ]]--
function get_insertdata()
        local env = nil
        local con = nil
        local rtn = nil
        _,env = pcall(mysql_db.mysql)
        if(not env) then
                error_log("get mysql env error")
                return nil
        end
        _,con = pcall(mysql_env.connect,mysql_env,config["mysql_db_name"], config["mysql_db_username"], config["mysql_db_userpwd"], config["mysql_db_host"], config["mysql_db_port"])
        if(not con) then 
                error_log(string.format("connect mysql error,mysql_db_name:%s,mysql_db_username:%s,mysql_db_userpwd:%s,mysql_db_host:%s,mysql_db_port:%s",config["mysql_db_name"], config["mysql_db_username"], config["mysql_db_userpwd"], config["mysql_db_host"], config["mysql_db_port"]))
                return nil
        end
        _,rtn = pcall(con.execute,con,"set names utf8")--根据校方数据库进行设置
        if(not rtn) then
                error_log("set names error")
                return nil
        end
        local ret = {}
        local cur_table = nil
        _,cur_table = pcall(con.execute,con,"SHOW TABLES LIKE \'prework\'")
        if(not cur_table) then 
                error_log("show tables like prework")
                return nil
        end
        pcall(os.remove,get_insertdata_path)
        local get_insertdata_file = io.open(get_insertdata_path ,"a+");

        local row_table = cur_table:fetch({},"a")
        if row_table then
                local cur = nil
                local tmp = nil
                tmp,cur = pcall(con.execute,con,"SELECT ora_uid, card_type, card_number, identity, name, sex, birthday, blood_type, nativeplace_nation, nativeplace_province, nativeplace_city, nativeplace_district, address_nation, address_province, address_city, address_district, address_extend, address_postcode, email, landline, cellphone, student_number, 1st_orgnizationid as oid1, 1st_orgnizationname as oname1, 2nd_orgnizationid as oid2, 2nd_orgnizationname as oname2, 3rd_orgnizationid as oid3, 3rd_orgnizationname as oname3, 4th_orgnizationid as oid4, 4th_orgnizationname as oname4, 5th_orgnizationid as oid5, 5th_orgnizationname as oname5, title from work where work.ora_uid not in (select ora_uid from prework)")
                if(not cur) then
                        error_log("select data from work not forom prework error")
                        pcall(os.remove,get_insertdata_path)
                        return nil
                end
                local row = cur:fetch({},"a")
                while row do
                        ret = {ora_uid=row.ora_uid, card_type=row.card_type, card_number=row.card_number, identity=row.identity, name=row.name, sex=row.sex, birthday=row.birthday, blood_type=row.blood_type, nativeplace_nation=row.nativeplace_nation, nativeplace_province=row.nativeplace_province, nativeplace_city=row.nativeplace_city, nativeplace_district=row.nativeplace_district, address_nation=row.address_nation, address_province=row.address_province, address_city=row.address_city, address_district=row.address_district, address_extend=row.address_extend, address_postcode=row.address_postcode, email=row.email, landline=row.landline, cellphone=row.cellphone, student_number=row.student_number, oid1=row.oid1, oname1=row.oname1, oid2=row.oid2, oname2=row.oname2, oid3=row.oid3, oname3=row.oname3, oid4=row.oid4, oname4=row.oname4, oid5=row.oid5, oname5=row.oname5, title=row.title }
                        get_insertdata_file:write(json.encode(ret).."\n")
                        row = cur:fetch (row, "a")
                end
                cur:close()
        else
                local cur = nil
                _,cur = pcall(con.execute,con,"SELECT ora_uid, card_type, card_number, identity, name, sex, birthday, blood_type, nativeplace_nation, nativeplace_province, nativeplace_city, nativeplace_district, address_nation, address_province, address_city, address_district, address_extend, address_postcode, email, landline, cellphone, student_number, 1st_orgnizationid as oid1, 1st_orgnizationname as oname1, 2nd_orgnizationid as oid2, 2nd_orgnizationname as oname2, 3rd_orgnizationid as oid3, 3rd_orgnizationname as oname3, 4th_orgnizationid as oid4, 4th_orgnizationname as oname4, 5th_orgnizationid as oid5, 5th_orgnizationname as oname5, title from work")
                if(not cur) then
                        error_log("select data from work not forom prework error")
                        pcall(os.remove,get_insertdata_path)
                        return nil
                end

                local row = cur:fetch({},"a")
                while row do
                        ret = {ora_uid=row.ora_uid, card_type=row.card_type, card_number=row.card_number, identity=row.identity, name=row.name, sex=row.sex, birthday=row.birthday, blood_type=row.blood_type, nativeplace_nation=row.nativeplace_nation, nativeplace_province=row.nativeplace_province, nativeplace_city=row.nativeplace_city, nativeplace_district=row.nativeplace_district, address_nation=row.address_nation, address_province=row.address_province, address_city=row.address_city, address_district=row.address_district, address_extend=row.address_extend, address_postcode=row.address_postcode, email=row.email, landline=row.landline, cellphone=row.cellphone, student_number=row.student_number, oid1=row.oid1, oname1=row.oname1, oid2=row.oid2, oname2=row.oname2, oid3=row.oid3, oname3=row.oname3, oid4=row.oid4, oname4=row.oname4, oid5=row.oid5, oname5=row.oname5, title=row.title }
                        get_insertdata_file:write(json.encode(ret).."\n")
                        row = cur:fetch (row, "a")
                end
                cur:close()
        end
        cur_table:close()
        con:close()
        env:close()
        get_insertdata_file:close()
        return ret
end

--[[ 获取删除数据 ]]--
function get_deletedata()
        local env = nil 
        local con = nil
        local rtn = nil
        --_,env = pcall(mysql_db.mysql)
        _,con = pcall(mysql_env.connect,mysql_env,config["mysql_db_name"], config["mysql_db_username"], config["mysql_db_userpwd"], config["mysql_db_host"], config["mysql_db_port"])
        if(not con) then
                error_log("connect mysql error")
                return nil
        end
        _,rtn = pcall(con.execute,con,"set names utf8")--根据校方数据库进行设置
        if(not rtn) then 
                error_log("set names error")
                return nil
        end
        pcall(os.remove,get_deletedata_path)
        local get_deletedata_file = io.open(get_deletedata_path ,"a+");
        local ret = {};
        local cur_table = nil
        _,cur_table  = pcall(con.execute,con,"SHOW TABLES LIKE \'prework\'")
        if(not cur_table) then
                error_log("show table like prework error")
                return nil
        end
        print(1)
        local row_table = cur_table:fetch({},"a")
        if row_table then
                local cur = nil 
                _,cur = pcall(con.execute,con,"SELECT ora_uid, card_type, card_number, identity, name, sex, birthday, blood_type, nativeplace_nation, nativeplace_province, nativeplace_city, nativeplace_district, address_nation, address_province, address_city, address_district, address_extend, address_postcode, email, landline, cellphone, student_number, 1st_orgnizationid as oid1, 1st_orgnizationname as oname1, 2nd_orgnizationid as oid2, 2nd_orgnizationname as oname2, 3rd_orgnizationid as oid3, 3rd_orgnizationname as oname3, 4th_orgnizationid as oid4, 4th_orgnizationname as oname4, 5th_orgnizationid as oid5, 5th_orgnizationname as oname5, title from prework where ora_uid not in (select ora_uid from work)")
                if(not cur) then
                        error_log("select data from prewok not from work error")
                        pcall(os.remove,get_deletedata_path)
                        return nil
                end

                local row = cur:fetch({},"a")
                while row do
                        ret = {ora_uid=row.ora_uid, card_type=row.card_type, card_number=row.card_number, identity=row.identity, name=row.name, sex=row.sex, birthday=row.birthday, blood_type=row.blood_type, nativeplace_nation=row.nativeplace_nation, nativeplace_province=row.nativeplace_province, nativeplace_city=row.nativeplace_city, nativeplace_district=row.nativeplace_district, address_nation=row.address_nation, address_province=row.address_province, address_city=row.address_city, address_district=row.address_district, address_extend=row.address_extend, address_postcode=row.address_postcode, email=row.email, landline=row.landline, cellphone=row.cellphone, student_number=row.student_number, oid1=row.oid1, oname1=row.oname1, oid2=row.oid2, oname2=row.oname2, oid3=row.oid3, oname3=row.oname3, oid4=row.oid4, oname4=row.oname4, oid5=row.oid5, oname5=row.oname5, title=row.title }
                        get_deletedata_file:write(json.encode(ret).."\n")
                        row = cur:fetch (row, "a")
                end
                cur:close()
        end
        cur_table:close()
        con:close()
        get_deletedata_file:close()
        return ret
end

--[[ 获取更新数据 ]]--
function get_updatedata()
        local con = nil
        _,con = pcall(mysql_env.connect,mysql_env,config["mysql_db_name"], config["mysql_db_username"], config["mysql_db_userpwd"], config["mysql_db_host"], config["mysql_db_port"])
        
        if(not con) then
                error_log("connect mysql error")
                return nil
        end
        _,rtn = pcall(con.execute,con,"set names utf8")--根据校方数据库进行设置
        if(not rtn) then 
                error_log("set names error")
                return nil
        end
        pcall(os.remove,get_updatedata_path)
        local get_updatedata_file = io.open(get_updatedata_path ,"a+");
        local ret = {};
        local cur_table = assert(con:execute"SHOW TABLES LIKE \'prework\'")
        local row_table = cur_table:fetch({},"a")
        if row_table then
                local cur = nil
                _,cur = pcall(con.execute,con,"select work.ora_uid, work.card_type, work.card_number, work.identity, work.name, work.sex, work.birthday, work.blood_type, work.nativeplace_nation, work.nativeplace_province, work.nativeplace_city, work.nativeplace_district, work.address_nation, work.address_province, work.address_city, work.address_district, work.address_extend, work.address_postcode, work.email, work.landline, work.cellphone, work.student_number, work.1st_orgnizationid as oid1, work.1st_orgnizationname as oname1, work.2nd_orgnizationid as oid2, work.2nd_orgnizationname as oname2, work.3rd_orgnizationid as oid3, work.3rd_orgnizationname as oname3, work.4th_orgnizationid as oid4, work.4th_orgnizationname as oname4, work.5th_orgnizationid as oid5, work.5th_orgnizationname as oname5, work.title from work join prework on work.ora_uid=prework.ora_uid where work.card_type!=prework.card_type or work.card_number!=prework.card_number or work.identity!=prework.identity or work.name!=prework.name or work.sex!=prework.sex or work.birthday!=prework.birthday or work.blood_type!=prework.blood_type or work.nativeplace_nation!=prework.nativeplace_nation or work.nativeplace_province!=prework.nativeplace_province or work.nativeplace_city!=prework.nativeplace_city or work.nativeplace_district!=prework.nativeplace_district or work.address_nation!=prework.address_nation or work.address_province!=prework.address_province or work.address_city!=prework.address_city or work.address_district!=prework.address_district or work.address_extend!=prework.address_extend or work.address_postcode!=prework.address_postcode or work.email!=prework.email or work.landline!=prework.landline or work.cellphone!=prework.cellphone or work.student_number!=prework.student_number or work.1st_orgnizationid!=prework.1st_orgnizationid or work.1st_orgnizationname!=prework.1st_orgnizationname or work.2nd_orgnizationid!=prework.2nd_orgnizationid or work.2nd_orgnizationname!=prework.2nd_orgnizationname or work.3rd_orgnizationid!=prework.3rd_orgnizationid or work.3rd_orgnizationname!=prework.3rd_orgnizationname or work.4th_orgnizationid!=prework.4th_orgnizationid or work.4th_orgnizationname!=prework.4th_orgnizationname or work.5th_orgnizationid!=prework.5th_orgnizationid or work.5th_orgnizationname!=prework.5th_orgnizationname or work.title!=prework.title")
                if(not cur) then
                        error_log("get update data error")
                        return nil
                end
                local row = cur:fetch({},"a")
                while row do
                        ret = {ora_uid=row.ora_uid, card_type=row.card_type, card_number=row.card_number, identity=row.identity, name=row.name, sex=row.sex, birthday=row.birthday, blood_type=row.blood_type, nativeplace_nation=row.nativeplace_nation, nativeplace_province=row.nativeplace_province, nativeplace_city=row.nativeplace_city, nativeplace_district=row.nativeplace_district, address_nation=row.address_nation, address_province=row.address_province, address_city=row.address_city, address_district=row.address_district, address_extend=row.address_extend, address_postcode=row.address_postcode, email=row.email, landline=row.landline, cellphone=row.cellphone, student_number=row.student_number, oid1=row.oid1, oname1=row.oname1, oid2=row.oid2, oname2=row.oname2, oid3=row.oid3, oname3=row.oname3, oid4=row.oid4, oname4=row.oname4, oid5=row.oid5, oname5=row.oname5, title=row.title}
                        get_updatedata_file:write(json.encode(ret).."\n")
                        row = cur:fetch (row, "a")
                end
                cur:close()
        end
        cur_table:close()
        con:close()
        get_updatedata_file:close()
        return ret
end

--[[ 数据处理 ]]--
function process()
        local ret = prepare_data()
        if(ret == nil) then 
                error_log("call prepare_data(),error")
                return nil 
        end
        ret = get_insertdata()
        if(ret == nil) then 
                error_log("call get_insertdata(),error")
                return nil
        end
        ret = get_deletedata()
        if(ret == nil) then 
                error_log("call get_deletedata(),error")
                return nil 
        end
        ret = get_updatedata()
        if(ret == nil) then
                error_log("call get_updatedata(),error")
                return nil
        end
        return "ok"
end

--[[ 资源回收 ]]-- 
function deal_finalize()
end

--[[ ###接口函数:odi_interface.lua调用 ### ]]-- 
function deal_run(conf)
        print(0)
        config = conf
        print(1)
        debug_log("call deal_data run"..config["mysql_db_host"])
        local ret = deal_init()
        print(1)
        if(ret == nil) then print(100); return nil end
        ret = process()
        print(1)
        if(ret == nil) then print(101); return nil end
        deal_finalize()
        print(1)
        debug_log("call deal_data run ok")
        return "ok"
end

--[[ ###接口函数:odi_interface.lua调用 ### ]]-- 
function reset_db(conf)
        _,mysql_env = pcall(mysql_db.mysql)
        if(mysql_env == nil) then
                error_log("call mysql_db.mysql(),error")
                return nil
        end
        local ret = nil
        local con = nil
        print(conf["mysql_db_name"]..":".. conf["mysql_db_username"]..":"..conf["mysql_db_userpwd"]..":"..conf["mysql_db_host"]..":"..conf["mysql_db_port"])
        _,con = pcall(mysql_env.connect,mysql_env,conf["mysql_db_name"], conf["mysql_db_username"], conf["mysql_db_userpwd"], conf["mysql_db_host"], conf["mysql_db_port"])
        ret = pcall(con.execute,con,"drop table if exists work")
        if(not ret) then 
                error_log("drop table work error")
                return nil
        end
        
        local cur_table = nil
        _,cur_table = pcall(con.execute,con,"SHOW TABLES LIKE \'prework\'")
        local row_table = cur_table:fetch({},"a")
        if row_table then
                _,ret = pcall(con.execute,con,"RENAME TABLE prework to work")
                if(not ret) then 
                        error_log("rename prework to work error")
                        return nil
                end
        end
        cur_table:close()

        return "ok"              
end
