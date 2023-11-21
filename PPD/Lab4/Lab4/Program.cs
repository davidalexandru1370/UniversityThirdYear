
using Lab4.Implementations;

List<string> hosts = new List<string>
{
    "www.wikipedia.com",
    "www.olx.ro",
    "www.cs.ubbcluj.ro/files/orar/2023-1/tabelar/IE3.html",
};

//Callback callback = new Callback(hosts);

//callback.Start();

Console.WriteLine("---------------------------------------------------");

//CallbackWithTask callbackWithTask = new CallbackWithTask(hosts);

//callbackWithTask.Start();

Console.WriteLine("---------------------------------------------------");

AsyncAwaitTask asyncAwaitTask = new AsyncAwaitTask(hosts);

asyncAwaitTask.Start().Wait();


public static class ExtensionMethods
{
    public static IEnumerable<(T item, int index)> Enumerate<T>(this IEnumerable<T> values)
    {
        return values.Select((item, index) => (item, index));
    }
}