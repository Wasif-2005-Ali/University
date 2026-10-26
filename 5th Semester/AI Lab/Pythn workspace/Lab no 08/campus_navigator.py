campus = {
    "Hostel": {"Cafeteria": 150, "Admin Block": 300, "Gym": 200},
    "Cafeteria": {"Academic Block": 200},
    "Admin Block": {"Academic Block": 120},
    "Gym": {"Library": 180},
    "Academic Block": {"Library": 100},
    "Library": {}
}
heuristic = {
    "Hostel": 350, "Cafeteria": 260, "Admin Block": 150,
    "Gym": 170, "Academic Block": 90, "Library": 0
}

def get_h(path):
    return heuristic[path[-1]]

def get_g(path):
    total = 0
    for i in range(len(path) - 1):
        total += campus[path[i]][path[i+1]]
    return total

def gbfs(start, goal):
    frontier = [[start]]
    visited = []
    
    while len(frontier) > 0:
        frontier.sort(key=get_h)
        path = frontier.pop(0)
        node = path[-1]
        
        if node in visited:
            continue
        visited.append(node)
        
        if node == goal:
            return path
            
        for neighbour in campus[node]:
            new_path = path + [neighbour]
            frontier.append(new_path)
    return None

answer = gbfs("Hostel", "Library")
print("Path found by GBFS:", answer)
print("Total cost of this path:", get_g(answer))