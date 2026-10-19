attendance = [
    "Present", "Present", "Absent", "Present", "Absent",
    "Present", "Present", "Absent", "Present", "Present"
]

print("Full Attendance:")
for hazri in attendance:
    print(hazri, end=" ")
print("\n")

total_students = len(attendance)
present_count = 0
absent_count = 0

for status in attendance:
    if hazri == "Present":
        present_count += 1
    elif hazri == "Absent":
        absent_count += 1

print("Total Present:", present_count)
print("Total Absent:", absent_count)

print("First 5:", attendance[:5])
print("Last 5:", attendance[-5:])

attendance.reverse()
print("Reversed list:", attendance)

attendance_percentage = (present_count / total_students) * 100
print(f"Attendance Percentage: {attendance_percentage}%")