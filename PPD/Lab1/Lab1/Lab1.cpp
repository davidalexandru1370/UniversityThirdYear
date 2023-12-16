#include <iostream>
#include <fstream>
#include <mutex>
#include <cmath>
#include "Order.h"

#define THREAD_COUNT 1000

long double money = 0;
int numberOfBills = 0;
int numberOfItems = 0;
mutex billsMutex;
mutex audit;

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

vector<Order*> generateOrders(int numberOfProducts, int numberOfOrders) {
	const int maximumNumberOfQuantity = 1;
	vector<Order*> orders;
	for (int index = 0; index < numberOfOrders; index++) {
		auto quantity = rand() % maximumNumberOfQuantity + 1;
		int productId = rand() % numberOfProducts + 1;
		Order* order = new Order(productId, quantity);
		orders.push_back(order);
	}

	return orders;
}


long double computeIncomeFromOrders(vector<Order*> orders) {
	double long realIncome = 0;
	for (auto& order : orders) {
		if (order->getIsRegistered() == true) {
			realIncome += order->getProfit();
		}
	}

	return realIncome;
}

void billingAudit(vector<Order*> orders) {
	while (true) {
		billsMutex.lock();
		if (numberOfBills % 100 != 0) {
			billsMutex.unlock();
			continue;
		}
		
		if (numberOfBills == THREAD_COUNT) {
			billsMutex.unlock();
			break;
		}

		double long realIncome = computeIncomeFromOrders(orders);
		long double epsilon = 0.0001;
		audit.lock();
		cout << "Real Income = " << realIncome << " and Current Income = " << money << " ";
		if (abs(realIncome - money) <= epsilon) {
			cout << "OK\n";
		}
		else {
			cout << "Fraudulent\n";
		}
		audit.unlock();
		billsMutex.unlock();
	}
}

void executeOrders(Inventory* inventory, Order* order) {
	order->execute(inventory);
	billsMutex.lock();
	order->setIsRegistered(true);
	money += order->getProfit();
	numberOfBills++;
	billsMutex.unlock();
}

int main()
{
	auto inventory = getInventory();
	vector<Order*> orders = generateOrders(inventory->getAllProducts().size(), THREAD_COUNT);
	vector<thread> threads;

	threads.resize(THREAD_COUNT + 2);

	thread* auditThread = new thread(billingAudit, orders);

	for (int index = 0; index < THREAD_COUNT; index++)
	{
		threads[index] = thread(executeOrders, inventory, orders[index]);
	}

	for (int index = 0; index < THREAD_COUNT; index++)
	{
		threads[index].join();
	}

	auditThread->join();

	cout << numberOfBills << "\n";
	cout << money << "\n";
	cout << computeIncomeFromOrders(orders);

	return 0;
}