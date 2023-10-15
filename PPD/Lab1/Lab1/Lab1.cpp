#include <iostream>
#include <fstream>
#include "Inventory.h"
#include "Order.h"

#define THREAD_COUNT 1000
long double money = 0;
int numberOfBills = 0;
int numberOfItems = 0;

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
    const int maximumNumberOfQuantity = 50;
    vector<Order> orders;
    for (int index = 0; index < numberOfOrders; index++) {
        auto quantity = rand() % maximumNumberOfQuantity;
        Order *order = new Order(quantity);
        orders.push_back(*order);
    }
    
    return orders;
}

void executeOrders(Order &order) {
    
}


int main()
{
    auto inventory = getInventory();
    auto orders = generateOrders(inventory->getAllProducts().size(), THREAD_COUNT);
    vector<thread> threads;
    threads.reserve(THREAD_COUNT + 2);

    for (size_t index = 0; index < THREAD_COUNT; index++)
    {
        threads[index] = thread(executeOrders, orders[index]);
    }

    for (size_t index = 0; index < THREAD_COUNT; index++)
    {
        threads[index].join();
    }

    return 0;
}

