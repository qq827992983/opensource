-- lua script to load mysql data in to openfire database
-- author: liuning
-- date:   2014-01-17
-----------------------------
--include json openfire lib
-----------------------------
package.path = package.path..";/usr/local/whistle/openfire/resources/auth/lua/odi_include/?.lua"
package.cpath = package.cpath..";/usr/lib64/lua/5.1/?.so;/usr/lib64/lua/5.1/luasql/?.so"
require("base64")
local json = require("json")
local global = require("global")
local pull_data = require("pull_data")
local deal_data = require("deal_data")
local config = nil

------------------------------------------------------------------------------------------------------
--odi_interface.log
local odi_interface_log_path = "/usr/local/whistle/openfire/resources/auth/lua/log/odi_interface.log"
local get_insertdata_path = "/usr/local/whistle/openfire/resources/auth/lua/get_insertdata.txt"
local get_deletedata_path = "/usr/local/whistle/openfire/resources/auth/lua/get_deletedata.txt"
local get_updatedata_path = "/usr/local/whistle/openfire/resources/auth/lua/get_updatedata.txt"
local html_path = "/usr/local/whistle/openfire/resources/auth/lua/index.html"

-----------------------------
--clean log
-----------------------------
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
--init plug
-----------------------------
function initialize(conf)
        return '{"ret": 0,"errcode": 0,"msg": "ok","data": {"mode": "timer","interval":"3600","threadsafe":"true"}}'
end

-----------------------------
--uninstall plug
-----------------------------
function finalize()
        log_write("unload plugin")
end

-----------------------------
--start plug
-----------------------------
function start(conf)
        log_write("start conf = "..conf);
        return '{"ret":0,"errcode":0,"msg":"ok"}'
end

-----------------------------
--stop service
-----------------------------
function stop()
        return '{"ret":0,"errcode":0,"msg":"ok"}'
end

function findStr(data,field)
        i,j = string.find(data, field..'":"')
        if(i ~= nil and i >0 )	then
                k,p = string.find(data, '"',1+j)
                value = string.sub(data,j+1,p-1)
                return value
        end
        log_write(string.format("findStr(),error,data:%s,field:%s",data,field))
        return nil
end

-----------------------------
--table to json
-----------------------------
function table2json(tableData)
        local jData = json.encode(tableData)
        if(jData == nil) then log_write("json.encode error,tableData:"..tableData) end
        return jData
end

-----------------------------------
--orgnization tree table operate
-----------------------------------
-----------------------------
--查找组织节点是否存在
-----------------------------
function find_orgnization(data)
        local orgRet = AuthPluginProvider:getLocalID(data)
        i,j = string.find(orgRet,'"ret":')
        local retResult = string.sub(orgRet,j+1,j+1)
        if(retResult == "0") then
                --如果存在组织节点就返回节点id
                local orgid = findStr(orgRet,"id")
                return orgid
        else
                log_write("find_orgnization error,orgRet:"..orgRet)
                return nil
        end
end

-----------------------------
--add orgnization
-----------------------------
function add_orgnization(data)
        local orgInsertRet = AuthPluginProvider:insertOrganization(data)
        i,j = string.find(orgInsertRet,'"ret":')
        local retResult = string.sub(orgInsertRet,j+1,j+1)
        if(retResult == "0") then
                return retResult
        else
                return orgInsertRet
        end
end

---------------------------------------
----构造增加组织节点参数
---------------------------------------
function org2json_for_add_org(name,parent,remote_orgid)
        local org_table = {}
        local org_t = {}
        local org_items = {}

        org_table["name"] = name
        org_table["parent"] = parent
        org_table["remote_orgid"] = remote_orgid
        org_table["status"] = "1"

        table.insert(org_t,org_table)
        org_items["items"] = org_t
        local org_data = table2json(org_items)
        return org_data
end


----------------------------------
--insert member into org tree
----------------------------------
function insert_member2org(data)
        local orgInsertRet = AuthPluginProvider:insertOrganizationMember(data)
        i,j = string.find(orgInsertRet,'"ret":')
        local retResult = string.sub(orgInsertRet,j+1,j+1)
        if(retResult == "0") then
                return orgInsertRet
        else
                log_write("insert_member2org(),error,orgInsertRet:"..orgInsertRet)
                return nil
        end
end

