CPP = g++ -g
CCFLAGS = -Wall -O2
LIBS = 
OBJS = main.o operation.o operation_add.o operation_sub.o operation_factory.o

all:main

%.o: %.cpp %.h
	$(CPP) $(CCFLAGS) $< -c

main: $(OBJS)
	$(CPP) -o $@ $^ $(LIBS)

clean:
	rm -f *.o main
