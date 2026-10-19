// ============================================================================
// MAZE EXPLORER - DSA Project in Single File
// Project by: Wasif Ali (245187), Jawad Mustafa (245195), Abrar Ahmed (245193)
// Course: Data Structures and Algorithms
// Instructor: Sir M. Kamran
// Air University Aerospace and Aviation Campus Kamra
// ============================================================================

#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <stack>
#include <queue>
#include <fstream>
#include <chrono>
#include <thread>
#include <cstdlib>
#include <ctime>
#include <algorithm>
#include <iomanip>
#include <list>
#include <limits>

using namespace std;

// ============================================================================
// CONSTANTS AND GLOBAL DEFINITIONS
// ============================================================================

// Maze constants
const char WALL = '#';
const char PATH = ' ';
const char PLAYER = 'P';
const char EXIT = 'E';
const char TREASURE = 'T';
const char VISITED = '.';

// Movement directions
enum Direction
{
    UP,
    DOWN,
    LEFT,
    RIGHT,
    NONE
};

// Game colors (ANSI codes)
const string RESET = "\033[0m";
const string RED = "\033[31m";
const string GREEN = "\033[32m";
const string YELLOW = "\033[33m";
const string BLUE = "\033[34m";
const string MAGENTA = "\033[35m";
const string CYAN = "\033[36m";
#define BOLD "\033[1m"

// Game configuration
const int DEFAULT_WIDTH = 15; // Smaller maze for better gameplay
const int DEFAULT_HEIGHT = 15;
const int MAX_TREASURES = 6; // Reduced for better balance

struct Position
{
    int x;
    int y;

    Position(int x = 0, int y = 0) : x(x), y(y) {}

    bool operator==(const Position &other) const
    {
        return x == other.x && y == other.y;
    }

    bool operator!=(const Position &other) const
    {
        return !(*this == other);
    }
};

// ============================================================================
// MAZE CLASS
// ============================================================================

class Maze
{
private:
    vector<vector<char>> grid;
    vector<list<int>> adjacencyList;
    int width;
    int height;
    Position start;
    Position exitPos;
    vector<Position> treasures;
    vector<Position> solutionPath;

    // Graph representation helper functions
    int positionToIndex(Position pos) const
    {
        return pos.y * width + pos.x;
    }

    Position indexToPosition(int index) const
    {
        return Position(index % width, index / width);
    }

    bool isValidCell(int x, int y) const
    {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    void buildGraphFromGrid()
    {
        adjacencyList.clear();
        adjacencyList.resize(width * height);

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                if (grid[y][x] != WALL)
                {
                    int current = positionToIndex(Position(x, y));

                    // Check all four directions
                    vector<Position> neighbors = {
                        Position(x, y - 1), // Up
                        Position(x, y + 1), // Down
                        Position(x - 1, y), // Left
                        Position(x + 1, y)  // Right
                    };

                    for (const auto &neighbor : neighbors)
                    {
                        if (isValidCell(neighbor.x, neighbor.y) &&
                            grid[neighbor.y][neighbor.x] != WALL)
                        {
                            int neighborIdx = positionToIndex(neighbor);
                            adjacencyList[current].push_back(neighborIdx);
                        }
                    }
                }
            }
        }
    }

    void carvePath(int x, int y, vector<vector<bool>> &visited)
    {
        visited[y][x] = true;
        grid[y][x] = PATH;

        // Define possible moves
        vector<pair<int, int>> directions = {{0, -2}, {0, 2}, {-2, 0}, {2, 0}};

        // Shuffle directions for random maze
        random_shuffle(directions.begin(), directions.end());

        for (const auto &dir : directions)
        {
            int nx = x + dir.first;
            int ny = y + dir.second;

            if (nx > 0 && nx < width - 1 && ny > 0 && ny < height - 1 && !visited[ny][nx])
            {
                // Carve through the wall between cells
                grid[y + dir.second / 2][x + dir.first / 2] = PATH;
                carvePath(nx, ny, visited);
            }
        }
    }

    vector<Position> bfsShortestPath(Position startPos, Position endPos)
    {
        vector<Position> path;
        if (!isValidCell(startPos.x, startPos.y) || !isValidCell(endPos.x, endPos.y))
            return path;

        int startIdx = positionToIndex(startPos);
        int endIdx = positionToIndex(endPos);

        vector<bool> visited(width * height, false);
        vector<int> parent(width * height, -1);
        queue<int> q;

        q.push(startIdx);
        visited[startIdx] = true;

        while (!q.empty())
        {
            int current = q.front();
            q.pop();

            if (current == endIdx)
            {
                // Reconstruct path
                while (current != -1)
                {
                    path.push_back(indexToPosition(current));
                    current = parent[current];
                }
                reverse(path.begin(), path.end());
                return path;
            }

            for (int neighbor : adjacencyList[current])
            {
                if (!visited[neighbor])
                {
                    visited[neighbor] = true;
                    parent[neighbor] = current;
                    q.push(neighbor);
                }
            }
        }

        return path; // No path found
    }