--------------------------------------
--创建组织结构表用于进行组织结构查找
--------------------------------------
function org_table_create(data)
        local org_table = {}
        local org_1st_table = {}
        local org_2nd_table = {}
        local org_3rd_table = {}
        local org_4th_table = {}
        local org_5th_table = {}
        local insert_falg = true
        if(data["oid1"] ~= "" and data["oname1"] ~= "") then
                table.insert(org_1st_table,data["oid1"])
                table.insert(org_1st_table,data["oname1"])
                table.insert(org_table,org_1st_table)
        else
                insert_falg = false
        end


        if(data["oid2"] ~= "" and data["oname2"] ~= "" and insert_falg) then
                table.insert(org_2nd_table,data["oid2"])
                table.insert(org_2nd_table,data["oname2"])
                table.insert(org_table,org_2nd_table)
        else
                insert_falg = false
        end

        if(data["oid3"] ~= "" and data["oname3"] ~= "" and insert_falg) then
                table.insert(org_3rd_table,data["oid3"])
                table.insert(org_3rd_table,data["oname3"])
                table.insert(org_table,org_3rd_table)
        else
                insert_falg = false
        end

        if(data["oid4"] ~= "" and data["oname4"] ~= "" and insert_falg) then
                table.insert(org_4th_table,data["oid4"])
                table.insert(org_4th_table,data["oname4"])
                table.insert(org_table,org_4th_table)
        else
                insert_falg = false
        end

        if(data["oid5"] ~= "" and data["oname5"] ~= "" and insert_falg) then
                table.insert(org_5th_table,data["oid5"])
                table.insert(org_5th_table,data["oname5"])
                table.insert(org_table,org_5th_table)
        else
                insert_falg = false
        end
        return org_table
end

--------------------------
--构造查找组织节点的参数
--------------------------
function org2json(data)
        local org_table = {}
        local org_t = {}
        local org_items = {}
        org_table["remote_id"] = data[1]
        org_table["type"] = "organization"
        table.insert(org_t,org_table)
        org_items["items"] = org_t
        local org_data = table2json(org_items)
        return org_data
end

--------------------------
--构造插入组织的节点参数
--------------------------
function org2json_for_insert(name,id)
        local org_table = {}
        local org_t = {}
        local org_items = {}
        org_table["username"] = name
        org_table["orgid"] = id
        table.insert(org_t,org_table)
        org_items["items"] = org_t
        local org_data = table2json(org_items)
        return org_data
end

-----------------------------
--find_username
-----------------------------
function find_username(findData,findKey)
        local ret = AuthPluginProvider:findUser(findData)
        local pos = string.find(ret, findKey)
        if(pos ~= nil and pos > 0) then
                local username = findStr(ret,"username")
                return username
        end
        return nil
end

----------------------------------
--查找aid
----------------------------------
function get_aid(data)
        local student_number = data["ora_uid"]
        local findData = '{"items":[{"student_number":"'..student_number..'"}]}'
        local ret = AuthPluginProvider:findUser(findData)
        local pos = string.find(ret, "student_number")
        if(pos ~= nil and pos > 0) then
                local aid = findStr(ret,"aid")
                return aid
        end
        log_write("get_aid(),error")
        return nil
end

----------------------------------
--获取 username
----------------------------------
function get_username(data)
        local student_number = data["ora_uid"]
        local findData = '{"items":[{"student_number":"'..student_number..'"}]}'
        local username = find_username(findData,"student_number")
        return username
end

--------------------------
--构造查找组织的节点id参数
--------------------------
function org2json_for_find_orgid(name)
        local org_table = {}
        local org_t = {}
        local org_items = {}
        org_table["username"] = name
        table.insert(org_t,org_table)
        org_items["items"] = org_t
        local org_data = table2json(org_items)
        return org_data
end

------------------------
--查找成员组织orgid
------------------------
function find_orgnization_member(username)
        local orgid_param = org2json_for_find_orgid(username)
        if(orgid_parm == nil) then 
                log_write("call org2json_for_find_orgid(),error,username:"..username)
                return nil 
        end
        local orgidRet = AuthPluginProvider:findOrganizationMember(orgid_param)
        i,j = string.find(orgidRet,'"ret":')
        local retResult = string.sub(orgidRet,j+1,j+1)
        if(retResult == "0") then
                local orgid = findStr(orgidRet,"orgid")
                if(orgid ~= nil) then
                        return orgidRet
                else
                        return nil
                end
        else
                return nil
        end
end

