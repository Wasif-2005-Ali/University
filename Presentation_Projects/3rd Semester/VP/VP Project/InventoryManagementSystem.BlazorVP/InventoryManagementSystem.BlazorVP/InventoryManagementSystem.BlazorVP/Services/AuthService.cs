using InventoryManagementSystem.BlazorVP.Models;

namespace InventoryManagementSystem.BlazorVP.Services
{
    public class AuthService
    {
        private List<User> Users = new();

        public User? CurrentUser { get; private set; }

        public bool Login(string username, string password)
        {
            var user = Users.FirstOrDefault(u =>
                u.Username == username && u.Password == password);

            if (user == null) return false;

            CurrentUser = user;
            return true;
        }

        public bool Register(string username, string password, string role)
        {
            if (Users.Any(u => u.Username == username))
                return false;

            Users.Add(new User
            {
                Username = username,
                Password = password,
                Role = role
            });

            return true;
        }

        public void Logout()
        {
            CurrentUser = null;
        }

        public bool IsLoggedIn => CurrentUser != null;
        public bool IsAdmin => CurrentUser?.Role == "Admin";
        public bool IsUser => CurrentUser?.Role == "User";
    }
}
