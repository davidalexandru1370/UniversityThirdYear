#include <iostream>
#include <fstream>
#include "Inventory.h"

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

void executeOrders() {

}


int main()
{
    auto inventory = getInventory();
    vector<thread> threads;
    threads.reserve(THREAD_COUNT + 2);



    for (size_t index = 0; index < THREAD_COUNT; index++)
    {
        threads[index] = thread(executeOrders, );
    }

    for (size_t index = 0; index < THREAD_COUNT; index++)
    {
        threads[index].join();
    }

    return 0;
}

