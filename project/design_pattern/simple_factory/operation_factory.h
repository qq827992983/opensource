#include "operation_add.h"
#include "operation_sub.h"
#include <string>

#ifndef OPERATION_FACTORY_H
#define OPERATION_FACTORY_H

class OperationFactory
{
	public:
		static Operation *createOperation(string operation); 
};

#endif
