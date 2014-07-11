module(..., package.seeall)

function p()
    print "module demopackage.a"
end

function lstostring(ls)
    return "{" .. table.concat(ls, ", ") .. "}"
end