--------------------------------------
--根据成员信息插入到相应的组织节点下
--------------------------------------
function traverse_org(data)
        local ret = 0
        --创建组织结构table
        local org_table_data = org_table_create(data)

        --获取组织节点级数
        local length = table.getn(org_table_data)
        --逐级查找组织节点
        for i = 1,length do
                --构造查找组织是否存在的参数
                local org_data = org2json(org_table_data[i])
                if(org_data == nil) then
                        ret = 1 
                end
                --调用查找函数
                local find_result = find_orgnization(org_data)
                if(find_result ~= nil) then
                        --找到了并且是最后一级则进行插入成员
                        if(i == length) then
                                --获取成员用户名(org_username)
                                local org_username = get_username(data)
                                if(org_username == nil) then
                                        log_write("traverse_org: org is exist call function get_username and the org_username is ----> nil")
                                        break
                                end
                                --查找成员是否存在
                                local find_org_insert = find_orgnization_member(org_username)
                                if(find_org_insert ~= nil) then
                                        log_write("traverse_org: org is exist call function find_orgnization_member to find the member if is exist and the result is ----> "..find_org_insert)
                                        break
                                else
                                        --构造插入的成员数据
                                        local insert_org_data = org2json_for_insert(org_username,find_result)
                                        if(insert_org_data == nil) then
                                                log_write("traverse_org: org is exist call function org2json_for_insert to create insert param and the param is  ----> "..insert_org_data)
                                        end

                                        --插入成员
                                        local insert_result = insert_member2org(insert_org_data)
                                        if(insert_result ~= nil) then
                                                log_write("traverse_org: org is exist call function insert_member2org insert someone into org tree ----> success")
                                        else
                                                log_write("traverse_org: org is exist call function insert_member2org insert someone into org tree ----> failed")
                                                ret = 2
                                                break
                                        end
                                        break
                                end
                        end
                else --没找到就创建组织
                        log_write("traverse_org: org not exist call function find_orgnization and the found result is ----> org is not exist we should create")
                        local name = org_table_data[i][2]
                        local parent
                        local remote_orgid = org_table_data[i][1]
                        if(i == 1) then
                                parent = 1
                        else
                                --构造查找组织是否存在的参数
                                local org_data_parent = org2json(org_table_data[i - 1])
                                if(org_data_parent == nil) then
                                        ret = 7
                                        --break
                                end
                                --调用查找函数
                                parent = find_orgnization(org_data_parent)
                                if(parent == nil) then
                                        log_write("traverse_org: org not exist call function find_orgnization to find parent and the parent is ----> nil")
                                        ret = 3
                                        break
                                end
                        end
                        --构造创建组织节点数据
                        local add_member_data = org2json_for_add_org(name,parent,remote_orgid)
                        --创建组织
                        local add_result = add_orgnization(add_member_data)
                        if(add_result == "0") then
                                log_write("traverse_org: org not exist add org tree ----> success")
                                --增加组织节点成功后如果是最后一级则将成员加入其中
                                if(i == length) then
                                        --插入成员
                                        local org_username = get_username(data)
                                        if(org_username ~= nil) then
                                        else
                                                log_write("traverse_org: org not exist call function get_username to find org_username and the org_username is ----> nil")
                                                ret = 4
                                                break
                                        end

                                        local org_self = find_orgnization(org_data)
                                        if(org_self ~= nil) then
                                        else
                                                log_write("traverse_org: org not exist call function find_orgnization to find org_self and the org_self is ----> nil")
                                                ret = 5
                                                break
                                        end

                                        local insert_org_data = org2json_for_insert(org_username,org_self)
                                        local insert_result = insert_member2org(insert_org_data)
                                        if(insert_result ~= nil) then
                                                log_write("traverse_org: org not exist create org and call function insert_member2org insert someone into org tree ----> success")
                                        else
                                                log_write("traverse_org: org not exist create org and call function insert_member2org insert someone into org tree ----> failed")
                                                ret = 6
                                                break
                                        end
                                        break
                                end
                        else
                                log_write("traverse_org: org not exist add org tree failed and result is ----> "..add_result)
                                break
                        end
                end
        end
        return ret
end

--------------------------
--构造插入组织的节点参数
--------------------------
function org2json_for_org_delete(name,id)
        local org_table = {}
        local org_t = {}
        local org_items = {}
        org_table["username"] = name
        org_table["orgid"] = id
        table.insert(org_t,org_table)
        org_items["items"] = org_t
        local org_data = table2json(org_items)
        return org_data
end

----------------------
--查找成员组织orgid
----------------------
function find_orgnization_member_data(data)
        local username = get_username(data)
        local orgid_param = org2json_for_find_orgid(username)
        local orgidRet = AuthPluginProvider:findOrganizationMember(orgid_param)
        i,j = string.find(orgidRet,'"ret":')
        if(i == nil or j ==nil) then 
                log_write("cann't find ret value in data")
        end
        local retResult = string.sub(orgidRet,j+1,j+1)
        if(retResult == "0") then
                local orgid = findStr(orgidRet,"orgid")
                if(orgid ~= nil) then
                        local org_delete_data = org2json_for_org_delete(username,orgid)
                        return org_delete_data
                else
                        return nil
                end
        else
                log_write("cann't find orgid in orgidRet,"..orgidRet)
                return nil
        end
end

