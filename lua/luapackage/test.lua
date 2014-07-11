print("path:"..package.path)       --> lua文件的搜索路径
print("cpath:"..package.cpath)      --> lua c 文件的搜索路径
local p = "D:/工作/lua/example/luapackage/"
local m_package_path = package.path
package.path = string.format("%s;%s?.lua;%s?/init.lua",m_package_path, p, p)

print("path:"..package.path)       --> lua文件的搜索路径
print("cpath:"..package.cpath)      --> lua c 文件的搜索路径

require "demopackage"
require "demopackage.a"

print("--------package: demopackage --------------")
for i in pairs(demopackage) do
    print(i, demopackage[i])
end

print("--------package: demopackage.a --------------")
for i in pairs(demopackage.a) do
    print(i, demopackage.a[i])
end


print("---------------demo print--------------")
print( demopackage.add(1, 2) )
print( demopackage.a.lstostring({"first", "second"}) )
print( demopackage.a.p() )
