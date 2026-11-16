def dfs_can_reach(maze, start, exit_room):
    visited = []
    
    def dfs(node):
        if node not in visited:
            visited.append(node)
        if node == exit_room:
            return True
        for neighbour in maze.get(node, []):
            if neighbour not in visited:
                if dfs(neighbour):
                    return True
        return False

    reachable = dfs(start)
    print(f"Visit order: {visited}")
    print(f"Can reach {exit_room} from {start}? {reachable}")
    return reachable

maze = {
    "Entrance": ["Hall1", "Hall2"],
    "Hall1": ["Room1"],
    "Hall2": ["Room2", "Trap"],
    "Room1": [],
    "Room2": ["Exit"],
    "Trap": [],
    "Exit": [],
}

dfs_can_reach(maze, "Entrance", "Exit")