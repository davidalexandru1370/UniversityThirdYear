#include <unordered_map>
#include <mutex>
#include <vector>
#include "Product.h"
using namespace std;

class Inventory
{
private:
	unordered_map<int, Product*> products;
	unordered_map<int, mutex*> mutexes;

public:
	
	Product* getProductById(int id);
	vector<Product*> getAllProducts();
	void addProduct(double prince, int quantity);

};
