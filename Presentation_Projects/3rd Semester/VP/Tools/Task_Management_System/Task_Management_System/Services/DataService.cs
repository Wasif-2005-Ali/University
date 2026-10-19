using Task_Management_System.Models;

namespace Task_Management_System.Services
{
    public class DataService
    {
        private readonly List<TaskItem> _tasks = new();
        private readonly List<Appointment> _appointments = new();
        private int _taskId = 1;
        private int _apptId = 1;

        public IEnumerable<TaskItem> GetTasks() => _tasks;
        public IEnumerable<Appointment> GetAppointments() => _appointments.OrderBy(a => a.Date).ThenBy(a => a.Time);

        public TaskItem AddTask(TaskItem task)
        {
            task.Id = _taskId++;
            _tasks.Add(task);
            return task;
        }

        public void UpdateTask(TaskItem task)
        {
            var existing = _tasks.FirstOrDefault(t => t.Id == task.Id);
            if (existing is null) return;
            existing.Title = task.Title;
            existing.Description = task.Description;
            existing.Priority = task.Priority;
            existing.Status = task.Status;
        }

        public void DeleteTask(int id) => _tasks.RemoveAll(t => t.Id == id);

        public Appointment AddAppointment(Appointment appt)
        {
            appt.Id = _apptId++;
            _appointments.Add(appt);
            return appt;
        }

        public void UpdateAppointment(Appointment appt)
        {
            var existing = _appointments.FirstOrDefault(a => a.Id == appt.Id);
            if (existing is null) return;
            existing.Person = appt.Person;
            existing.Date = appt.Date;
            existing.Time = appt.Time;
            existing.Purpose = appt.Purpose;
        }

        public void DeleteAppointment(int id) => _appointments.RemoveAll(a => a.Id == id);
    }
}
