#include <iostream>
#include <fstream>
#include <mutex>
#include "Order.h"

#define THREAD_COUNT 1000
double money = 0;
int numberOfBills = 0;
int numberOfItems = 0;
mutex billsMutex;

Inventory* getInventory() {
	Inventory* inventory = new Inventory();
	double price;
	int quantity;

	ifstream inputFile("input.txt");

	while (!inputFile.eof()) {
		numberOfItems++;
		inputFile >> quantity >> price;
		inventory->addProduct(price, quantity);
	}

	return inventory;
}

vector<Order> generateOrders(int numberOfProducts, int numberOfOrders) {
	const int maximumNumberOfQuantity = 1;
	vector<Order> orders;
	for (int index = 0; index < numberOfOrders; index++) {
		auto quantity = rand() % maximumNumberOfQuantity + 1;
		int productId = rand() % numberOfProducts + 1;
		Order* order = new Order(productId, quantity);
		orders.push_back(*order);
	}

	return orders;
}

void executeOrders(Inventory* inventory, Order* order) {
	order->execute(inventory);
	billsMutex.lock();
	money += order->getProfit();
	numberOfBills++;
	billsMutex.unlock();
}

int main()
{
	auto inventory = getInventory();
	vector<Order> orders = generateOrders(inventory->getAllProducts().size(), THREAD_COUNT);
	vector<thread> threads;
	threads.resize(THREAD_COUNT + 2);

	for (size_t index = 0; index < THREAD_COUNT; index++)
	{
		threads[index] = thread(executeOrders, inventory, &orders[index]);
	}

	for (size_t index = 0; index < THREAD_COUNT; index++)
	{
		threads[index].join();
	}

	cout << numberOfBills << "\n";
	cout << money;
	return 0;
}