----------------
--删除组织成员
----------------
function delete_orgnization_member(data)
        --查找要删除的成员信息
        local delete_data = find_orgnization_member_data(data)
        if(delete_data ~= nil) then
                local orgRet = AuthPluginProvider:deleteOrganizationMember(delete_data)
                i,j = string.find(orgRet,'"ret":')
                local retResult = string.sub(orgRet,j+1,j+1)
                if(retResult == "0") then
                        log_write("delete_orgnization_member: member is delete ----> success")
                else
                        log_write("delete_orgnization_member: member is delete ----> failed")
                        return nil
                end
        else
                log_write("delete_orgnization_member:  call function find_orgnization_member_data the member is already not exist in org tree")
                return nil
        end
end

--------------------------
----构造更新组织节点参数
--------------------------
function org2json_for_update_org(name)
        local org_table = {}
        local org_t = {}
        local org_items = {}
        org_table["username"] = name
        table.insert(org_t,org_table)
        org_items["items"] = org_t
        local org_data = table2json(org_items)
        return org_data
end

--------------------------
--删除原来的所以组织结构
--------------------------
function orgtable2json_for_update(data)
        local username = get_username(data)
        if(username ~= nil) then
                local update_delete_all = org2json_for_update_org(username)
                if(update_delete_all == nil) then
                        log_write("call org2json_for_update_org(),error,"..update_delete_all)
                        return nil
                end
                local orgRet = AuthPluginProvider:deleteOrganizationMember(update_delete_all)
                i,j = string.find(orgRet,'"ret":')
                local retResult = string.sub(orgRet,j+1,j+1)
                if(retResult == "0") then
                        return orgRet
                else
                        log_write("get ret error!")
                        return nil
                end
        else
                log_write("username is not exist!")
                return nil
        end
end

----------------
--更新组织成员
----------------
function update_orgnization_member(data)
        --删除原有的组织结构树
        local update_data = orgtable2json_for_update(data)
        if(update_data ~= nil) then
                --插入新的组织结构
                if(traverse_org(data) > 0) then log_write("traverse_org,error") end
        else
                log_write("update_orgnization_member: update org ----> failed")
        end
end

-------------------------------
--login 表操作 账户的增删改查
-------------------------------
-------------------------------
--构造插入账户参数
-------------------------------
function account_param_create(data)
        local org_table = {}
        local org_t = {}
        local org_items = {}
        org_table["account"] = data["ora_uid"]

        local username = get_username(data)
        if( username ~= nil) then
                org_table["username"] = username
        else
                return nil
        end

        local aid = get_aid(data)
        if(aid ~= nil) then
                org_table["aid"] = aid
        else
                return nil
        end

        table.insert(org_t,org_table)
        org_items["items"] = org_t
        local org_data = table2json(org_items)
        return org_data
end

-------------------------------
--构造账户参数
-------------------------------
function create_account_param(data)
        local org_table = {}
        local org_t = {}
        local org_items = {}
        org_table["account"] = data["ora_uid"]
        table.insert(org_t,org_table)
        org_items["items"] = org_t
        local org_data = table2json(org_items)
        return org_data
end

-----------------------------
--查找账号
-----------------------------
function find_account(data)
        local find_account_data = create_account_param(data)
        log_write("find_account : call function create_account_param and the result is ----> "..find_account_data)
        local find_account_result = AuthPluginProvider:findAccount(find_account_data)
        i,j = string.find(find_account_result,'"ret":')
        local retResult = string.sub(find_account_result,j+1,j+1)
        if(retResult == "0") then
                local account = findStr(find_account_result,"account")
                return account
        else
                return nil
        end
end

-------------------------------
--增加账号
-------------------------------
function insert_account(data)
        local find_account_result = find_account(data)
        if(find_account_result ~= nil) then
                log_write("insert_account : the account is already ----> exist")
                return
        else
                --构造插入账户参数
                local insert_account_data = account_param_create(data)
                if(insert_account_data ~= nil) then
                        local insert_account_result = AuthPluginProvider:insertAccount(insert_account_data)
                        i,j = string.find(insert_account_result,'"ret":')
                        local retResult = string.sub(insert_account_result,j+1,j+1)
                        if(retResult == "0") then
                                log_write("insert_account : call function AuthPluginProvider:insertAccount insert account ----> success")
                        else
                                log_write("insert_account : call function AuthPluginProvider:insertAccount insert account failed ----> "..insert_account_result)
                                return nil
                        end
                else
                        log_write("insert_account : call function account_param_create and  insert_account_data is nil ----> insert failed")
                        return nil
                end
        end
        return "ok"
end

