1. Let us consider the Pseudocode subalgorithms defined below. The subalgorithm **transformare** has as input data the
   array x with n integer elements (x[1], …, x[n]) and the array y with m integer elements (y[1], …, y[m]) and as output
   data the array z with k integer elements (z[1], …, z[k]). The subalgorithm g has as input data the array x with n integer
   elements and a natural number i (1 ≤ i ≤ n) and as output data the array y with n elements. What will the array z contain
   after the call **transformare**(x, n, y, 0, z, k)? Justify your answer.

```
Subalgorithm g(x, n, y, i)
    If i ≤ n then
        y[i] ← x[i]
        g(x, n, y, i + 1)
    EndIf
EndSubalgorithm

Subalgorithm transformare(x, n, y, m, z, k)
    If n = 0 then
        g(y, m, z, 1)
        k ← m
    else
        y[m +1] ← x[n]
        transformare(x, n - 1, y, m + 1, z, k)
    EndIf
EndSubalgorithm
```

Answer: The algorithm will override m+n elements from the z array with n elements from x array in reverse order, m elements from y. After the call **transformare**(x, n, y, 0, z, k), z array will contain on the first n elements the x array in reverse order, because the method transformare gets all the elements from the x array into y array in reverse order, starting from n to 1, the the method g will copy all the first n elements from x array into z array.

2. Given an array a[1], …, a[n] (4 ≤ n ≤ 10000) having integer elements in the interval [-1015, 1015], write a program in
   the C++ programming language, having the worst-case time complexity O(n
   3
   ), which displays on the screen four natural
   numbers x, y, z, t (x, y, z, t ∈ [1, n], x < y < z < t), so that a[x] + a[y] + a[z] + a[t] = 0. If there are no four numbers on
   distinct positions in the array having the sum 0, the program will display the value -1 on the screen. Remark. Solutions
   that do not fall within the indicated complexity class will receive a partial score. Algorithms and containers from the
   STL can be used.

   ```
    void 4sum(const vector<long long int>& nums){
        unordered_map<long long int, int> lastPosition;

        for(int index = 0; index < nums.size(); index++){
            lastPosition[nums[index]] = index;
        }

        for(int x = 0; x < nums.size() - 3; x++){
            for(int y = x + 1; y < nums.size() - 2; y++){
                for(int z = y + 1; z < nums.size() - 1; z++){
                    long long int sum = -(nums[x] + nums[y] + nums[z]);
                    if(lastPosition.contains(sum)){
                        int t = lastPosition[sum];
                        cout << x << " " << y << " " << z << t;
                        return;
                    }
                }
            }
        }

        cout << "-1";
    }
   ```

3. Implement in C++ the classes **MyObject, MyInteger, MyString, MyObjectList and MyListIterator** so that the
   following C++ function’s output is the one mentioned in the comment and the memory is correctly managed.

```cpp
void function()
{
    MyObjectList list{};

    list.add(new MyInteger{ 2 })
        .add(new MyString{ "Hi" });

    MyString* s = new MyString{ "Bye" };
    list.add(s)
        .add(new MyString{ "5" });

    MyListIterator i{ list };

    while (i.isValid()) {
        MyObject* o = i.element();
        o->print();
        i.next();
    } // prints: 2 Hi Bye 5
}
```

Solution:

```cpp
class MyObject {
public:
	MyObject(){};
	virtual void print() const = 0;
	virtual ~MyObject() = default;
};

class MyInteger : public MyObject {
private:
	int value;
public:
	MyInteger(int val) : value(val) {

	}

	void print() const override {
		cout << this->value <<" ";
	}

	~MyInteger()  override {

	}
};

class MyString : public MyObject {
private:
	string value;

public:
	MyString(string val) : value(val) {

	}

	void print() const override{
		cout << this->value<<" ";
	}

	~MyString() override {}
};

class MyObjectList {
private:
	vector<MyObject*> objects;

public:
	MyObjectList() {

	}

	MyObjectList& add(MyObject* object) {
		objects.push_back(object);
		return *this;
	}

	MyObject* operator[](int index)  {
		if (index < 0 || index >= objects.size()) {
			throw std::out_of_range("Index out of range");
		}
		MyObject* object =  this->objects[index];
		return object;
	}

	~MyObjectList() {
		for (MyObject* object : objects) {
			delete object;
		}
		objects.clear();
	}

	friend class MyListIterator;
};

class MyListIterator {
private:
	MyObjectList& objectList;
	int index;
public:
	MyListIterator(MyObjectList& myObjectList) : objectList(myObjectList) {
		index = 0;
	};

	bool isValid() {
		return index < objectList.objects.size();
	}

	void next() {
		this->index += 1;
	}

	MyObject* element() {
		return objectList[index];
	}
};
```

