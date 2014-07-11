#include "abstract_strategy.h"

#ifndef CONTEXT_H
#define CONTEXT_H

class Context
{
	public:
		AbstractStrategy *strategy;

		Context(AbstractStrategy *strategy)
		{
			this->strategy = strategy;
		}
		
		void callStrategy();
};

#endif
