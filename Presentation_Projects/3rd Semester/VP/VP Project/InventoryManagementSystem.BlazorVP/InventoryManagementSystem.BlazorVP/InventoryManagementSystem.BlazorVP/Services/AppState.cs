using InventoryManagementSystem.BlazorVP.Models;

namespace InventoryManagementSystem.BlazorVP.Services
{
    public class AppState
    {
        public List<Product> Products { get; set; }
        public List<CartItem> Cart { get; set; } = new();
        public List<Order> OrdersList { get; set; } = new();


        public AppState()
        {
            Products = new List<Product>
            {
                new Product { Id = 1, Name = "Laptop", Category = "Electronics", Stock = 15, Price = 899.99 },
                new Product { Id = 2, Name = "Desk Chair", Category = "Furniture", Stock = 8, Price = 149.99 },
                new Product { Id = 3, Name = "Coffee Mug", Category = "Kitchen", Stock = 50, Price = 12.99 },
                new Product { Id = 4, Name = "Notebook", Category = "Stationery", Stock = 3, Price = 5.99 },
                new Product { Id = 5, Name = "Wireless Mouse", Category = "Electronics", Stock = 22, Price = 29.99 },
                new Product { Id = 6, Name = "Desk Lamp", Category = "Furniture", Stock = 0, Price = 39.99 }
            };
        }
    }
}
