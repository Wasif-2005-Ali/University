percept_sequence = ["Dark", "Dim", "Bright", "Dark"]

def room_light_agent(percept):
    if percept == "Dark":
        return "Turn Light ON"
    elif percept == "Bright":
        return "Turn Light OFF"
    elif percept == "Dim":
        return "Leave Light As Is"
    elif percept == "Very Bright":
        return "Turn Light OFF completely"
    else:
        return "default"

print("Percept\tAction")
for p in percept_sequence:
    action = room_light_agent(p)
    print(f"{p}\t{action}")