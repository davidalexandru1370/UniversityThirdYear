using backend.Persistence;
using backend.Services;
using backend.SignalR;
using Serilog;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

Log.Logger = new LoggerConfiguration()
    .WriteTo.Console()
    .CreateLogger();

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Services.AddScoped<IAssignmentService, AssignmentService>();
builder.Services.AddScoped<NotificationHub, NotificationHub>();

builder.Services.AddSqlServer<AssignmentDbContext>(
    "Server=localhost;Database=Assignments;Trusted_Connection=True;TrustServerCertificate=True;");

builder.Host.UseSerilog();
var app = builder.Build();

app.UseSerilogRequestLogging();

app.UseCors(options => options
    .AllowAnyOrigin().AllowAnyMethod());

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();
