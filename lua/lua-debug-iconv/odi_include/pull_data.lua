--************************************
-- 脚本功能:将数据同步到MongoDB
--************************************

--[[引用]]
local string = require("string")
local table = require("table")
local global = require("global")
local os = require("os")
local oracle = require("luasql.odbc")
local mysql = require("luasql.mysql")
local base = _G

module("pull_data")

--[[变量]]
local pull_data_log = global.root_path.."log/pull_data.log"
local school_db_tablename = {} 
local school_db = nil 
local school_env = nil
local school_con = nil
local mysql_db = nil 
local mysql_env = nil
local mysql_con = nil
local res = nil
local row = nil
local fields = {}
local print = base.print
local assert = base.assert
local pairs = base.pairs
local ipairs = base.ipairs
local pcall = base.pcall
local config = global.config
local parameter_parser = global.parameter_parser
local gbk_to_utf8 = global.gbk_to_utf8
local utf8_to_gbk = global.utf8_to_gbk

local function error_log(log)
        global.error_log(pull_data_log,log)
end

local function debug_log(log)
        global.debug_log(pull_data_log,log)
end

--[[ daily_pull初始化 ]]-- 
function pull_init()
        local ret = nil
        if(config["school_db_type"] == "oracle") then
                school_db = oracle
                _,school_env = pcall(school_db.odbc)
                ret,school_con = pcall(school_env.connect, school_env, config["school_db_servername"])
        elseif(config["school_db_type"] == "mysql") then
                school_db = mysql
                _,school_env = pcall(school_db.mysql)
                _,school_con = pcall(mysql_env.connect,mysql_env, config["school_db_name"], config["school_db_username"], config["school_db_userpwd"], config["school_db_host"], config["school_db_port"])
        else
                error_log("unknown database type")
                return nil
        end
        if(school_con == nil) then
                error_log("connect school databases error")
                return nil
        end

        mysql_db = mysql
        _,mysql_env = pcall(mysql_db.mysql)
        print(config["mysql_db_name"]..":".. config["mysql_db_username"]..":"..config["mysql_db_userpwd"]..":"..config["mysql_db_host"]..":"..config["mysql_db_port"])
        _,mysql_con = pcall(mysql_env.connect,mysql_env, config["mysql_db_name"], config["mysql_db_username"], config["mysql_db_userpwd"], config["mysql_db_host"], config["mysql_db_port"])
        if(mysql_con == nil) then 
                error_log("mysql_con is nil")
                return nil
        end
        os.remove(pull_data_log)
        _,ret = pcall(mysql_con.execute,mysql_con,"set names utf8")
        if(not ret) then error_log("set names error") end
        print("2")
        return "ok"
end


--[[ 使用管理员配置的sql语句创建校方表 ]]--
function create_table(sql)
        local ret = nil
        if(string.find(sql,"create") == nil or string.find(sql,"table") == nil) then
                error("invalid sql:"..sql)
                return nil
        end

        _,ret = pcall(mysql_con.execute,mysql_con,sql)
        if(not ret) then
                error_log("create table error,sql:",sql)
                return nil
        end
        return "ok"
end 

--[[ 使用管理员配置的sql语句创建校方所有表 ]]--
function create_tables()
        local ret = nil
        print(config["school_db_sqls"])
        local tables = parameter_parser(config["school_db_sqls"],"#")
        _,ret = pcall(mysql_con.execute,mysql_con,"begin")
        if(not ret) then 
                error_log("execute begin transaction")
                return nil
        end
        for i,v in pairs(tables) do
                ret = create_table(v)
                if(ret == nil) then 
                        error_log("create table error,sql:"..v)
                        _,ret = pcall(mysql_con.execute,mysql_con,"rollback")
                        if(ret == nil) then error_log("execute rollback error"); return nil end
                end
        end
        _,ret = pcall(mysql_con.execute,mysql_con,"commit")
        if(not ret) then 
                error_log("execute commit error")
                pcall(mysql_con.execute,mysql_con,"rollback")
                return nil
        end
        debug_log("create tables success!")
        return "ok"
end

--[[ 获取全部表名 ]]
function get_tablenames()
        local show_table = string.format("show tables")
        _,res = pcall(mysql_con.execute,mysql_con,show_table)
        row = res:fetch ({}, "a")
        if row then
                while row do
                        if(string.find(row.Tables_in_odi_data_sync,"ds") == 1) then
                                table.insert(school_db_tablename,row.Tables_in_odi_data_sync)
                        end
                        row = res:fetch (row, "a")
                end
        else
                error_log("row is nil")
        end
