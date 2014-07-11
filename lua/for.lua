function fact(n)
	if(n < 1) then
		return 1;
	end
	return n*fact(n-1);
end

for i=1,fact(3) do --fact(3) only count one times.
	print(fact(i))
end

for i=1,10 do     --from 1 to 10, defalut step value is 1
	print(fact(i))
end

for i = 1, 9, 2 do --from 1 to 9, step value is 2.the values of i is "1,3,5,7,9"
	print(fact(i));
end
