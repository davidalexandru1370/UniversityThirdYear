- Facade is similar to Proxy in that both buffer a complex entity and \* initialize it on its own. Unlike Facade, Proxy has the same interface as its service object, which makes them interchangeable.

* Decorator and Proxy have similar structures, but very different intents. Both patterns are built on the composition principle, where one object is supposed to delegate some of the work to another. The difference is that a Proxy usually manages the life cycle of its service object on its own, whereas the composition of Decorators is always controlled by the client

- You can use Command and Memento together when implementing “undo”. In this case, commands are responsible for performing various operations over a target object, while mementos save the state of that object just before a command gets executed

- Command and Strategy may look similar because you can use both to parameterize an object with some action. However, they have very different intents.

  - You can use Command to convert any operation into an object. The operation’s parameters become fields of that object. The conversion lets you defer execution of the operation, queue it, store the history of commands, send commands to remote services, etc.

  - On the other hand, Strategy usually describes different ways of doing the same thing, letting you swap these algorithms within a single context class.

- State can be considered as an extension of Strategy. Both patterns are based on composition: they change the behavior of the context by delegating some work to helper objects. Strategy makes these objects completely independent and unaware of each other. However, State doesn’t restrict dependencies between concrete states, letting them alter the state of the context at will.

- Template Method is based on inheritance: it lets you alter parts of an algorithm by extending those parts in subclasses. Strategy is based on composition: you can alter parts of the object’s behavior by supplying it with different strategies that correspond to that behavior. Template Method works at the class level, so it’s static. Strategy works on the object level, letting you switch behaviors at runtime.

So are their structures.

- Proxy and Decorator both have the same interface as their wrapped types, but the proxy creates an instance under the hood, whereas the decorator takes an instance in the constructor.

- Adapter and Facade both have a different interface than what they wrap. But the adapter derives from an existing interface, whereas the facade creates a new interface.

- Bridge and Adapter both point at an existing type. But the bridge will point at an abstract type, and the adapter might point to a concrete type. The bridge will allow you to pair the implementation at runtime, whereas the adapter usually won't.

---

My take on the subject.

All four patterns have a lot in common, all four are sometimes informally called wrappers, or wrapper patterns. All use composition, wrapping subject and delegating the execution to the subject at some point, do mapping one method call to another one. They spare client the necessity of having to construct a different object and copy over all relevant data. If used wisely, they save memory and processor.

By promoting loose coupling they make once stable code less exposed to inevitable changes and better readable for fellow developers.

Adapter

Adapter adapts subject (adaptee) to a different interface. This way we can add object be placed to a collection of nominally different types.

Adapter expose only relevant methods to client, can restrict all others, revealing usage intents for particular contexts, like adapting external library, make it appear less general and more focused on our application needs. Adapters increase readability and self description of our code.

Adapters shields one team from volatile code from other teams; a life savior tool when dealing with offshore teams ;-)

Less mentioned purpose it to prevent the subject class from excess of annotations. With so many frameworks based on annotations this becomes more important usage then ever.

Adapter helps to get around Java limitation of only single inheritance. It can combine several adaptees under one envelope giving impression of multiple inheritance.

Code wise, Adapter is “thin”. It should not add much code to the adaptee class, besides simply calling the adaptee method and occasional data conversions necessary to make such calls.

There are not many good adapter examples in JDK or basic libraries. Application developers create Adapters, to adapt libraries to application specific interfaces.

Decorator

Decorator not only delegate, not only maps one method to another, they do more, they modify behaviour of some subject methods, it can decide not call subject method at all, delegate to a different object, a helper object.

Decorators typically add (transparently) functionality to wrapped object like logging, encryption, formatting, or compression to subject. This New functionality may bring a lot of new code. Hence, decorators are usually much “fatter” then Adapters.

Decorator must be a sub-class of subject's interface. They can be used transparently instead of its subjects. See BufferedOutputStream, it is still OutputStream and can be used as such. That is a major technical difference from Adapters.

Text book examples of whole decorators family is readily in JDK - the Java IO. All classes like BufferedOutputStream, FilterOutputStream and ObjectOutputStream are decorators of OutputStream. They can be onion layered, where one one decorator is decorated again, adding more functionality.

Proxy

- Proxy is not a typical wrapper. The wrapped object, the proxy subject, may not yet exist at the time of proxy creation. Proxy often creates it internally. It may be a heavy object created on demand, or it is remote object in different JVM or different network node and even a non-Java object, a component in native code. It does not have to necessary wrap or delegate to another object at all.

  - Most typical examples are remote proxies, heavy object initializers and access proxies.

  - Remote Proxy – subject is on remote server, different JVM or even non Java system. Proxy translates method calls to RMI/REST/SOAP calls or whatever is needed, shielding client from exposure to underlying technology.

  - Lazy Load Proxy – fully initialize object only the first usage or first intensive usage.

  - Access Proxy – control access to subject.

- Facade

- Facade is closely associated with design Principle of Least Knowledge (Law of Demeter). Facade is very similar to Adapter. They both wrap, they both map one object to another, but they differ in the intent. Facade flattens complex structure of a subject, complex object graph, simplifying access to a complex structure.

- Facade wraps a complex structure, providing a flat interface to it. This prevents client object from being exposed to inner relations in subject structure hence promoting loose coupling.

* Bridge

* More complex variant of Adapter pattern where not only implementation varies but also abstraction. It adds one more indirection to the delegation. The extra delegation is the bridge. It decouples Adapter even from adapting interface. It increases complexity more than any other of the other wrapping patterns, so apply with care.

* Differences in constructors

* Pattern differences are also obvious when looking at their constructors.

* Proxy is not wrapping an existing object. There is no subject in constructor.

* Decorator and Adapter does wrap already existing object, and such is typically
  provided in the constructor.

* Facade constructor takes root element of a whole object graph, otherwise it looks same as Adapter.

* The Flyweight design is usually a store of immutable objects.
