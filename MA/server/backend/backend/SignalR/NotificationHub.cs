using backend.Domain;
using Microsoft.AspNetCore.SignalR;

namespace backend.SignalR;

public class NotificationHub : Hub
{
    public async Task Broadcast(OperationType operationType, Assignment? assignment)
    {
        await Clients.All.SendCoreAsync("Operation", new object?[]
        {
            new
            {
                operationType,
                assignment
            }
        });
    }
}