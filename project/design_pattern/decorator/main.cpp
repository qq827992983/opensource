#include "shirt.h"
#include "suit.h"

int main(int argc,char *argv[])
{
	Finery *finery = new Finery();
	Shirt *shirt = new Shirt();
	Suit *suit = new Suit();

	shirt->decorate(finery);
	suit->decorate(shirt);

	suit->show();
	return 0;
}
