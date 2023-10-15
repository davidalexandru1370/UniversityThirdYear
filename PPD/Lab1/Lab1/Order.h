#pragma once
#include "Inventory.h"
class Order
{
private: 
	int quantity;
	int id;
	long double profit = 0;
public:
	int getQuantity();
	void setQuantity(int quantity);
	long double getProfit();
	void setProfit(double profit);
	void setId(int id);
	int getId();

	void execute(Inventory* inventory);

	Order(int productId,int quantity) : id(productId), quantity(quantity)
	{
	}
};