public:
    Maze(int w = DEFAULT_WIDTH, int h = DEFAULT_HEIGHT) : width(w), height(h)
    {
        // Ensure odd dimensions for proper maze generation
        if (width % 2 == 0)
            width++;
        if (height % 2 == 0)
            height++;

        grid.resize(height, vector<char>(width, WALL));
        start = Position(1, 1);
        exitPos = Position(width - 2, height - 2);
    }

    void generate()
    {
        // Initialize all as walls
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                grid[y][x] = WALL;
            }
        }

        vector<vector<bool>> visited(height, vector<bool>(width, false));

        // Start carving from (1, 1)
        carvePath(1, 1, visited);

        // Set start and exit
        grid[start.y][start.x] = PLAYER;
        grid[exitPos.y][exitPos.x] = EXIT;

        // Place random treasures
        treasures.clear();
        srand(time(nullptr));
        int treasureCount = 0;

        while (treasureCount < MAX_TREASURES)
        {
            int x = rand() % (width - 2) + 1;
            int y = rand() % (height - 2) + 1;

            if (grid[y][x] == PATH && Position(x, y) != start && Position(x, y) != exitPos)
            {
                grid[y][x] = TREASURE;
                treasures.push_back(Position(x, y));
                treasureCount++;
            }
        }

        buildGraphFromGrid();
    }

    void display() const
    {
        cout << "\n";
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                char cell = grid[y][x];
                switch (cell)
                {
                case PLAYER:
                    cout << GREEN << PLAYER << RESET;
                    break;
                case EXIT:
                    cout << RED << EXIT << RESET;
                    break;
                case TREASURE:
                    cout << YELLOW << TREASURE << RESET;
                    break;
                case WALL:
                    cout << BLUE << WALL << RESET;
                    break;
                case VISITED:
                    cout << CYAN << VISITED << RESET;
                    break;
                default:
                    cout << cell;
                }
                cout << " ";
            }
            cout << "\n";
        }
    }

    void displayWithPath() const
    {
        vector<vector<char>> displayGrid = grid;

        // Mark solution path
        for (const auto &pos : solutionPath)
        {
            if (displayGrid[pos.y][pos.x] == PATH)
            {
                displayGrid[pos.y][pos.x] = VISITED;
            }
        }

        cout << "\n";
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                char cell = displayGrid[y][x];
                switch (cell)
                {
                case PLAYER:
                    cout << GREEN << PLAYER << RESET;
                    break;
                case EXIT:
                    cout << RED << EXIT << RESET;
                    break;
                case TREASURE:
                    cout << YELLOW << TREASURE << RESET;
                    break;
                case WALL:
                    cout << BLUE << WALL << RESET;
                    break;
                case VISITED:
                    cout << MAGENTA << VISITED << RESET;
                    break;
                default:
                    cout << cell;
                }
                cout << " ";
            }
            cout << "\n";
        }
    }

    vector<Position> findShortestPath(Position from, Position to)
    {
        return bfsShortestPath(from, to);
    }

    void autoSolve()
    {
        solutionPath = findShortestPath(start, exitPos);
        if (!solutionPath.empty())
        {
            cout << GREEN << "Solution found! Path length: " << solutionPath.size() - 1 << " steps" << RESET << endl;
        }
        else
        {
            cout << RED << "No solution found!" << RESET << endl;
        }
    }

    bool saveToFile(const string &filename) const
    {
        ofstream file(filename);
        if (!file)
            return false;

        file << width << " " << height << "\n";
        file << start.x << " " << start.y << "\n";
        file << exitPos.x << " " << exitPos.y << "\n";

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                file << grid[y][x];
            }
            file << "\n";
        }

        file << treasures.size() << "\n";
        for (const auto &treasure : treasures)
        {
            file << treasure.x << " " << treasure.y << "\n";
        }

        file.close();
        return true;
    }

    bool loadFromFile(const string &filename)
    {
        ifstream file(filename);
        if (!file)
            return false;

        file >> width >> height;
        file >> start.x >> start.y;
        file >> exitPos.x >> exitPos.y;

        grid.resize(height, vector<char>(width));

        string line;
        getline(file, line); // Consume newline

        for (int y = 0; y < height; y++)
        {
            getline(file, line);
            for (int x = 0; x < width; x++)
            {
                grid[y][x] = line[x];
            }
        }

        int treasureCount;
        file >> treasureCount;
        treasures.clear();

        for (int i = 0; i < treasureCount; i++)
        {
            Position treasure;
            file >> treasure.x >> treasure.y;
            treasures.push_back(treasure);
        }

        file.close();
        buildGraphFromGrid();
        return true;
    }

    // Accessors
    char getCell(int x, int y) const { return grid[y][x]; }
    void setCell(int x, int y, char c) { grid[y][x] = c; }
    Position getStart() const { return start; }
    Position getExit() const { return exitPos; }
    vector<Position> getTreasures() const { return treasures; }
    int getWidth() const { return width; }
    int getHeight() const { return height; }
    bool isWall(int x, int y) const { return grid[y][x] == WALL; }
    bool isPath(int x, int y) const { return grid[y][x] == PATH || grid[y][x] == TREASURE; }

    // Remove treasure at position
    bool removeTreasure(Position pos)
    {
        for (auto it = treasures.begin(); it != treasures.end(); ++it)
        {
            if (*it == pos)
            {
                treasures.erase(it);
                return true;
            }
        }
        return false;
    }

    // Utility
    void clearSolution() { solutionPath.clear(); }
    bool isSolved() const { return !solutionPath.empty(); }
};

