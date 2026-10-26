warehouse = {
    "Receiving": {"Aisle1": 40, "Aisle2": 70},
    "Aisle1": {"Aisle2": 30, "Packing Station": 90},
    "Aisle2": {"Packing Station": 35},
    "Packing Station": {}
}
heuristic = {
    "Receiving": 100, "Aisle1": 70, 
    "Aisle2": 35, "Packing Station": 0
}

def get_g(path):
    total = 0
    for i in range(len(path) - 1):
        total += warehouse[path[i]][path[i+1]]
    return total

def get_h(path):
    return heuristic[path[-1]]

def get_f(path):
    return get_g(path) + get_h(path)

def a_star(start, goal):
    frontier = [[start]]
    nodes_explored = 0
    
    while len(frontier) > 0:
        frontier.sort(key=get_f)
        path = frontier.pop(0)
        node = path[-1]
        nodes_explored += 1
        
        if node == goal:
            print("Total nodes explored:", nodes_explored)
            return path
            
        for neighbour in warehouse[node]:
            new_path = path + [neighbour]
            frontier.append(new_path)
    return None

answer = a_star("Receiving", "Packing Station")
print("Path found by A*:", answer)
print("Total cost of this path:", get_g(answer))