-------------------------------
--删除账号
-------------------------------
function delete_account(data)
        local find_account_result = find_account(data)
        if(find_account_result ~= nil) then
                --构造插入账户参数
                local delete_account_data = create_account_param(data)
                log_write("delete_account : call function create_account_param and the result is ----> "..delete_account_data)
                local delte_account_result = AuthPluginProvider:deleteAccount(find_account_result)
                i,j = string.find(delte_account_result,'"ret":')
                local retResult = string.sub(delte_account_result,j+1,j+1)
                if(retResult == "0") then
                        log_write("delete_account : call function AuthPluginProvider:deleteAccount delete account ----> success")
                else
                        log_write("delete_account : call function AuthPluginProvider:deleteAccount delete accoun failed ----> "..delte_account_result)
                        return nil
                end
        else
                log_write("delete_account : the account is not exist")
                return nil
        end
end

-----------------------------
--user data table operate----
-----------------------------
-----------------------------
--find_data
-----------------------------
function find_data(findData)
        local ret = AuthPluginProvider:findUser(findData)
        return ret
end

---------------
--insert_data
---------------
function insert_data(insertData)
        local insertRet = AuthPluginProvider:insertUser(insertData)
        i,j = string.find(insertRet,'"ret":')
        local retResult = string.sub(insertRet,j+1,j+1)
        return retResult
end

-----------------------------
--update_data
-----------------------------
function update_data(updateData)
        local updateRet = AuthPluginProvider:updateUser(updateData)
        i,j = string.find(updateRet,'"ret":')
        local retResult = string.sub(updateRet,j+1,j+1)
        return retResult
end

-----------------------------
--delete_data
-----------------------------
function delete_data(deleteData)
        local deleteRet = AuthPluginProvider:deleteUser(deleteData)
        i,j = string.find(deleteRet,'"ret":')
        local retResult = string.sub(deleteRet,j+1,j+1)
        return retResult
end

---------------------------------
--delete
---------------------------------
function delete(data)
        local delete_table = {}
        local delete_items = {}
        local delete_t = {}
        delete_table["student_number"] = data["ora_uid"]
        table.insert(delete_t,delete_table)
        delete_items["items"] = delete_t
        local delete_find_data = table2json(delete_items)
        if(delete_find_data == nil) then return nil end
        --find if exist user
        delete_username = find_username(delete_find_data,"student_number")
        if(delete_username ~= nil)	then
                --delete data
                local deleteData = '{"items":[{"username":"'..delete_username..'"}]}'
                deleteResult = delete_data(deleteData)
                if(deleteResult == "0") then
                        log_write(string.format("delete success"))
                else
                        log_write(string.format("delete failed"))
                        return nil
                end
        end
        return "ok"
end

---------------------------------
--去除字符串两端空格
---------------------------------
function string_trim(s)
        assert(type(s)=="string")
        return s:match("^%s+(.-)%s+$")
end

---------------------------------
--根据identify，生成role
---------------------------------
function get_info(identity_info)
        string_trim(identity_info)
        if(identity_info=="学生" or identity_info=="student" ) then
                return "1";
        elseif(identity_info=="老师" or identity_info=="teacher") then
                return "2";
        else
                return "3";
        end
end

---------------------------------
--生成用户的role
---------------------------------
function get_user_role(data)
        local i = 0
        local j = 0
        local sub_data = ""
        local return_str = ""
        local str;

        while i ~=nill and j~=nill do

                i, j = string.find(data, ",")
                if(i ==nil or j == nil) then
                        break;
                end
                sub_data = string.sub(data, 0, i-1)
                str = get_info(sub_data)
                if(return_str == "") then
                        return_str = str
                else
                        return_str = string.format("%s,%s",return_str,str)
                end
                data = string.sub(data,j+1)
        end

        if(data ~= nil and string.len(data) >0) then
                str = get_info(data)
                if(return_str == "") then
                        return_str = str
                else
                        return_str = string.format("%s,%s",return_str,str)
                end
                return return_str
        else
                return get_info(data)
        end
end

---------------------------------
--insert
---------------------------------
function insert(data)
        local insert_table = {}
        local insert_t = {}
        local insert_items = {}
        insert_table["remote_id"] = data["ora_uid"]
        insert_table["card_number"] = data["card_number"]
        insert_table["card_type"] = data["card_type"]
        insert_table["name"] = data["name"]
        insert_table["sex"] = data["sex"]
        insert_table["student_number"] = data["ora_uid"]
        insert_table["cellphone"] = data["cellphone"]
        insert_table["email"] = data["email"]
        insert_table["identity"] = data["identity"]
        insert_table["role"] = get_user_role(data["identity"])
        insert_table["landline"] = ""
        insert_table["nativeplace_city"] = data["nativeplace_city"]
        insert_table["nativeplace_district"] = data["nativeplace_district"]
        insert_table["nativeplace_nation"] = data["nativeplace_nation"]
        insert_table["card_number"] = data["card_number"]
        insert_table["nativeplace_province"] = data["nativeplace_province"]
        insert_table["title"] = data["title"]
        table.insert(insert_t,insert_table)
        insert_items["items"] = insert_t
        local insert_find_data = table2json(insert_items)
        if(insert_find_data == nil) then return nil end
        -- find if exist user
        local insert_student_number = data["ora_uid"]
        local insertFindData = '{"items":[{"student_number":"'..insert_student_number..'"}]}'
        local insert_username = find_username(insertFindData,"student_number")
        if(insert_username ~= nil) then
                log_write("insert : call function find_username and the result is ----> user is already exist")
        else
                --insert data
                local insertResult = insert_data(insert_find_data)
                if(insertResult == "0") then
                        log_write(string.format("insert : call function insert_data insert someone into ofuser table and the resulet is ----> insert success"))
                else
                        log_write(string.format("insert : call function insert_data insert someone into ofuser table and the resulet is ----> insert failed"))
                        return nil
                end
        end
        return "ok"
