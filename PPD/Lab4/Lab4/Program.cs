
using Lab4.Implementations;

List<string> hosts = new List<string>
{
    "www.cs.ubbcluj.ro/files/orar/2023-1/tabelar/IE3.html",
    "www.wikipedia.com",
    "www.olx.ro"
};

Callback callback = new Callback(hosts);

callback.Start();


public static class ExtensionMethods
{
    public static IEnumerable<(T item, int index)> Enumerate<T>(this IEnumerable<T> values)
    {
        return values.Select((item, index) => (item, index));
    }
}