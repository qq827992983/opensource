print("path:"..package.path)       --> lua�ļ�������·��
print("cpath:"..package.cpath)      --> lua c �ļ�������·��
local p = "D:/����/lua/example/luapackage/"
local m_package_path = package.path
package.path = string.format("%s;%s?.lua;%s?/init.lua",m_package_path, p, p)

print("path:"..package.path)       --> lua�ļ�������·��
print("cpath:"..package.cpath)      --> lua c �ļ�������·��

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