end

---------------------------------
--update
---------------------------------
function update(data)
        local update_table = {}
        local update_t = {}
        local update_items = {}
        update_table["remote_id"] = data["ora_uid"]
        update_table["card_number"] = data["card_number"]
        update_table["card_type"] = data["card_type"]
        update_table["name"] = data["name"]
        update_table["sex"] = data["sex"]
        update_table["student_number"] = data["ora_uid"]
        update_table["cellphone"] = data["cellphone"]
        update_table["email"] = data["email"]
        update_table["identity"] = data["identity"]
        update_table["role"] = get_user_role(data["identity"])
        update_table["landline"] = ""
        update_table["nativeplace_city"] = data["nativeplace_city"]
        update_table["nativeplace_district"] = data["nativeplace_district"]
        update_table["nativeplace_nation"] = data["nativeplace_nation"]
        update_table["card_number"] = data["card_number"]
        update_table["nativeplace_province"] = data["nativeplace_province"]
        update_table["title"] = data["title"]
        --find if exist user
        local update_student_number = data["ora_uid"]
        local updateFindData = '{"items":[{"student_number":"'..update_student_number..'"}]}'
        local update_username = find_username(updateFindData,"student_number")
        if(update_username ~= nil) then
                update_table["username"] = update_username
                table.insert(update_t,update_table)
                update_items["items"] = update_t
                update_find_data = table2json(update_items)
                if(update_find_data == nil) then return nil end
                --update data
                local updateResult = update_data(update_find_data)
                if(updateResult == "0")	then
                        log_write("update : call function update to update and the result is success")
                else
                        log_write("update : call function update to update and the result is failed")
                        return nil
                end
        else
                log_write("update : user is not exist")
                return nil
        end
        return "ok"
end

function process()
        --delete
        local delete_file = io.open(get_deletedata_path,"rb")
        if(delete_file) then
                for delete_line in delete_file:lines() do
                        if(delete_line ~= "") then
                                local tData = json.decode(delete_line)
                                if(tData ~= nil) then
                                        if(delete(tData) ~= nil) then
                                                if(delete_orgnization_member(tData) ~= nil) then
                                                        if(delete_account(tData) == nil) then log_write("call delete_account(),error,"..delete_line) end
                                                else
                                                        log_write("call delete_orgnization_member(),error,"..delete_line)
                                                end
                                        else
                                                log_write("call delete(),error,"..delete_line)
                                        end
                                else
                                        log_write("call json.decode(),error,"..delete_line)
                                end
                        end
                end
                delete_file:close()
        end

        --insert
        local insert_file = io.open(get_insertdata_path,"rb")
        if(insert_file) then
                for insert_line in insert_file:lines() do
                        if(insert_line ~= "") then
                                local tData = json.decode(insert_line)
                                if(tData ~= nil) then
                                        if(insert(tData) ~= nil) then
                                                if(traverse_org(tData) == 0) then 
                                                        if(insert_account(tData) == nil) then log_write("call insert_account(),error") end
                                                else
                                                        log_write("call traverse_org(),error,"..insert_line)
                                                end
                                        else
                                                log_write("call insert(),error,"..insert_line)
                                        end
                                else
                                        log_write("call json.decode(),error,"..insert_line)
                                end
                        end
                end
                insert_file:close()
        end

        --update
        local update_file = io.open(get_updatedata_path,"rb")
        if(update_file) then
                for update_line in update_file:lines() do
                        if(update_line ~= "") then
                                local tData = json.decode(update_line)
                                if(tData ~= nil) then
                                        if(update(tData) ~= nil) then 
                                                if(update_orgnization_member(tData) == nil) then log_write("call update_orgnization_member(),error")
                                                else
                                                        log_write("call update(),error,"..update_line)
                                                end
                                        end
                                end
                        end
                        update_file:close()
                end
        end