// ============================================================================
// PLAYER CLASS
// ============================================================================

class Player
{
private:
    Position position;
    stack<Position> moveHistory;
    unordered_map<string, int> inventory;
    int moves;
    int treasuresCollected;
    int totalTreasureValue;

public:
    Player(Position start = Position(1, 1)) : position(start), moves(0), treasuresCollected(0), totalTreasureValue(0)
    {
        inventory["Gold"] = 0;
        inventory["Silver"] = 0;
        inventory["Gem"] = 0;
    }

    bool move(Direction dir, Maze &maze)
    {
        Position newPos = position;

        switch (dir)
        {
        case UP:
            newPos.y--;
            break;
        case DOWN:
            newPos.y++;
            break;
        case LEFT:
            newPos.x--;
            break;
        case RIGHT:
            newPos.x++;
            break;
        default:
            return false;
        }

        // Check if move is valid
        if (newPos.x >= 0 && newPos.x < maze.getWidth() &&
            newPos.y >= 0 && newPos.y < maze.getHeight() &&
            !maze.isWall(newPos.x, newPos.y))
        {

            // Save current position to history
            moveHistory.push(position);

            // Check for treasure at new position BEFORE moving
            if (maze.getCell(newPos.x, newPos.y) == TREASURE)
            {
                collectTreasure(newPos, maze);
            }

            // Update maze display
            maze.setCell(position.x, position.y, PATH);
            maze.setCell(newPos.x, newPos.y, PLAYER);

            position = newPos;
            moves++;

            return true;
        }

        return false;
    }

    bool undo(Maze &maze)
    {
        if (moveHistory.empty())
        {
            return false;
        }

        Position prevPos = moveHistory.top();
        moveHistory.pop();

        // Update maze display
        maze.setCell(position.x, position.y, PATH);
        maze.setCell(prevPos.x, prevPos.y, PLAYER);

        position = prevPos;
        moves--;

        return true;
    }

