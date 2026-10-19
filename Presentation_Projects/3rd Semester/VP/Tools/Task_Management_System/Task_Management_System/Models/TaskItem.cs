namespace Task_Management_System.Models
{
    public enum TaskPriority { Low, Medium, High }
    public enum TaskStatus { Todo, InProgress, Done }

    public class TaskItem
    {
        public int Id { get; set; }
        public string Title { get; set; } = string.Empty;
        public string? Description { get; set; }
        public TaskPriority Priority { get; set; } = TaskPriority.Medium;
        public TaskStatus Status { get; set; } = TaskStatus.Todo;
    }
}
