--example1
print("--------example-1---------")
co = coroutine.create(function (a,b,c)  --����Эͬ����
	print("co", a,b,c)
	print(coroutine.status(co))
end)
print(coroutine.status(co))--��ȡЭͬ����״̬,����̬
coroutine.resume(co, 1, 2, 3) --����Эͬ����,��Ϊ����̬
print(coroutine.status(co)) --״̬����ֹ,��ֹ̬

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
	coroutine.yield(a + b, a - b) ----�Գ��ԣ�yield���صĶ���Ĳ���Ҳ���ᴫ�ݸ�resume
end)
print(coroutine.resume(co2, 20, 10)) --resume���س���true������������ֽ���Ϊ�������ݸ���Ӧ��yield
print(coroutine.status(co2))

--example5
print("--------example-5---------")
co4 = coroutine.create(function ()
	return 6, 7  --��Эͬ�������ʱ���������ص�ֵ���ᴫ����Ӧ��resume
end)
print(coroutine.resume(co4))
