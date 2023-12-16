#include <unordered_map>
#include <mutex>
#include <vector>
#include <algorithm>
#include "Product.h"
using namespace std;

class Inventory
{
private:
	unordered_map<int, Product*> products;
	unordered_map<int, mutex*> mutexes;
	mutex lock;

public:
	Product* getProductById(int id);
	vector<Product*> getAllProducts();
	long double sale(int productId, int quantity);
	void addProduct(double price, int quantity);
};
