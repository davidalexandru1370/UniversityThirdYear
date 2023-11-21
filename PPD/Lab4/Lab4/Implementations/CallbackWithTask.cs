using Lab4.Domain;
using Lab4.Utilities;
using System.Net.Sockets;
using System.Net;
using System.Text;

namespace Lab4.Implementations
{
    public class CallbackWithTask
    {
        private List<string> _urls;
        private Task[] _tasks;

        public CallbackWithTask(List<string> hosts)
        {
            _urls = hosts;
        }

        public void Start()
        {
            _tasks = new Task[_urls.Count];

            foreach (var (url, index) in _urls.Enumerate())
            {
                var u = url;
                var idx = index;
                _tasks[idx] = Task.Run(() => StartRequest(u, idx));
            }

            Task.WaitAll(_tasks.ToArray());
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

            Connect(state).Wait();
            Send(state).Wait();
            Receive(state).Wait();

           

        }

        private Task Connect(State state)
        {
            state.Socket.BeginConnect(state.Endpoint, (IAsyncResult ar) =>
            {
                OnConnect(ar);
            }, state);

            return Task.FromResult(state.IsConnected.WaitOne());

        }

        void OnConnect(IAsyncResult ar)
        {
            State state = ar.AsyncState as State;

            Console.WriteLine($"Id = {state.RequestId} connected to {state.Endpoint}");

            state.Socket.EndConnect(ar);

            var request = Encoding.ASCII.GetBytes(HttpUtilities.GetRequest(state.Hostname, state.EndpointPath));

            state.IsConnected.Set();

        }

        private Task Send(State state)
        {
            var request = Encoding.ASCII.GetBytes(HttpUtilities.GetRequest(state.Hostname, state.EndpointPath));

            state.Socket.BeginSend(request, 0, request.Length, SocketFlags.None, (IAsyncResult ar) =>
            {
                OnSend(ar);
            }, state);

            return Task.FromResult(state.IsConnected.WaitOne());
        }

        private void OnSend(IAsyncResult ar)
        {
            State state = ar.AsyncState as State;

            var numberOfBytesSent = state.Socket.EndSend(ar);
            Console.WriteLine($"Id = {state.RequestId} sending {numberOfBytesSent} to {state.Endpoint}");

            state.IsSend.Set();
        }

        private Task Receive(State state)
        {
            state.Socket.BeginReceive(state.Buffer,0,State.BufferSize,SocketFlags.None, (IAsyncResult ar) =>
            {
                OnReceive(ar);
            }, state);

            return Task.FromResult(state.IsReceived.WaitOne());
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
                    var contentLength = HttpUtilities.GetContentLength(state.Response.ToString());
                    Console.WriteLine("Content length = " + contentLength.ToString());

                    state.Socket.Shutdown(SocketShutdown.Both);
                    state.Socket.Close();
                    state.IsReceived.Set();

                }
                else
                {
                    state.Socket.BeginReceive(state.Buffer, 0, State.BufferSize, SocketFlags.None, (IAsyncResult ar) =>
                    {
                        OnReceive(ar);
                    }, state);
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }

        }
    }
}
