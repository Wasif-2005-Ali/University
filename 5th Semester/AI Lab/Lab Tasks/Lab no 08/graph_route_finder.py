graph = {
    "S": {"A": 6, "B": 5, "C": 10},
    "A": {"E": 6},
    "B": {"E": 6, "D": 7},
    "C": {"D": 6},
    "D": {"F": 6},
    "E": {"F": 4},
    "F": {"G": 3},
    "G": {}
}
heuristic = {
    "S": 17, "A": 10, "B": 13, "C": 4, 
    "D": 2, "E": 4, "F": 1, "G": 0
}

def get_g(path):
    total = 0
    for i in range(len(path) - 1):
        total += graph[path[i]][path[i+1]]
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
            
        for neighbour in graph[node]:
            new_path = path + [neighbour]
            frontier.append(new_path)
    return None

answer = a_star("S", "G")
print("Path found by A*:", answer)
print("Total cost of this path:", get_g(answer))