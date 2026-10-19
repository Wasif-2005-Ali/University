namespace Task_Management_System.Models
{
    public class Appointment
    {
        public int Id { get; set; }
        public string Person { get; set; } = string.Empty;
        public DateTime Date { get; set; } = DateTime.Today;
        public TimeSpan Time { get; set; } = TimeSpan.Zero;
        public string Purpose { get; set; } = string.Empty;
    }
}
