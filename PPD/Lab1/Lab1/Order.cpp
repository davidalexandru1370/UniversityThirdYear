#include "Order.h"

int Order::getQuantity()
{
    return this->quantity;
}

void Order::setQuantity(int quantity)
{
    this->quantity = quantity;
}

long double Order::getProfit()
{
    return this->profit;
}

void Order::setProfit(long double profit)
{
    this->profit = profit;
}

void Order::setId(int id)
{
    this->id = id;
}

int Order::getId()
{
    return this->id;
}

void Order::setIsRegistered(bool isRegistered)
{
    this->isRegistered = isRegistered;
}

bool Order::getIsRegistered()
{
    return this->isRegistered;
}

void Order::execute(Inventory* inventory)
{
    auto profit = inventory->sale(this->id, this->quantity);
    this->setQuantity(0);
    this->profit += profit;
}