end
local dbg_conf = '{"sync_mode":"pull","ontimer_begin":10,"ontimer_period":"3600","mysql_db_version":"5","mysql_db_host":"192.168.254.100","mysql_db_port":"3306","mysql_db_username":"odi","mysql_db_userpwd":"odiodi","mysql_db_name":"odi_data_sync","school_db_type":"oracle","school_db_username":"school_db_username","school_db_userpwd":"school_db_userpwd","school_db_host":"school_db_host","school_db_port":"school_db_port","school_db_servername": "oracle11","school_db_name": "school_db_name","school_db_charset": "GBK","school_odbc_name": "oracle11","school_db_sqls": "create table if not exists ds_V_SAM_USER(xm VARCHAR(60),xh VARCHAR(20) not null,xbdm VARCHAR(1),sf VARCHAR(9),zw VARCHAR(100),dzxx VARCHAR(100),sjh VARCHAR(40),lxdh VARCHAR(300),txyb VARCHAR(6),sfzjlxdm VARCHAR(1),sfzjh VARCHAR(60),jgdm VARCHAR(6),txdz VARCHAR(620),whcd CHAR(1),gzz CHAR(18),yxsh VARCHAR(20),yxmc VARCHAR(300),zydm VARCHAR(20),zymc VARCHAR(90),bjdm VARCHAR(300),bjmc VARCHAR(300))","user_data_sql": "create or replace view user_data as select xh as ora_uid, \\\"1\\\" as card_type, sfzjh as card_number, sf as identity, xm as name, xbdm as sex, NULL as birthday, NULL as blood_type,NULL as nativeplace_nation,NULL as nativeplace_province, NULL as nativeplace_city,NULL as nativeplace_district, NULL as address_nation, NULL as address_province, NULL as address_city, NULL as address_district, NULL as address_extend, NULL as address_postcode,dzxx as email, NULL as landline, sjh as cellphone, xh as student_number, yxsh as 1st_orgnizationid, yxmc as 1st_orgnizationname,zydm as 2nd_orgnizationid,zymc as 2nd_orgnizationname, bjdm as 3rd_orgnizationid, bjmc as 3rd_orgnizationname, NULL as 4th_orgnizationid, NULL as 4th_orgnizationname, NULL as 5th_orgnizationid, NULL as 5th_orgnizationname,NULL as title from ds_V_SAM_USER"}'
-----------------------------
--openfir call onTimer
-----------------------------
function onTimer(conf)
        local ret = nil

        conf = dbg_conf
        print("onTimer conf:"..conf)
        ret,config = pcall(json.decode, conf)
        if(not ret) then
                log_write("parameter conf is invalid!")
                return 1
        end
        print("ontimer_begin = "..config["ontimer_begin"])
        local tab = os.date("*t")
        if(tab.hour >= config["ontimer_begin"] and tab.hour < config["ontimer_begin"] + 1) then
        --if(tab.hour >= 0) then
                if(config["sync_mode"] == "pull") then
                        log_write("ontimer_time : get data from custom database the style of get data is pull")
                        _,ret = pcall(pull_data.pull_run,config)
                        if(not ret) then 
                                log_write("first call pull_run(),error")
                                ret = pcall(pull_data.pull_run,config)
                                if(not ret) then log_write("secode call pull_run(),error"); return 3 end
                        end
                        log_write("firist pull")
                else
                        log_write("ontimer_time : the style of get data is push")
                end
                ret = pcall(deal_data.deal_run,config)
                if(not ret) then log_write("first call deal_run(),error")
                        ret = pcall(deal_data.deal_run,config)
                        if(not ret) then log_write("second call deal_run(),error"); return 5 end
                end 
                ret = pcall(process)
                if(not ret) then 
                        log_write("call process(),error")
                        ret = pcall(process())
                        if(not ret) then log_write("call process(),error"); return 6 end
                end
        else
                return 0
        end
        return 0
end