4.  Implement in C++ the class Person, which has the private attributes: surname (string), firstname (string), age (integer),
    a constructor that initializes all attributes, and accessor methods for all attributes of the class. Write a function that sorts
    a list of persons. The function will receive as input parameters the list of persons and a function representing the sort
    criteria. The time complexity of the sorting function, in the worst case, must be Θ(n log2n), where n is the number of
    elements in the list.
    Call the function to sort a list of objects of type Person in ascending order:

    - by the attribute name
    - by the attribute age
    - by age and name (if they have the same age, they are compared by name)

    Observation. Do not use any predefined sort operations

```cpp
#include <string>
#include <vector>
#include <function>
using namespace std;

class Person {
private:
	string surname;
	string firstname;
	int age;

public:
	Person(string surname, string firstname, int age) {
		this->surname = surname;
		this->firstname = firstname;
		this->age = age;
	}

	string getSurname() {
		return this->surname;
	}

	string getFirstname() {
		return this->firstname;
	}

	int getAge() {
		return this->age;
	}
};

void mySort(vector<Person*>& persons,int left, int right, function<bool(Person*, Person*)> comparator) {
	// merge sort
	if (persons.size() <= 1 || left >= right) {
		return;
	}

	int mid = (left + right) / 2;
	mySort(persons, left, mid, comparator);
	mySort(persons, mid + 1, right, comparator);

	vector<Person*> temp;
	int i = left;
	int j = mid + 1;
	while (i <= mid && j <= right) {
		if (comparator(persons[i], persons[j])) {
			temp.push_back(persons[i]);
			i++;
		}
		else {
			temp.push_back(persons[j]);
			j++;
		}
	}

	while (i <= mid) {
		temp.push_back(persons[i]);
		i++;
	}

	while (j <= right) {
		temp.push_back(persons[j]);
		j++;
	}

	for (int i = left; i <= right; i++) {
		persons[i] = temp[i - left];
	}
}
int main(){
   vector<Person*> persons;
persons.push_back(new Person("Doe3", "John", 20));
persons.push_back(new Person("Doe4", "Jane", 22));
persons.push_back(new Person("Doe6", "Alice", 22));
persons.push_back(new Person("Doe1", "Bob", 22));
persons.push_back(new Person("Doe2", "Charlie", 18));

mySort(persons, 0, persons.size() - 1, [](Person* p1, Person* p2) {
	return p1->getAge() < p2->getAge();
});

for (Person* p : persons) {
	cout << p->getFirstname() << " " << p->getSurname() << " " << p->getAge() << endl;
}
cout<<"-------------------"<<endl;

mySort(persons, 0, persons.size() - 1, [](Person* p1, Person* p2) {
	return p1->getFirstname() < p2->getFirstname();
});

for (Person* p : persons) {
	cout << p->getFirstname() << " " << p->getSurname() << " " << p->getAge() << endl;
}
cout << "-------------------" << endl;


mySort(persons, 0, persons.size() - 1, [](Person* p1, Person* p2) {
	if (p1->getAge() == p2->getAge()) {
		return p1->getSurname() < p2->getSurname();
	}
	return p1->getAge() < p2->getAge();
});

for (Person* p : persons) {
	cout << p->getFirstname() << " " << p->getSurname() << " " << p->getAge() << endl;
}
cout << "-------------------" << endl;

}

```
