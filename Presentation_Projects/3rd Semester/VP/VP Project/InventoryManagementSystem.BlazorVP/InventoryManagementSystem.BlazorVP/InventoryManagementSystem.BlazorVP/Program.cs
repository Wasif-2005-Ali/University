using InventoryManagementSystem.BlazorVP;
using InventoryManagementSystem.BlazorVP.Services;
using Microsoft.AspNetCore.Components.Web;
using Microsoft.AspNetCore.Components.WebAssembly.Hosting;

var builder = WebAssemblyHostBuilder.CreateDefault(args);

builder.RootComponents.Add<App>("#app");
builder.RootComponents.Add<HeadOutlet>("head::after");

builder.Services.AddScoped(sp => new HttpClient
{
    BaseAddress = new Uri(builder.HostEnvironment.BaseAddress)
});

// ✅ SINGLE STATE FOR ENTIRE APP
builder.Services.AddSingleton<AppState>();

// ✅ SINGLE AUTH SERVICE
builder.Services.AddSingleton<AuthService>();


await builder.Build().RunAsync();
