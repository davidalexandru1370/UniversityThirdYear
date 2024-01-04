using backend.Domain;
using Microsoft.EntityFrameworkCore;

namespace backend.Persistence;

public class AssignmentDbContext : DbContext
{
    public AssignmentDbContext(DbContextOptions options) : base(options)
    {
    }

    public virtual DbSet<Assignment> Assignments { get; set; } = null!;
}