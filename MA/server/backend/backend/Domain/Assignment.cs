using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace backend.Domain;

[Table("Assignment")]
public class Assignment
{
    [Key]
    public int Id { get; set; }
    public string Subject { get; set; } = null!;
    public string Name { get; set; } = null!;
    public DateTime DueDate { get; set; }
    public DateTime ReceivedDate { get; set; }
    public bool IsCompleted { get; set; } = false;
}