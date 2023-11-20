using System.Text.RegularExpressions;

namespace Lab4.Utilities
{
    public class HttpUtilities
    {

        public static string GetRequest(string hostname, string endpoint)
        {
            return "GET " + endpoint + " HTTP/1.1" + "\r\n" +
                "Host: " + hostname + "\r\n\r\n";
        }

        public static bool IsHttpHeaderObtained(string response)
        {
            return response.Contains("\r\n\r\n");
        }

        public static int GetContentLength(string header)
        {
            var pairs = header.Split("\n", StringSplitOptions.RemoveEmptyEntries);

            foreach (string pair in pairs)
            {
                if (Regex.IsMatch(pair, @"Content-Length[ \t]*:[ \t]*[\d]+"))
                {
                    return Convert.ToInt32(pair.Split(":")[1]);
                }
            }

            return 0;
        }
    }
}
