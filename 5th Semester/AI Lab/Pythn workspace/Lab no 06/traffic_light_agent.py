table = {
    "Red": "Stop",
    "Yellow": "Slow Down",
    "Green": "Go",
    "Flashing Red": "Stop and Proceed with Caution"
}

percept_sequence = ["Red", "Green", "Yellow", "Red"]

def traffic_light_agent(percept):
    return table.get(percept)

print("Percept\tAction")
for p in percept_sequence:
    action = traffic_light_agent(p)
    print(f"{p}\t{action}")

