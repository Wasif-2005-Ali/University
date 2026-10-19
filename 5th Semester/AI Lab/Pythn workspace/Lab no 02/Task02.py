numbers = []
for i in range(1, 16):
    numbers.append(i)

print("Numbers:", numbers)

even_numbers = []
odd_numbers = []

for num in numbers:
    if num % 2 == 0:
        even_numbers.append(num)
    else:
        odd_numbers.append(num)

print("Even numbers:", even_numbers)
print("Odd numbers:", odd_numbers)

numbers.sort(reverse=True)
print("Descending:", numbers)

middle_five = numbers[5:10]
print("Middle five:", middle_five)