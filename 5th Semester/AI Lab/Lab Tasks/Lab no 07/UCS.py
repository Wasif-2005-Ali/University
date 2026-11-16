def path_cost(path):
    return sum(cost for _, cost in path)

def ucs_cheapest_route(graph, start, goal):
    visited = []
    queue = [[(start, 0)]]
    
    while queue:
        queue.sort(key=path_cost)
        path = queue.pop(0)
        
        route_nodes = [node for node, cost in path]
        current_cost = path_cost(path)
        print(f"Exploring route: {route_nodes} with cost: {current_cost}")
        
        node = path[-1][0]
        
        if node in visited:
            continue
        visited.append(node)
        
        if node == goal:
            return route_nodes, current_cost
            
        for neighbour, cost in graph.get(node, []):
            queue.append(path + [(neighbour, cost)])
            
    return None, None

flights = {
    "Islamabad": [("Lahore", 40), ("Karachi", 90)],
    "Lahore": [("Karachi", 50), ("Peshawar", 30)],
    "Karachi": [("Quetta", 60)],
    "Peshawar": [("Quetta", 70)],
    "Quetta": [],
}

best_path, best_cost = ucs_cheapest_route(flights, "Islamabad", "Quetta")
print(f"Cheapest route: {best_path}")
print(f"Total ticket cost: {best_cost}")