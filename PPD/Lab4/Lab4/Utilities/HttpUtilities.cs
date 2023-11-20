namespace Lab4.Utilities
{
    public class HttpUtilities
    {

        public static string GetRequest(string hostname, string endpoint)
        {
            return "GET " + endpoint + "HTTP/1.1" + Environment.NewLine +
                "Host: " + hostname + Environment.NewLine +
                "Content-Type: text/plain";
        }
    }
}
