#include "strategy_a.h"
#include "strategy_b.h"
#include "context.h"

int main(int argc,char *argv[])
{
	Context *context = new Context(new StrategyA());
	context->callStrategy();
	delete context;
	context = new Context(new StrategyB());
	context->callStrategy();
	delete context;
	return 0;
}
