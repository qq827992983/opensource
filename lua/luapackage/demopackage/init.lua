module(..., package.seeall)

function add(n1, n2)
    return n1 + n2
end

function sub(n1, n2)
    return n1 - n2
end

function div(n1, n2)
    if n2 ~= 0 then
        return n1 / n2
    else
        error("require n2 is not zero")
    end
end

function mul(n1, n2)
    return n1 * n2
end
