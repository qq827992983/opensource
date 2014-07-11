#!/bin/bash
echo "*******************************************"
echo "**************设计模式*********************"
echo "*******************************************"
echo "在学习设计模式之前，请先知晓设计模式遵循的基本设计原则，他们是设计模式的核心："
echo "1.单一职责原则：类的功能尽量单一"
echo "2.开放封闭原则：对扩展开放，对修改封闭"
echo "3.依赖倒转原则：针对接口变成而不是针对实现编程"
echo "4.里氏代换原则：子类型必须能够替换父类型"
echo "5.迪米特法则：在类的结构设计上，每个类都尽量降低访问权限"
echo "总结一下：设计模式其实就是前人总结的经验，但却是内功！"
echo "-------------------------------------------"
echo "-------------简单工厂模式------------------"
echo "-------------------------------------------"
cd simple_factory
make
echo "运行结果："
./main
echo "总结："
cat readme.txt
cd ..

echo ""
echo ""
echo "-------------------------------------------"
echo "-------------策略模式----------------------"
echo "-------------------------------------------"
cd strategy
make
echo "运行结果："
./main
echo "总结："
cat readme.txt
cd ..

echo ""
echo ""
echo "-------------------------------------------"
echo "-------------装饰者模式--------------------"
echo "-------------------------------------------"
cd decorator
make
echo "运行结果："
./main
echo "总结："
cat readme.txt
cd ..

