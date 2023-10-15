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

void Order::setProfit(double profit)
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

void Order::execute(Inventory* inventory)
{
    auto profit = inventory->sale(this->id, this->quantity);
    this->profit += (long double)profit;
}