    void collectTreasure(Position pos, Maze &maze)
    {
        // Check if current position has treasure
        if (maze.getCell(pos.x, pos.y) == TREASURE)
        {
            treasuresCollected++;

            // Random treasure type with different values
            srand(time(nullptr) + moves); // Better randomness
            int treasureType = rand() % 3;
            int value = 0;

            switch (treasureType)
            {
            case 0:
                inventory["Gold"]++;
                value = 100;
                cout << YELLOW << "✨ Collected GOLD! (+100 points)" << RESET << endl;
                break;
            case 1:
                inventory["Silver"]++;
                value = 50;
                cout << CYAN << "✨ Collected SILVER! (+50 points)" << RESET << endl;
                break;
            case 2:
                inventory["Gem"]++;
                value = 200;
                cout << MAGENTA << "✨ Collected GEM! (+200 points)" << RESET << endl;
                break;
            }

            totalTreasureValue += value;

            // Remove treasure from maze
            maze.setCell(pos.x, pos.y, PLAYER);
            maze.removeTreasure(pos);

            // Add some delay for visual feedback
            this_thread::sleep_for(chrono::milliseconds(500));
        }
    }

    void displayInventory() const
    {
        cout << CYAN << "\n========== INVENTORY ==========\n"
             << RESET;

        cout << YELLOW << "Gold:    " << RESET
             << inventory.at("Gold")
             << " x 100 = "
             << GREEN << inventory.at("Gold") * 100 << RESET << endl;

        cout << CYAN << "Silver:  " << RESET
             << inventory.at("Silver")
             << " x 50  = "
             << GREEN << inventory.at("Silver") * 50 << RESET << endl;

        cout << MAGENTA << "Gems:    " << RESET
             << inventory.at("Gem")
             << " x 200 = "
             << GREEN << inventory.at("Gem") * 200 << RESET << endl;

        cout << BLUE << "-------------------------------\n"
             << RESET;

        cout << YELLOW << "Total Treasures: " << RESET
             << treasuresCollected << endl;

        cout << GREEN << "Treasure Value:  " << RESET
             << totalTreasureValue << endl;

        cout << CYAN << "Moves Made:      " << RESET
             << moves << endl;

        cout << BLUE << "================================\n\n"
             << RESET;
    }

    bool hasReachedExit(const Maze &maze) const
    {
        return position == maze.getExit();
    }

    void reset(Position start)
    {
        position = start;
        while (!moveHistory.empty())
        {
            moveHistory.pop();
        }
        inventory["Gold"] = 0;
        inventory["Silver"] = 0;
        inventory["Gem"] = 0;
        moves = 0;
        treasuresCollected = 0;
        totalTreasureValue = 0;
    }

    void saveGame(const string &filename) const
    {
        ofstream file(filename);
        if (!file)
            return;

        file << position.x << " " << position.y << "\n";
        file << moves << " " << treasuresCollected << " " << totalTreasureValue << "\n";
        file << inventory.at("Gold") << " " << inventory.at("Silver") << " " << inventory.at("Gem") << "\n";

        // Save move history
        stack<Position> temp = moveHistory;
        file << temp.size() << "\n";
        while (!temp.empty())
        {
            file << temp.top().x << " " << temp.top().y << "\n";
            temp.pop();
        }

        file.close();
    }

    bool loadGame(const string &filename)
    {
        ifstream file(filename);
        if (!file)
            return false;

        file >> position.x >> position.y;
        file >> moves >> treasuresCollected >> totalTreasureValue;

        int gold, silver, gem;
        file >> gold >> silver >> gem;
        inventory["Gold"] = gold;
        inventory["Silver"] = silver;
        inventory["Gem"] = gem;

        // Load move history
        int historySize;
        file >> historySize;

        while (!moveHistory.empty())
            moveHistory.pop();

        stack<Position> temp;
        for (int i = 0; i < historySize; i++)
        {
            Position pos;
            file >> pos.x >> pos.y;
            temp.push(pos);
        }

        // Reverse to restore order
        while (!temp.empty())
        {
            moveHistory.push(temp.top());
            temp.pop();
        }

        file.close();
        return true;
    }

