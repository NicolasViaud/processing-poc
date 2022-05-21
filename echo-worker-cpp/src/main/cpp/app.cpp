#include <iostream>
#include <stdlib.h>
#include "app.h"

std::string demo::Greeter::greeting() {
    return std::string("Hello, World!");
}

int main () {
    demo::Greeter greeter;
    std::cout << greeter.greeting() << std::endl;
    return 0;
}