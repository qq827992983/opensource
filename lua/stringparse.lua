src = "abc,def,hij,k,lmnopq,s"
aaa = src

local ret = nil

function get_db_tables(name)
if(ret == nil) then
	ret = {}
	print("11111111")
end
	while 1 do
		i = string.find(name,",")
		if(i == nil) then break end

		local tmp = string.sub(name,0,i-1)
		table.insert(ret,tmp)
		name = string.sub(name,i+1)
	end
	table.insert(ret,name)
end


get_db_tables(aaa)
for i,v in ipairs(ret) do
	print(i..":"..v);
end