local g_plugin_config ='[{"key":"sync_mode","label":"数据同步模式:", "component": "select","value":"sync_mode_value","attr":[{"key":"pull","label":"推"},{"key":"push","label":"拉"}]},{"key": "ontimer_begin","label": "数据同步时间:","component": "text", "value": "ontimer_begin_value"},{"key": "sync_period","label": "数据同步周期:","component": "text", "value": "sync_period_value"},{"key": "mysql_db_version","label": "MySql版本:","component": "text", "value": "mysql_db_version_value"},{"key": "mysql_db_host","label": "MySql主机名:","component": "text", "value": "mysql_db_host_value"},{"key": "mysql_db_port","label": "MySql端口:","component": "text", "value": "mysql_db_port_value"},{"key": "mysql_db_username","label": "MySql用户名:","component": "text", "value": "mysql_db_username_value"},{"key": "mysql_db_userpwd","label": "MySql密码:","component": "text", "value": "mysql_db_userpwd_value"},{"key": "mysql_db_name","label": "MySql数据库名:","component": "text", "value": "mysql_db_name_value"},{"key": "school_db_type","label": "学校数据库类型:","component": "text", "value": "school_db_type_value"},{"key": "school_db_username","label": "学校数据库用户名:","component": "text", "value": "school_db_username_value"},{"key": "school_db_userpwd","label": "学校数据库密码:","component": "text", "value": "school_db_userpwd_value"},{"key": "school_db_host","label": "学校数据库主机名:","component": "text", "value": "school_db_host_value"},{"key": "school_db_port","label": "学校数据库端口:","component": "text", "value": "school_db_port_value"},{"key": "school_db_servername","label": "学校数据库服务名:","component": "text", "value": "school_db_servername_value"},{"key": "school_db_name","label": "学校数据库名称:","component": "text", "value": "school_db_name_value"},{"key": "school_db_charset","label": "学校数据库字符集:","component": "text", "value": "school_db_charset_value"},{"key": "school_odbc_name","label": "配置odbc名称:","component": "text", "value": "school_odbc_name_value"},{"key": "school_db_sqls","label": "学校数据库建表语句:","component": "text", "value": "school_db_sqls_value"},{"key": "user_data_sql","label": "user_data建视图语句:","component": "text", "value": "user_data_sql_value"},{"key": "test","label": "测试:","component": "button","value":"test_value"},{"key": "test","label": "重置:","component": "button","value":"reset_value"}]'	

-----------------------------
--get plug config
-----------------------------
function getPluginConfig(conf)
        print("getPluginConfig")
        local ret = nil
        local plugin_cfg = nil
        local plugin_config = g_plugin_config
        ret,plugin_cfg = pcall(json.decode, conf)
        if(not ret) then
                log_write("parameter conf is invalid!")
                return 1
        end

        for k,v in  pairs(plugin_cfg) do
                print("------------------------")
                print(k..":"..v)
               plugin_config = string.gsub(plugin_config,k.."_value",v)
        end
        print("plugin_config:"..plugin_config)
        local file = io.open(html_path,"r")
        local data = file:read("*all")
        file:close()
        local base64_html = to_base64(data)

        return '{"ret": 0,"errcode": 0,"errmsg": "ok","data": {"name": "datasync","alias":"datasync1","type":"datasync","mode":"lua","template":"config_table_tpl","config":'..plugin_config..'},"html":"'..base64_html..'"}'
end

function getPluginConfigItems()
        print("getPluginConfigItems")
        local ret = nil
        local plugin_cfg = nil
        local plugin_config = g_plugin_config
        local file = io.open(html_path,"r")
        local data = file:read("*all")
        file:close()
        local base64_html = to_base64(data)
        return '{"ret": 0,"errcode": 0,"errmsg": "ok","data": {"name": "datasync","alias":"datasync1","type":"datasync","mode":"lua","config":'..plugin_config..'},"html":"'..base64_html..'"}'
end

-----------------------------
--check username and passwd
-----------------------------
function verifyIdentity(conf,account,password)
        log_write("verifyIdentity:",conf,account,password);
        return '{"ret":0,"errcode":0,"msg":"ok"}'
end

-----------------------------
--load account info
-----------------------------
function loadAccount(conf,account)
        log_write("loadAccount:",conf,account);
        return '{"ret":0,"errcode":0,"msg":"operate success","data":[{"address":"beijing haidian","sex":"man","name":"zhangsan"},{"address":"beijing","sex":"woman","name":"xiaoming"}]}'
end

function check_env(conf)
        print("aaaaaaaaaaaaaaa")
        local ret = nil
        local cfg = nil
        ret,cfg = pcall(json.decode, dbg_conf)
        if(not ret) then
                log_write("parameter conf is invalid!")
                return 1
        end
        ret = pull_data.check_env(cfg)
        print("ret="..ret)
        if(ret == 0) then
                return '{"ret":0,"errcode":0,"msg":"ok"}'
        elseif(ret == 1) then
                return '{"ret":1,"errcode":1,"msg":"unknown database type"}'
        elseif(ret == 2) then
                return '{"ret":2,"errcode":2,"msg":"connect school error"}'
        elseif(ret == 3) then
                return '{"ret":3,"errcode":3,"msg":"connect mysql error"}'
        else
                return '{"ret":100,"errcode":100,"msg":"unknown error"}'
        end
end

function reset(conf)
        local ret
        local cfg
        ret,cfg= pcall(json.decode,conf)
        ret = deal_data.reset_db(cfg)
        print("ret:"..ret)
        if(ret == "ok") then
                return '{"ret":0,"errcode":0,"msg":"ok"}'
        else
                return '{"ret":-1,"errcode":1,"msg":"reset error"}'
        end
end
--print(check_env(dbg_conf))
--onTimer("111")
