# shopping_list.py
shopping_list = [
    {"item": "Notebook", "price": 150, "purchased": True},
    {"item": "Pen", "price": 30, "purchased": True},
    {"item": "USB Drive", "price": 900, "purchased": False},
]

unpurchased_items = [item["item"] for item in shopping_list if not item["purchased"]]
remaining_cost = sum(item["price"] for item in shopping_list if not item["purchased"])

print(f"Still need to buy: {unpurchased_items}")
print(f"Remaining cost: {remaining_cost}")