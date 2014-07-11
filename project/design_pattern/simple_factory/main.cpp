#include "operation_factory.h"

int main(int argc,char *argv[])
{
	string *str = new string("+");
	Operation *op = OperationFactory::createOperation(*str);
	op->setNum1(100.12);
	op->setNum2(89.90);
	cout << op->getResult() << endl;
	
	op = OperationFactory::createOperation("-");
	op->setNum1(100.12);
	op->setNum2(89.90);
	cout << op->getResult() << endl;

	return 0;
}
