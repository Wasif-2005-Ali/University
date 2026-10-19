fruits = [("Apple",150), 
          ("Banana", 80), 
          ("Mango", 200),
          ("Grapes",250 ),
          ("Orange", 100),
          ("Papaya", 90)
        ]

#a
for fruit in fruits:
    print(fruit)

#b
names = [fruit[0] for fruit in fruits]
print("Fruit names: ", names)

#c
prices = [fruit[1] for fruit in fruits]
print("Total price:", sum(prices))

#d
fruits[2] = ("Mango", 180)
print("Updated list:",fruits)