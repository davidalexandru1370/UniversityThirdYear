using Lab4.Domain;
using Lab4.Utilities;
using System.Net;
using System.Net.Sockets;
using System.Text;

namespace Lab4.Implementations
{
    public class Callback
    {
        private List<string> _urls;

        public Callback(List<string> hosts)
        {
            _urls = hosts;
        }

        public void Start()
        {
            foreach (var (url, index) in _urls.Enumerate())
            {
                StartRequest(url, index);
                Thread.Sleep(10000);
            }
        }

        private void StartRequest(string url, int requestId)
        {
            var host = url.Split("/")[0];
            IPHostEntry hostInfo = Dns.GetHostEntry(host);
            IPAddress ipAddress = hostInfo.AddressList[0];

            var endpoint = new IPEndPoint(ipAddress, (int)PORTS.HTTP);

            var socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

            var state = new State()
            {
                Socket = socket,
                Endpoint = endpoint,
                EndpointPath = url.Contains("/") ? url.Substring(url.IndexOf("/")) : "/",
                Hostname = url,
                HostInfo = hostInfo,
                RequestId = requestId,
                Response = new(),
                IpAddress = ipAddress
            };

            socket.BeginConnect(endpoint, OnConnect, state);
        }

        private void OnConnect(IAsyncResult ar)
        {
            State state = ar.AsyncState as State;

            Console.WriteLine($"Id = {state.RequestId} connected to {state.Endpoint}");

            state.Socket.EndConnect(ar);

            var request = Encoding.ASCII.GetBytes(HttpUtilities.GetRequest(state.Hostname, state.EndpointPath));

            state.Socket.BeginSend(request, 0, request.Length, SocketFlags.None, OnSend, state);
        }

        private void OnSend(IAsyncResult ar)
        {
            State state = ar.AsyncState as State;

            var numberOfBytesSent = state.Socket.EndSend(ar);
            Console.WriteLine($"Id = {state.RequestId} sending {numberOfBytesSent} to {state.Endpoint}");

            state.Socket.BeginReceive(state.Buffer, 0, State.BufferSize, SocketFlags.None, OnReceive, state);
        }

        private void OnReceive(IAsyncResult ar)
        {
            var state = ar.AsyncState as State;
            var numberOfBytesRead = state.Socket.EndReceive(ar);

            try
            {
                var response = Encoding.ASCII.GetString(state.Buffer, 0, numberOfBytesRead);
                Console.WriteLine(response);
                state.Response.Append(response);
                if (HttpUtilities.IsHttpHeaderObtained(state.Response.ToString()) == true)
                {
                    string header = state.Response.ToString();

                    Console.WriteLine($"Id = {state.RequestId} received {numberOfBytesRead} from {state.Endpoint}");
                    Console.WriteLine(header);
                    var contentLength = HttpUtilities.GetContentLength(header);
                    Console.WriteLine("Content length = " + contentLength.ToString());
                }
                else
                {

                    state.Socket.BeginReceive(state.Buffer, 0, State.BufferSize, SocketFlags.None, OnReceive, state);
                }


            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }
    }
}


