
print("-----------table 1------------")
table1 = {color="blue", thickness=2, npoints=4,
{x=0, y="aa"},
{x=-10, y=0},
{x=-10, y="afdsadf"},
{x=0, y=1}
}

print(table1['color'])
print(table1["thickness"])
print(table1['npoints'])
print(table1[1].x,table1[1].y)
print(table1[2].x,table1[2].y)
print(table1[3].x,table1[3].y)
print(table1[4].x,table1[4].y)


print("-----------table 2------------")
table2 = {1,2,3}
print(table2[1])
print(table2[2])
print(table2[3])

print("-----------table 3------------")
table3 = {["+"] = "add", ["-"] = "sub", ["*"] = "mul", ["/"] = "div"}
i = 20; s = "+"
a = {[i+0] = s, [i+1] = s..s, [i+2] = s..s..s}
print(table3[s])
print(table3["/"])
print(a[22])

print("-----------table 4------------")
--[[
list = nil
for line in io.lines() do
	if(line == "0") then
		break;
	end
	list = {next=list, value=line}
end

l = list
while l do
print(l.value)
l = l.next
end
]]
print("-----------table 5------------(")
tab = {}
table.insert(tab,{name = "name",value = "xh,sex,number"})
table.insert(tab,{name = "org",value = "orgid1,orgname1,orgid2,orgname2"})
table.insert(tab,{name = "test",value = "test1"})
for i,v in ipairs(tab) do
	print(i,v.name,v.value)
end

