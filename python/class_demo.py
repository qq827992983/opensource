class car:
    def __init__(self,name='',price=0):
        self.__name = name;
        self.__price = price
    @property
    def name(self):
        """ return this car's name"""
        return self.__name
    @name.setter
    def name(self, name):
        """ set this car's name"""
        if(name != None and len(name)>0):
            self.__name = name
    @property
    def price(self):
        """ return this car's price"""
        return self.__price
    @price.setter
    def price(self, price):
        """ set this car's name"""
        if(0<price<10000000):
            self.__price = price
    def __str__(self):
        return ('car('+self.name + ',' + str(self.price) + ')')
    def show(self):
        print('name:'+self.name + ',price:' + str(self.price))
    def hello(self, message):
        print message

c = car('Audi',1000000)
c.show()
print(c.name)
print(c.price)
print str(c)
c.name = 'BMW'
c.price=20000000
c.show()
print(c.name)
print(c.price)
print str(c)
c.hello('hello BMW!');

class audi(car):
    def __init__(self,name,price,company):
        car.__init__(self,name,price)
        self.__company = company
    @property
    def company(self):
        """ return this car's company"""
        return self.__company
    @company.setter
    def company(self, company):
        """ set this car's company"""
        if(company != None and len(company) > 0):
            self.__company = company
    def __str__(self):
        return ('car('+self.name + ',' + str(self.price) + ',' + self.company + ')')
    def show(self):
        car.show(self)
        print('company:' + self.company)
        
ad = audi('audi',100000000,'dazhong')
ad.show()
print(ad)


