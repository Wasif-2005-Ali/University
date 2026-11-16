students = ["Ali", "Sara", "Bilal", "Ayesha", "Hamza", "Zain"]

print("Initial list:", students)
print("First student:", students[0])
print("Last student:", students[-1])

students.append("Usman")
students.insert(2, "Noor")
students.remove("Ayesha")

print("Final list:", students)
print("Total students:", len(students))