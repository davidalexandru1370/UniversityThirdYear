namespace backend.Domain;

public class CreateAssignmentRequest
{
    public string Subject { get; set; } = null!;
    public string Name { get; set; } = null!;
    public DateTime DueDate { get; set; }
    public DateTime ReceivedDate { get; set; }
    public bool IsCompleted { get; set; } = false;
}