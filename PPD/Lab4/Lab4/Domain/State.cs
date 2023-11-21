using System.Net.Sockets;
using System.Net;
using System.Text;
using System.Linq.Expressions;

namespace Lab4.Domain
{
    public class State
    {
        public IPHostEntry HostInfo { get; init; }
        public string Hostname { get; init; }
        public string EndpointPath { get; init; }
        public IPAddress IpAddress { get; init; }
        public Socket Socket { get; init; }
        public IPEndPoint Endpoint { get; init; }
        public int RequestId { get; init; }
        public byte[] Buffer { get; init; } = new byte[BufferSize];
        public static int BufferSize { get; private set; } = 256;
        public StringBuilder Response { get; set; }
        public ManualResetEvent IsConnected { get; set; } = new ManualResetEvent(false);
        public ManualResetEvent IsReceived { get; set; } = new ManualResetEvent(false);
        public ManualResetEvent IsSend { get; set; } = new ManualResetEvent(false);

    }
}
