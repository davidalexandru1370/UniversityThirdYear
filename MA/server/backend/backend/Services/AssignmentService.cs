using backend.Domain;
using backend.Persistence;
using Microsoft.EntityFrameworkCore;

namespace backend.Services;

public class AssignmentService : IAssignmentService
{
    private readonly AssignmentDbContext _assignmentDbContext;

    public AssignmentService(AssignmentDbContext assignmentDbContext)
    {
        _assignmentDbContext = assignmentDbContext;
    }

    public async Task<Assignment> AddAssignmentAsync(Assignment assignment)
    {
        await _assignmentDbContext.AddAsync(assignment);

        await _assignmentDbContext.SaveChangesAsync();

        return assignment;
    }

    public async Task<List<Assignment>> GetAllAssignmentsAsync()
    {
        return await _assignmentDbContext.Assignments.ToListAsync();
    }

    public async Task<Assignment> UpdateAssignmentAsync(Assignment newAssignment)
    {
        var existingAssignment =
            await _assignmentDbContext.Assignments.FirstOrDefaultAsync(a => a.Id == newAssignment.Id);

        if (existingAssignment == null)
        {
            newAssignment.Id = 0;   
            return await this.AddAssignmentAsync(newAssignment);
        }

        _assignmentDbContext.Entry(existingAssignment).CurrentValues.SetValues(newAssignment);
        await _assignmentDbContext.SaveChangesAsync();
        return existingAssignment;
    }

    public async Task DeleteAssignmentAsync(int id)
    {
        var assignment = await _assignmentDbContext.Assignments.FirstOrDefaultAsync(a => a.Id == id);

        if (assignment is null)
        {
            throw new NullReferenceException("Assignment does not exist");
        }

        _assignmentDbContext.Assignments.Remove(assignment);

        await _assignmentDbContext.SaveChangesAsync();
    }
}