CPP = g++ -g
CCFLAGS = -Wall -O2
LIBS = 
OBJS = main.o abstract_strategy.o strategy_a.o strategy_b.o context.o 

all:main

%.o: %.cpp %.h
	$(CPP) $(CCFLAGS) $< -c

main: $(OBJS)
	$(CPP) -o $@ $^ $(LIBS)

clean:
	rm -f *.o main
