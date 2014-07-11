function foo(num)
	if(num >= 10) then
		print(num)
	elseif(num >=  0 and num < 10) then
		print(debug.traceback())
		error({code=121})
	else
		print(debug.traceback())
		error("num < 0!")
	end
end

local ret,err = pcall(foo,11)
if(ret) then
	print("call foo 11 success! ")
else
	print("call foo 11 faild! ")
end

ret,err = pcall(foo,1)
if(ret) then
	print("call foo 1 success! error.code is "..err.code)
else
	print("call foo 1 faild! error.code is "..err.code)
end

ret,err = pcall(foo,-1)
if(ret) then
	print("call foo -1 success! error.code is "..err)
else
	print("call foo -1 faild! error.code is "..err)
end


print("test finish!")
