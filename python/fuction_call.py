def string_cut(str):
        """
        string cut
        """
        #str = 'abc def XYZ'

        if(len(str) > 0):
                for s in str:
                        print(s)
        else:
                print("this is a null")
        print("ok\n-------------\n")
        return None
def my_add(a,b):
        return a + b;

str = '123 abc xyz'
string_cut(str)
print(my_add(1,2))
