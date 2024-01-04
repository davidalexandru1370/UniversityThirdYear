using backend.Domain;
using backend.Services;
using backend.SignalR;
using Mapster;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.SignalR;

namespace backend.Controllers;

[ApiController]
[Route("/api/assignment")]
public class AssignmentController : ControllerBase
{
    private readonly IAssignmentService _assignmentService;
    private readonly NotificationHub _notificationHub;
    public AssignmentController(IAssignmentService assignmentService, NotificationHub notificationHub)
    {
        _assignmentService = assignmentService;
        _notificationHub = notificationHub;
    }

    [HttpPost("")]
    public async Task<ActionResult<Assignment>> AddAssignment([FromBody]CreateAssignmentRequest createAssignmentRequest)
    {
        Assignment assignment = createAssignmentRequest.Adapt<Assignment>();
        var result = await _assignmentService.AddAssignmentAsync(assignment);
        
        return Created("", result);
    }

    [HttpGet("")]
    public async Task<ActionResult<List<Assignment>>> GetAllAssignments()
    {
        var assignments = await _assignmentService.GetAllAssignmentsAsync();
        return Ok(assignments);
    }
    
    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteAssignment([FromRoute]int id)
    {
        try
        {
            await _assignmentService.DeleteAssignmentAsync(id);
            return Ok();
        }
        catch (NullReferenceException)
        {
            return NotFound();
        }
    }

    [HttpPut("")]
    public async Task<ActionResult<Assignment>> UpdateAssignment([FromBody]Assignment assignment)
    {
        try
        {
            await _assignmentService.UpdateAssignmentAsync(assignment);
            return Ok();
        }
        catch (NullReferenceException)
        {
            return NotFound();
        }
    }
}
