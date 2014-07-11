#include <iostream>

#ifndef OPERATION_H
#define OPERATION_H

using namespace std;

class Operation
{
	protected:
		double num1;
		double num2;
	public:
		virtual double getResult(void)=0;
		void setNum1(double value);
		void setNum2(double value);
};

#endif
