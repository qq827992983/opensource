-- lua script to load mysql data in to openfire database
-- author: liuning
-- date:   2014-01-17
--
-----------------------------
--include json openfire lib
-----------------------------
 package.path = '/usr/local/whistle/openfire/resources/auth/lua/odi_include/?.lua'

-----------------------------
--json
-----------------------------
json = require('json')

--------------------
--get date time
--------------------
--tab = os.date("*t")
--lua_log_time = string.format("["..tab.year.."-"..tab.month.."-"..tab.day.." "..tab.hour..":"..tab.min..":"..tab.sec.."]".."lua_print:	")


-----------------------------------------------------
--推拉数据配置 is_pull = true 拉数据 false 推数据
-----------------------------------------------------
local is_pull = true

---------------------------------
--call onTimer time
---------------------------------
local onTimerBegin = 10

------------------------------------------------------------------------------------------------------
--file path odi_interface.log get_insertdata.txt get_deletedata.txt get_updatedata.txt db_compare.lua
------------------------------------------------------------------------------------------------------
--odi_interface.log
odi_interface_log_path = "/usr/local/whistle/openfire/resources/auth/lua/log/odi_interface.log"

--get_insertdata.txt
get_insertdata_path = "/usr/local/whistle/openfire/resources/auth/lua/get_insertdata.txt"

--get_deletedata.txt
get_deletedata_path = "/usr/local/whistle/openfire/resources/auth/lua/get_deletedata.txt"

--get_updatedata.txt
get_updatedata_path = "/usr/local/whistle/openfire/resources/auth/lua/get_updatedata.txt"

--operte school mysql lua path
school_lua_path = "lua /usr/local/whistle/openfire/resources/auth/lua/odi_include/wuhanfangzhi_xueyuan.lua"
daily_lua_path = "lua /usr/local/whistle/openfire/resources/auth/lua/odi_include/dailyPullData.lua"

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
function initialize()
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
function start()
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
	return nil
end

-----------------------------
--table to json
-----------------------------
function table2json(tableData)
	local jData = json.encode(tableData)
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
	--创建组织结构table
	log_write("traverse_org: the source data is ----> "..table2json(data))
	local org_table_data = org_table_create(data)
	log_write("traverse_org: call function org_table_create to create org_table and the table is ----> "..table2json(org_table_data))

	--获取组织节点级数
	local length = table.getn(org_table_data)
	--逐级查找组织节点
	for i = 1,length do
		--构造查找组织是否存在的参数
		local org_data = org2json(org_table_data[i])
		--log_write("traverse_org: call function org2json to create param for org find and the param is ----> "..org_data)
		--调用查找函数
		local find_result = find_orgnization(org_data)
		if(find_result ~= nil) then
			--找到了并且是最后一级则进行插入成员
			if(i == length) then
				--获取成员用户名(org_username)
				local org_username = get_username(data)
				if(org_username ~= nil) then
				else
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
					log_write("traverse_org: org is exist call function org2json_for_insert to create insert param and the param is  ----> "..insert_org_data)

					--插入成员
					local insert_result = insert_member2org(insert_org_data)
					if(insert_result ~= nil) then
						log_write("traverse_org: org is exist call function insert_member2org insert someone into org tree ----> success")
					else
						log_write("traverse_org: org is exist call function insert_member2org insert someone into org tree ----> failed")
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
				log_write("traverse_org: org not exist call function org2json to create param for finding father point and the param is ----> "..org_data_parent)
				--调用查找函数
				parent = find_orgnization(org_data_parent)
				if(parent ~= nil) then
				else
					log_write("traverse_org: org not exist call function find_orgnization to find parent and the parent is ----> nil")
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
						break
					end

					local org_self = find_orgnization(org_data)
					if(org_self ~= nil) then
					else
						log_write("traverse_org: org not exist call function find_orgnization to find org_self and the org_self is ----> nil")
						break
					end

					local insert_org_data = org2json_for_insert(org_username,org_self)
					log_write("traverse_org: org not exist call function org2json_for_insert to add member into tree ----> "..insert_org_data)
					local insert_result = insert_member2org(insert_org_data)
					if(insert_result ~= nil) then
						log_write("traverse_org: org not exist create org and call function insert_member2org insert someone into org tree ----> success")
					else
						log_write("traverse_org: org not exist create org and call function insert_member2org insert someone into org tree ----> failed")
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
		log_write("delete_orgnization_member: call function find_orgnization_member_data to find the member and the member is ---->"..delete_data)
		local orgRet = AuthPluginProvider:deleteOrganizationMember(delete_data)
		log_write("delete_orgnization_member: call function AuthPluginProvider:deleteOrganizationMember and the result is ---->"..orgRet)
		i,j = string.find(orgRet,'"ret":')
		local retResult = string.sub(orgRet,j+1,j+1)
		if(retResult == "0") then
			log_write("delete_orgnization_member: member is delete ----> success")
		else
			log_write("delete_orgnization_member: member is delete ----> failed")
		end
	else
		log_write("delete_orgnization_member:  call function find_orgnization_member_data the member is already not exist in org tree")
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
			log_write("orgtable2json_for_update : call function org2json_for_update_org and the result is ----> "..update_delete_all)
			local orgRet = AuthPluginProvider:deleteOrganizationMember(update_delete_all)
			i,j = string.find(orgRet,'"ret":')
			local retResult = string.sub(orgRet,j+1,j+1)
			if(retResult == "0") then
				return orgRet
			else
				return nil
			end
		else
			return "not exist"
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
		traverse_org(data)
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
			end
		else
			log_write("insert_account : call function account_param_create and  insert_account_data is nil ----> insert failed")
		end
	end
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
		end
	else
		log_write("delete_account : the account is not exist")
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
	--find if exist user
	delete_username = find_username(delete_find_data,"student_number")
	if(delete_username ~= nil)	then
		--delete data
		local deleteData = '{"items":[{"username":"'..delete_username..'"}]}'
		deleteResult = delete_data(deleteData)
		if(deleteResult == "0") then
			log_write(string.format("delete : ----> delete success"))
		else
			log_write(string.format("delete : ----> delete failed"))
		end
	end
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
        --print("identify_info:"..identity_info)
        string_trim(identity_info)
        --print("identify_info:"..identity_info)
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
         --print("insert_table[role]:"..insert_table["role"])
        insert_table["landline"] = ""--data["landline"]
	insert_table["nativeplace_city"] = data["nativeplace_city"]
	insert_table["nativeplace_district"] = data["nativeplace_district"]
	insert_table["nativeplace_nation"] = data["nativeplace_nation"]
	insert_table["card_number"] = data["card_number"]
	insert_table["nativeplace_province"] = data["nativeplace_province"]
	insert_table["title"] = data["title"]
	table.insert(insert_t,insert_table)
	insert_items["items"] = insert_t
	local insert_find_data = table2json(insert_items)
	-- find if exist user
	local insert_student_number = data["ora_uid"]
	local insertFindData = '{"items":[{"student_number":"'..insert_student_number..'"}]}'
	local insert_username = find_username(insertFindData,"student_number")
	if(insert_username ~= nil) then
		log_write("insert : call function find_username and the result is ----> user is already exist")
	else
		--insert data
                --print("insert_find_data"..insert_find_data)
		local insertResult = insert_data(insert_find_data)
		if(insertResult == "0") then
			log_write(string.format("insert : call function insert_data insert someone into ofuser table and the resulet is ----> insert success"))
		else
			log_write(string.format("insert : call function insert_data insert someone into ofuser table and the resulet is ----> insert failed"))
		end
	end
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
        --print("update_table[role]:"..update_table["role"])
	update_table["landline"] = ""--data["landline"]
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
	if(update_username ~= nil)	then
		update_table["username"] = update_username
		table.insert(update_t,update_table)
		update_items["items"] = update_t
		update_find_data = table2json(update_items)
		--update data
		local updateResult = update_data(update_find_data)
		if(updateResult == "0")	then
			log_write("update : call function update to update and the result is ----> success")
		else
			log_write("update : call function update to update and the result is ----> failed")
		end
	else
		log_write("update : user is not exist")
	end
