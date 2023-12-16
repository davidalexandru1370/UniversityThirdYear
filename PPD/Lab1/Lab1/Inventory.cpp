#include "Inventory.h"

Product* Inventory::getProductById(int id)
{
	return this->products[id];
}

vector<Product*> Inventory::getAllProducts()
{
	vector<Product*> allProducts;

	for (const auto& pair : this->products) {
		allProducts.push_back(pair.second);
	}

	return allProducts;
}

long double Inventory::sale(int productId, int quantity)
{
	mutexes[productId]->lock();
	auto product = products[productId];
	long double amount = 0;
	
	if (quantity <= product->getQuantity()) {
		amount = product->getPrice() * quantity;
		product->setQuantity(product->getQuantity() - quantity);
	}
	
	mutexes[productId]->unlock();
	return amount;
}

void Inventory::addProduct(double price, int quantity)
{
	int id = products.size() + 1;
	auto product = new Product(id, quantity, price);
	products[id] = product;
	mutexes[id] = new mutex();
}

