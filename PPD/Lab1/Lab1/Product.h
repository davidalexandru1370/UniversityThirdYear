#pragma once
class Product
{
private:
	int id;
	int quantity;
	double price;
	
public:
	double getPrice();
	int getQuantity();
	int getId();
	void setQuantity(int quantity);
	void setPrice(double price);

	Product(int id, int quantity, double price) : id(id), quantity(quantity), price(price)
	{
	}
};

