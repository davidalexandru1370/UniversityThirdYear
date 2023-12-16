#include "Product.h"

double Product::getPrice()
{
    return this->price;
}

int Product::getQuantity()
{
    return this->quantity;
}

int Product::getId()
{
    return this->id;
}

void Product::setQuantity(int quantity)
{
    this->quantity = quantity;
}

void Product::setPrice(double price)
{
    this->price = price;
}