end

-----------------------------
--查找insert后的某个成员
-----------------------------
--[[
function find_inserted_member()
	--查找已经插入到openfire ofuser表里的数据
	local findInsertData = '{"items":[{"student_number":"1220042204"}]}'
	tmpData = find_data(findInsertData)
	if(tmpData ~= nil) then
		log_write("find_inserted_member: the found number is ----> "..tmpData)
	end
end
--]]

-----------------------------
--openfir call onTimer
-----------------------------
function onTimer(conf)
	--check time for everyday's two time
	local tab = os.date("*t")
	if(tab.hour >= onTimerBegin and tab.hour < onTimerBegin + 1) then
	--if(tab.hour >= 0) then
		--call dailyPullData to get data from oracle database
		if(is_pull) then
			log_write("ontimer_time : get data from oracle database the style of get data is ---->pull")
			os.execute(daily_lua_path)
		else
			log_write("ontimer_time : the style of get data is ---->odi push")
		end

		--call db_compare.lua to create get_insertdata.txt get_deletedata.txt get_updatedata.txt
		log_write("call wuhanfangzhi_xueyuan.lua to create get_insertdata.txt get_deletedata.txt get_updatedata.txt")
		os.execute(school_lua_path)

		--delete
		local delete_file = io.open(get_deletedata_path,"rb")
		if(delete_file) then
			for delete_line in delete_file:lines() do
				if(delete_line ~= "") then
					local tData = json.decode(delete_line)
					--user data operate
					delete(tData)
					--orgnization operate delete someone from org tree
					delete_orgnization_member(tData)
					--account operate
					delete_account(tData)
				else
				end
			end
			delete_file:close()
		end

		--insert
		local insert_file = io.open(get_insertdata_path,"rb")
		if(insert_file) then
			for insert_line in insert_file:lines() do
				if(insert_line ~= "") then
					--user data operate
					local tData = json.decode(insert_line)
					insert(tData)
					--orgnization operate insert someone into org tree
					traverse_org(tData)
					--account operate
					insert_account(tData)
				else
				end
			end
			insert_file:close()
		end

		--update
		local update_file = io.open(get_updatedata_path,"rb")
		if(update_file) then
			for update_line in update_file:lines() do
				if(update_line ~= "") then
					--user data operate
					local tData = json.decode(update_line)
					update(tData)
					--orgnization operate to update someone org
					update_orgnization_member(tData)
				else
				end
			end
			update_file:close()
		end
	else
		return
	end
	--log_file:close()
end

-----------------------------
--get plug config
-----------------------------
function getPluginConfig()
	return '{"ret": 0,"errcode": 0,"msg": "ok","data": {"name": "sam350","alias":"sam350_unicom_timer" ,"config": [{"key": "ip","label":"server_ip"},{"key": "port","label" :"server_port","regex": "/^[0-9]*$/"}]}}'
end

-----------------------------
--check username and passwd
-----------------------------
function verifyPassword(account,password,conf,properties)
	return '{"ret":0,"errcode":0,"msg":"ok"}'
end

-----------------------------
--load account info
-----------------------------
function loadAccount(account,conf,properties)
	return '{"ret":0,"errcode":0,"msg":"operate success","data":[{"address":"beijing haidian","sex":"man","name":"zhangsan"},{"address":"beijing","sex":"woman","name":"xiaoming"}]}'
end

