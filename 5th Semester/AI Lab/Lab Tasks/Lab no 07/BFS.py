def bfs_shortest_path(graph, start, goal):
   
    visited = [start]
    queue = [[start]]
    
    while queue:
        
        path = queue.pop(0)
        node = path[-1]
        
        if node == goal:
            return path, len(path) - 1
            
        for neighbour in graph.get(node, []):
            if neighbour not in visited:
                visited.append(neighbour)
                queue.append(path + [neighbour])
                
    return None, 0

network = {
    "Ali": ["Bilal", "Sara"],
    "Bilal": ["Ali", "Zara"],
    "Sara": ["Ali", "Omar"],
    "Zara": ["Bilal", "Hira"],
    "Omar": ["Sara"],
    "Hira": ["Zara"],
}

path, introductions = bfs_shortest_path(network, "Ali", "Hira")
print(f"Shortest chain from Ali to Hira: {path}")
print(f"Number of introductions needed: {introductions}")