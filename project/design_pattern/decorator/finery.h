#include "interface.h"

#ifndef FINERY_H
#define FINERY_H

class Finery : public Interface
{
	protected:
		Interface *interface;

	public:
		void show(void);
		void decorate(Interface *interface);
};

#endif