end

--[[ 获取表字段 ]]
function get_fields(tablename)
        local fields = ""
        local ret = nil
        local desc_table = string.format("desc %s",tablename)

        ret,res = pcall(mysql_con.execute,mysql_con,desc_table)
        row = res:fetch ({}, "a")
        if row then
                while row do
                        if string.len(fields) == 0 then 
                                fields = string.format("%s",row.Field)
                        else
                                fields = string.format("%s,%s",fields,row.Field)
                        end
                        row = res:fetch (row, "a")
                end
        else
                error_log("row is nil")
        end
        return fields
end

function set_fields(fields,key,value)
        local ret = ""
        local table_fields = parameter_parser(fields,",")
        for k,v in ipairs(table_fields) do
                if(v == key) then 
                        table_fields[k] = value
                        break
                end
        end

        for k,v in ipairs(table_fields) do
                if(string.len(ret) == 0) then 
                        ret = v
                else
                        ret = string.format("%s,%s",ret,v)
                end
        end
        return ret;
end

--[[ 从校方数据库读取到本地对应数据库 ]]
function insert_data(tablename)
        local ret = nil
        local values = ""
        local school_table = string.sub(tablename,4,string.len(tablename))
        local mysql_fields = get_fields(tablename)
        ret,res = pcall(school_con.execute,school_con,string.format("select * from %s",school_table))
        if(not ret) then
                error_log("select data from school table")
                return nil
        end
        row = res:fetch ({}, "a")
        if row then
                _,ret = pcall(mysql_con.execute,mysql_con,string.format("delete from %s",tablename))
                if(not ret) then
                        error_log("delete data error,tablename:"..tablename)
                end
                while row do
                        local fields = mysql_fields
                        for k,v in pairs(row) do
                                k = string.lower(k)
                                print("1.v:"..v..":"..k)
                                if(config["school_db_charset"] ==  "GBK") then v = gbk_to_utf8(v) end
                                v = string.format("\"%s\"",v)
                                print("2.v:"..v)
                                fields = set_fields(fields,k,v)
                        end
                        print(tablename..":"..fields)
                        local insert_str = string.format("insert into %s values (%s)",tablename,fields)
                        print(insert_str)
                        _,ret = pcall(mysql_con.execute,mysql_con,insert_str)
                        if(not ret) then error_log("insert data error,insert sql:"..insert_str) end
                        row = res:fetch (row, "a")
                end
        else
                error_log("No data in school database resource")
        end
        return "ok"
end

--[[ 拉数据到本地数据库 ]]
function pull_data()
        get_tablenames()
        local ret = nil
        _,ret = pcall(mysql_con.execute,mysql_con,"begin")
        if(not ret) then 
                error_log("execute begin transaction")
                return nil
        end
        for k,v in ipairs(school_db_tablename) do
                local ret = insert_data(v)
                if(ret == nil) then 
                        error_log("pull data error,sql:"..v)
                        ret = pcall(mysql_con.execute, mysql_con,"rollback")
                        if(ret == nil) then error_log("execute rollback error") end
                        return nil
                end
        end
        _,ret = pcall(mysql_con.execute,mysql_con,"commit")
        if(not ret) then 
                error_log("execute commit error")
                mysql_con:execute("rollback")
                return nil
        end
        debug_log("create tables success!")
        return "ok"
end

--[[ 回收资源 ]]-- 
function pull_finalize()
        if(res ~= nil) then
                res:close()
        end
        if(mysql_con ~= nil) then
                mysql_con:close()
        end
        if(env_odbc ~= nil) then
                env_odbc:close()
        end
        if(con ~= nil) then
                con:close()
        end
        if(env ~= nil) then
                env:close()
        end
end

--[[ ###接口函数:odi_interface.lua调用 ### ]]-- 
function pull_run(conf)
        config = conf
        local ret = nil
        print("call pull_run(),mysql_db_host:"..config["mysql_db_host"])
        ret =  pull_init()
        if(ret == nil) then 
                error_log("call pull_init(),error") 
                return nil 
        end
        ret = create_tables()
        if(ret == nil) then 
                error_log("call create_tables(),error") 
                pull_finalize()
                return nil 
        end
        ret = pull_data()
        if(ret == nil) then 
                error_log("call pull_data(),error") 
                pull_finalize()
                return nil 
        end
        pull_finalize()
end

