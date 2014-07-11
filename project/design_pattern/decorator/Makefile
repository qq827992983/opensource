CPP = g++ -g
CCFLAGS = -Wall -O2
LIBS = 
OBJS = main.o finery.o shirt.o suit.o

all:main

%.o: %.cpp %.h
	$(CPP) $(CCFLAGS) $< -c

main: $(OBJS)
	$(CPP) -o $@ $^ $(LIBS)

clean:
	rm -f *.o main
