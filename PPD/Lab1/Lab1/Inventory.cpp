
#include "Inventory.h"

Product* Inventory::getProductById(int id)
{
	return this->products[id];
}

vector<Product*> Inventory::getAllProducts()
{
	vector<Product*> allProducts;
	lock.lock();

	for (const auto& pair : this->products) {
		allProducts.push_back(pair.second);
	}
	lock.unlock();

	return allProducts;
}

void Inventory::addProduct(double prince, int quantity)
{
	lock.lock();
	int id = products.size() + 1;
	if()

	lock.unlock();
}
