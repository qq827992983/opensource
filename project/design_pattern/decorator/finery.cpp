#include "finery.h"

void Finery::show(void)
{
	if(interface != NULL)
		interface->show();
}

void Finery::decorate(Interface *interface)
{
	this->interface = interface;
}
