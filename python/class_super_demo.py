class A(object):
    def hello(self):
        print('hello A')

class B(A):
    def sub_hello(self):
        super(B, self).hello()
        print "Hello B"

b = B()
b.sub_hello()
