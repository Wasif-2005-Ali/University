import numpy as np

units = np.array([
    [30, 20, 10], 
    [25, 30, 15], 
    [40, 25, 20], 
    [35, 15, 25]
])
price = np.array([100, 150, 200])

revenue_per_day = (units * price).sum(axis=1)
units_sold_per_item = units.sum(axis=0)
best_day_index = revenue_per_day.argmax()
coffee_last_two_days = units[2:, 1]

print(f"Revenue per day: {revenue_per_day}")
print(f"Units sold per item: {units_sold_per_item}")
print(f"Best day index: {best_day_index}")
print(f"Coffee, last two days: {coffee_last_two_days}")

total_revenue = revenue_per_day.sum()
print(f"Total week revenue: {total_revenue}")