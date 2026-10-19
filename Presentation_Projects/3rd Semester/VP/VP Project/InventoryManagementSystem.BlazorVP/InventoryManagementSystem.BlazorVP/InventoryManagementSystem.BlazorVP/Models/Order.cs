using InventoryManagementSystem.BlazorVP.Models;

public class Order
{
    public int Id { get; set; }
    public DateTime OrderDate { get; set; } = DateTime.Now;
    public double TotalAmount { get; set; }
    public string Status { get; set; } = "Pending";

    public List<CartItem> Items { get; set; } = new();
}
