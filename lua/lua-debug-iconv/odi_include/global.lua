-----------------------------------------------------------------------------
-- global info
-----------------------------------------------------------------------------

package.path = package.path..";/usr/local/whistle/openfire/resources/auth/lua/odi_include/?.lua"
package.cpath = package.cpath..";/usr/lib64/lua/5.1/?.so;/usr/lib64/lua/5.1/luasql/?.so"

local math = require('math')
local string = require("string")
local table = require("table")
local os = require("os")
local io = require("io")
local base = _G

local charset = require "iconv"
local utf8 = charset.new("utf8", "gbk")
local gbk = charset.new("gbk", "utf8")

-----------------------------------------------------------------------------
-- Module declaration
-----------------------------------------------------------------------------
module("global")

--global variables
root_path = "/usr/local/whistle/openfire/resources/auth/lua/"
log_level = 1 --[[1:error  2:debug]]
get_insertdata_path = root_path.."get_insertdata.txt"
get_deletedata_path = root_path.."get_deletedata.txt"
get_updatedata_path = root_path.."get_updatedata.txt"

local function log_write(log_file,log_str,log_lvl)
        if(log_lvl > 0 and log_lvl <= log_level) then
                local tab = base.os.date("*t")
                local lua_log_time = string.format("["..tab.year.."-"..tab.month.."-"..tab.day.." "..tab.hour..":"..tab.min..":"..tab.sec.."]".."lua_log:")
                local log_file = base.io.open(log_file,"a")
                log_file:write(lua_log_time..log_str.."\n")
                log_file:close()
        end
end

--write error log to file
--no return value
--@para log_file: file name
--@para log: log info
function error_log(log_file,log)
        log_write(log_file,log,1)
end

--write debug log to file
--no return value
--@para log_file: file name
--@para log: log info
function debug_log(log_file,log)
        log_write(log_file,log,2)
end

--parse src to table by mark
--return a table
--@para src: string text 
--@para mark: boundary sign
function parameter_parser(src,mark)
        local result = {}
        while true do
                local i = string.find(src,mark)
                if(i == nil) then break end
                local name = string.sub(src,0,i-1)
                table.insert(result,name)
                src = string.sub(src,i+1)
        end
        table.insert(result,src)
        return result
end

--trim blank at string
--return: string text
--@para s: string text
function string_trim(s)
        assert(type(s)=="string")
        return s:match("^%s+(.-)%s+$")
end

--convert utf8 string to gbk
--return a gbk string text
--@para value: a utf8 string text
function utf8_to_gbk(value)
        if value then
                local ret, error = gbk:iconv(value)
                if error then
                        return ""
                else
                        return ret
                end
        else
                return ""
        end
end

--convert gbk string to utf8
--return a utf8 string text
--@para value: a gbk string text
function gbk_to_utf8(value)
        if value then
                local ret, error = utf8:iconv(value)
                if error then
                        return ""
                else
                        return ret
                end
        else
                return ""
        end
end

