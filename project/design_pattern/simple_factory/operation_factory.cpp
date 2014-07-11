#include "operation_factory.h"

Operation *OperationFactory::createOperation(string operation)
{
	Operation *op = NULL;

	if(operation == "+")
	{
		op = new OperationAdd();
	}
	else if(operation == "-")
	{
		op = new OperationSub();
	}
	else
	{
		return NULL;
	}
	return op;
}