    // Accessors
    Position getPosition() const { return position; }
    int getMoveCount() const { return moves; }
    int getTreasureCount() const { return treasuresCollected; }
    int getTreasureValue() const { return totalTreasureValue; }
};

// ============================================================================
// LEADERBOARD CLASS (AVL TREE)
// ============================================================================

struct ScoreNode
{
    string playerName;
    int score;
    int treasures;
    int moves;
    int time;
    ScoreNode *left;
    ScoreNode *right;
    int height;

    ScoreNode(string name, int s, int t, int m, int tm)
        : playerName(name), score(s), treasures(t), moves(m), time(tm),
          left(nullptr), right(nullptr), height(1) {}
};

class Leaderboard
{
private:
    ScoreNode *root;

    // AVL Tree helper functions
    int getHeight(ScoreNode *node)
    {
        return node ? node->height : 0;
    }

    int getBalance(ScoreNode *node)
    {
        return node ? getHeight(node->left) - getHeight(node->right) : 0;
    }

    ScoreNode *rightRotate(ScoreNode *y)
    {
        ScoreNode *x = y->left;
        ScoreNode *T2 = x->right;

        x->right = y;
        y->left = T2;

        y->height = max(getHeight(y->left), getHeight(y->right)) + 1;
        x->height = max(getHeight(x->left), getHeight(x->right)) + 1;

        return x;
    }

    ScoreNode *leftRotate(ScoreNode *x)
    {
        ScoreNode *y = x->right;
        ScoreNode *T2 = y->left;

        y->left = x;
        x->right = T2;

        x->height = max(getHeight(x->left), getHeight(x->right)) + 1;
        y->height = max(getHeight(y->left), getHeight(y->right)) + 1;

        return y;
    }

    ScoreNode *insert(ScoreNode *node, const string &name, int score, int treasures, int moves, int time)
    {
        if (!node)
            return new ScoreNode(name, score, treasures, moves, time);

        // Higher scores first (better performance = higher score)
        if (score > node->score)
        {
            node->left = insert(node->left, name, score, treasures, moves, time);
        }
        else if (score < node->score)
        {
            node->right = insert(node->right, name, score, treasures, moves, time);
        }
        else
        {
            // If scores are equal, sort by treasures collected
            if (treasures > node->treasures)
            {
                node->left = insert(node->left, name, score, treasures, moves, time);
            }
            else
            {
                node->right = insert(node->right, name, score, treasures, moves, time);
            }
        }

        // Update height
        node->height = 1 + max(getHeight(node->left), getHeight(node->right));

        // Get balance factor
        int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && score > node->left->score)
            return rightRotate(node);

        // Right Right Case
        if (balance < -1 && score < node->right->score)
            return leftRotate(node);

        // Left Right Case
        if (balance > 1 && score < node->left->score)
        {
            node->left = leftRotate(node->left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && score > node->right->score)
        {
            node->right = rightRotate(node->right);
            return leftRotate(node);
        }

        return node;
    }

    void inOrderTraversal(ScoreNode *node, vector<ScoreNode *> &result)
    {
        if (!node)
            return;

        inOrderTraversal(node->left, result);
        result.push_back(node);
        inOrderTraversal(node->right, result);
    }

    void clearTree(ScoreNode *node)
    {
        if (!node)
            return;
        clearTree(node->left);
        clearTree(node->right);
        delete node;
    }

public:
    Leaderboard() : root(nullptr) {}

    ~Leaderboard()
    {
        clearTree(root);
    }

    void addScore(const string &name, int score, int treasures, int moves, int time)
    {
        root = insert(root, name, score, treasures, moves, time);
    }

    void displayTopScores(int n = 10)
    {
        vector<ScoreNode *> scores;
        inOrderTraversal(root, scores);

        // Reverse to get descending order
        reverse(scores.begin(), scores.end());

        cout << CYAN << "\n=== LEADERBOARD (Top " << min(n, (int)scores.size()) << ") ===\n"
             << RESET;
        cout << left << setw(5) << "Rank" << setw(20) << "Player" << setw(10) << "Score"
             << setw(10) << "Treasures" << setw(10) << "Moves" << setw(10) << "Time" << endl;
        cout << string(65, '-') << endl;

        for (int i = 0; i < min(n, (int)scores.size()); i++)
        {
            cout << left << setw(5) << i + 1
                 << setw(20) << scores[i]->playerName.substr(0, 19)
                 << setw(10) << scores[i]->score
                 << setw(10) << scores[i]->treasures
                 << setw(10) << scores[i]->moves
                 << setw(10) << scores[i]->time << endl;
        }
        cout << CYAN << "===================================================\n"
             << RESET << endl;
    }

