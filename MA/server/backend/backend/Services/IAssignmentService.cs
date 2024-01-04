using backend.Domain;

namespace backend.Services;

public interface IAssignmentService
{
    Task<Assignment> AddAssignmentAsync(Assignment assignment);

    Task<List<Assignment>> GetAllAssignmentsAsync();

    Task<Assignment> UpdateAssignmentAsync(Assignment newAssignment);

    Task DeleteAssignmentAsync(int id);
}