#pragma once
class Order
{
private: 
	int quantity;

public:
	int getQuantity();
	void setQuantity(int quantity);


	Order(int quantity): quantity(quantity)
	{
	}
};