    void saveToFile(const string &filename)
    {
        ofstream file(filename);
        if (!file)
            return;

        vector<ScoreNode *> scores;
        inOrderTraversal(root, scores);
        reverse(scores.begin(), scores.end());

        for (const auto &node : scores)
        {
            file << node->playerName << " " << node->score << " "
                 << node->treasures << " " << node->moves << " " << node->time << "\n";
        }

        file.close();
    }

    void loadFromFile(const string &filename)
    {
        ifstream file(filename);
        if (!file)
            return;

        clearTree(root);
        root = nullptr;

        string name;
        int score, treasures, moves, time;

        while (file >> name >> score >> treasures >> moves >> time)
        {
            root = insert(root, name, score, treasures, moves, time);
        }

        file.close();
    }

    void clear()
    {
        clearTree(root);
        root = nullptr;
    }
};

// ============================================================================
// UTILITY FUNCTIONS
// ============================================================================

void clearScreen()
{
#ifdef _WIN32
    system("cls");
#else
    system("clear");
#endif
}

void displayMenu()
{
    clearScreen();
    cout << CYAN << "=====================================\n";
    cout << "        MAZE EXPLORER v1.0\n";
    cout << "   Data Structures & Algorithms Project\n";
    cout << "=====================================\n"
         << RESET;
    cout << GREEN << "Created by:\n";
    cout << "  Wasif Ali (245187)\n";
    cout << "  Jawad Mustafa (245195)\n";
    cout << "  Abrar Ahmed (245193)\n"
         << RESET;
    cout << YELLOW << "\n=== MAIN MENU ===\n"
         << RESET;
    cout << "1. New Game\n";
    cout << "2. Load Game\n";
    cout << "3. Leaderboard\n";
    cout << "4. Instructions\n";
    cout << "5. Exit\n";
    cout << "Choose an option: ";
}

void displayInstructions()
{
    clearScreen();
    cout << YELLOW << "=== INSTRUCTIONS ===\n"
         << RESET;
    cout << "Objective: Reach the " << RED << "E" << RESET << " (Exit) while collecting "
         << YELLOW << "T" << RESET << " (Treasures)\n\n";
    cout << "Legend:\n";
    cout << GREEN << "  P" << RESET << " - Player\n";
    cout << RED << "  E" << RESET << " - Exit\n";
    cout << YELLOW << "  T" << RESET << " - Treasure\n";
    cout << BLUE << "  #" << RESET << " - Wall\n";
    cout << MAGENTA << "  ." << RESET << " - Solution Path\n\n";

    cout << "Controls:\n";
    cout << "  W - Move Up\n";
    cout << "  A - Move Left\n";
    cout << "  S - Move Down\n";
    cout << "  D - Move Right\n";
    cout << "  U - Undo Move\n";
    cout << "  P - Show Shortest Path\n";
    cout << "  I - Show Inventory\n";
    cout << "  T - Save Game\n";
    cout << "  M - Main Menu\n";
    cout << "  Q - Quit Game\n\n";

    cout << "Treasure Values:\n";
    cout << YELLOW << "  Gold: 100 points\n"
         << RESET;
    cout << CYAN << "  Silver: 50 points\n"
         << RESET;
    cout << MAGENTA << "  Gem: 200 points\n"
         << RESET;

    cout << "\nPress Enter to continue...";
    cin.ignore();
    cin.get();
}

// ============================================================================
// MAIN FUNCTION
// ============================================================================

