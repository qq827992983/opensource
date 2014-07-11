--example1
print("--------example-1---------")
co = coroutine.create(function (a,b,c)  --创建协同程序
	print("co", a,b,c)
	print(coroutine.status(co))
end)
print(coroutine.status(co))--获取协同程序状态,挂起态
coroutine.resume(co, 1, 2, 3) --启动协同程序,变为运行态
print(coroutine.status(co)) --状态已终止,终止态

--example2
print("--------example-2---------")
function fun1(a,b,c)
	print("co", a,b,c)
end

co1 = coroutine.create(fun1)
coroutine.resume(co1,1, 2, 3)

--example3
print("--------example-3---------")
co2 = coroutine.create(function (a,b)
	print(coroutine.status(co2))
	coroutine.yield(a + b, a - b) ----对称性，yield返回的额外的参数也将会传递给resume
end)
print(coroutine.resume(co2, 20, 10)) --resume返回除了true以外的其他部分将作为参数传递给相应的yield
print(coroutine.status(co2))

--example5
print("--------example-5---------")
co4 = coroutine.create(function ()
	return 6, 7  --当协同代码结束时主函数返回的值都会传给相应的resume
end)
print(coroutine.resume(co4))
