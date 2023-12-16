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
    this->productId = id;
}

int Order::getId()
{
    return this->productId;
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
    mutexes[productId]->lock();
    auto profit = inventory->sale(this->productId, this->quantity);
    this->setQuantity(0);
    this->profit += profit;
    mutexes[productId]->unlock();
}