int main()
{
    // Seed random number generator
    srand(time(nullptr));

// Create saved_mazes directory if it doesn't exist
#ifdef _WIN32
    system("mkdir saved_mazes 2>nul");
#else
    system("mkdir -p saved_mazes 2>/dev/null");
#endif

    try
    {
        Maze maze;
        Player player;
        Leaderboard leaderboard;
        string playerName;
        bool gameRunning = true;

        // Load leaderboard
        leaderboard.loadFromFile("leaderboard.txt");

        while (gameRunning)
        {
            displayMenu();

            int choice;
            cin >> choice;

            switch (choice)
            {
            case 1:
            { // New Game
                clearScreen();
                cout << "Enter your name: ";
                cin >> playerName;

                // Generate new maze
                maze.generate();
                player.reset(maze.getStart());

                bool inGame = true;
                auto startTime = chrono::steady_clock::now();

                while (inGame)
                {
                    clearScreen();
                    cout << "Player: " << GREEN << playerName << RESET;
                    cout << " | Moves: " << player.getMoveCount();
                    cout << " | Treasures: " << player.getTreasureCount();
                    cout << " | Score: " << YELLOW << player.getTreasureValue() << RESET << "\n\n";

                    maze.display();

                    cout << "\nControls: [WASD] Move | [U] Undo | [P] Auto-solve | [I] Inventory | [T] Save | [M] Menu\n";
                    cout << "Enter command: ";

                    char command;
                    cin >> command;

                    switch (toupper(command))
                    {
                    case 'W':
                        player.move(UP, maze);
                        break;
                    case 'A':
                        player.move(LEFT, maze);
                        break;
                    case 'S':
                        player.move(DOWN, maze);
                        break;
                    case 'D':
                        player.move(RIGHT, maze);
                        break;
                    case 'U':
                        if (player.undo(maze))
                        {
                            cout << GREEN << "Move undone!" << RESET << endl;
                            this_thread::sleep_for(chrono::milliseconds(300));
                        }
                        else
                        {
                            cout << RED << "No moves to undo!" << RESET << endl;
                            this_thread::sleep_for(chrono::milliseconds(500));
                        }
                        break;
                    case 'P':
                        maze.autoSolve();
                        clearScreen();
                        maze.displayWithPath();
                        cout << "\nPress Enter to continue...";
                        cin.ignore();
                        cin.get();
                        maze.clearSolution();
                        break;
                    case 'I':
                        player.displayInventory();
                        cout << "Press Enter to continue...";
                        cin.ignore();
                        cin.get();
                        break;
                    case 'T':
                    {
                        string filename;
                        cout << "Enter save name: ";
                        cin >> filename;
                        if (maze.saveToFile("saved_mazes/" + filename + ".maze"))
                        {
                            player.saveGame("saved_mazes/" + filename + ".save");
                            cout << GREEN << "Game saved successfully!\n"
                                 << RESET;
                        }
                        else
                        {
                            cout << RED << "Failed to save game!\n"
                                 << RESET;
                        }
                        this_thread::sleep_for(chrono::seconds(1));
                        break;
                    }
                    case 'M':
                        inGame = false;
                        break;
                    case 'Q':
                        inGame = false;
                        gameRunning = false;
                        break;
                    default:
                        cout << RED << "Invalid command!\n"
                             << RESET;
                        this_thread::sleep_for(chrono::milliseconds(500));
                    }

                    // Check if player reached exit
                    if (player.hasReachedExit(maze))
                    {
                        auto endTime = chrono::steady_clock::now();
                        auto duration = chrono::duration_cast<chrono::seconds>(endTime - startTime);
                        int timeTaken = duration.count();

                        clearScreen();
                        cout << BOLD << GREEN
                             << "\n🎉🎉 CONGRATULATIONS! 🎉🎉\n"
                             << RESET;

                        cout << "You reached the exit!\n\n";

                        player.displayInventory();

                        // Calculate final score (ALWAYS POSITIVE)
                        int treasureScore = player.getTreasureValue();
                        int timeBonus = max(0, 500 - (timeTaken * 10));            // Bonus for being fast
                        int moveBonus = max(0, 300 - (player.getMoveCount() * 2)); // Bonus for fewer moves
                        int finalScore = treasureScore + timeBonus + moveBonus;

                        cout << "\n"
                             << CYAN << "=== SCORE BREAKDOWN ===\n"
                             << RESET;
                        cout << "Time taken:        " << timeTaken << " seconds\n";
                        cout << "Time bonus:        " << timeBonus << " points\n";
                        cout << "Move bonus:        " << moveBonus << " points\n";
                        cout << "Treasure score:    " << treasureScore << " points\n";
                        cout << "---------------------------------\n";
                        cout << YELLOW << "FINAL SCORE:       " << finalScore << " points\n\n"
                             << RESET;

                        // Add to leaderboard
                        leaderboard.addScore(playerName, finalScore,
                                             player.getTreasureCount(),
                                             player.getMoveCount(),
                                             timeTaken);

                        cout << "Press Enter to continue...";
                        cin.ignore();
                        cin.get();
                        inGame = false;
                    }
                }
                break;
            }

            case 2:
            { // Load Game
                clearScreen();
                cout << "Enter save name: ";
                string filename;
                cin >> filename;

                if (maze.loadFromFile("saved_mazes/" + filename + ".maze") &&
                    player.loadGame("saved_mazes/" + filename + ".save"))
                {
                    cout << GREEN << "Game loaded successfully!\n"
                         << RESET;
                    this_thread::sleep_for(chrono::seconds(1));

                    bool inGame = true;
                    while (inGame)
                    {
                        clearScreen();
                        cout << "Loaded Game | Moves: " << player.getMoveCount();
                        cout << " | Treasures: " << player.getTreasureCount();
                        cout << " | Score: " << YELLOW << player.getTreasureValue() << RESET << "\n\n";

                        maze.display();

                        cout << "\nControls: [WASD] Move | [U] Undo | [P] Auto-solve | [I] Inventory | [S] Save | [M] Menu\n";
                        cout << "Enter command: ";

                        char command;
                        cin >> command;

                        switch (toupper(command))
                        {
                        case 'W':
                            player.move(UP, maze);
                            break;
                        case 'A':
                            player.move(LEFT, maze);
                            break;
                        case 'S':
                            player.move(DOWN, maze);
                            break;
                        case 'D':
                            player.move(RIGHT, maze);
                            break;
                        case 'U':
                            player.undo(maze);
                            break;
                        case 'P':
                            maze.autoSolve();
                            clearScreen();
                            maze.displayWithPath();
                            cout << "\nPress Enter to continue...";
                            cin.ignore();
                            cin.get();
                            maze.clearSolution();
                            break;
                        case 'I':
                            player.displayInventory();
                            cout << "Press Enter to continue...";
                            cin.ignore();
                            cin.get();
                            break;
                        case 'M':
                            inGame = false;
                            break;
                        case 'Q':
                            inGame = false;
                            gameRunning = false;
                            break;
                        }

                        if (player.hasReachedExit(maze))
                        {
                            cout << GREEN << "\nYou reached the exit!\n"
                                 << RESET;
                            player.displayInventory();
                            cout << "Press Enter to continue...";
                            cin.ignore();
                            cin.get();
                            inGame = false;
                        }
                    }
                }
                else
                {
                    cout << RED << "Failed to load game!\n"
                         << RESET;
                    this_thread::sleep_for(chrono::seconds(1));
                }
                break;
            }

            case 3: // Leaderboard
                clearScreen();
                leaderboard.displayTopScores();
                cout << "Press Enter to continue...";
                cin.ignore();
                cin.get();
                break;

            case 4: // Instructions
                displayInstructions();
                break;

            case 5: // Exit
                gameRunning = false;
                break;

            default:
                cout << RED << "Invalid option!\n"
                     << RESET;
                this_thread::sleep_for(chrono::seconds(1));
            }
        }

        // Save leaderboard before exiting
        leaderboard.saveToFile("leaderboard.txt");

        clearScreen();
        cout << GREEN << "Thank you for playing Maze Explorer!\n";
        cout << "Project by: Wasif Ali, Jawad Mustafa, Abrar Ahmed\n";
        cout << "Course: Data Structures & Algorithms\n";
        cout << "Air University Aerospace and Aviation Campus Kamra\n"
             << RESET;
    }
    catch (const exception &e)
    {
        cerr << RED << "Error: " << e.what() << RESET << endl;
        return 1;
    }

    return 0;
}