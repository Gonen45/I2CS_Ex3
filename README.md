# ᗧ•••ᗣ Pac-Man AI Algorithm (Ex3)
This project is part of the Introduction to Computer Science course at Ariel University. The objective is to implement an autonomous Pac-Man agent for the Ex3 game engine. The algorithm is designed to navigate a maze, collect points (Pink targets), and actively evade ghosts using a heuristic safety map.

## How It Works
The logic goes beyond a simple random walk.   It uses Breadth-First Search (BFS) calculations to map the board and make intelligent decisions based on two main factors: Distance to Food and Distance to Ghosts.

### The Decision Cycle
**Map Analysis:**

The algorithm scans the current board state.

It identifies the Pac-Man's position, ghost positions, and all available food pellets (Pink).

**Target Selection (Nearest Neighbor):**

It calculates a "Distance Map" from the Pac-Man to every reachable cell.

It identifies the closest food pellets (Color.PINK). If multiple pellets are at the same minimum distance, they are added to a candidate list.

**Ghost Avoidance (Safety Heuristic):**

A Ghost Map is generated. This is effectively a "heat map" where every cell on the board is assigned a value representing its distance to the nearest ghost.

The algorithm evaluates the candidate food pellets. It prioritizes targets that maximize the distance from ghosts (High Safety Score).

Specific Logic: If the safety score is critical (less than 8 steps from a ghost), the algorithm drastically prioritizes safety over merely eating.

**Movement:**

Once a target is chosen, the algorithm calculates the shortest path to that specific coordinate.

It returns the immediate direction (UP, DOWN, LEFT, or RIGHT) required to follow that path.

Fallback: If no valid path is found (e.g., trapped), it defaults to a random valid move to attempt to unstuck itself.

### Code Structure
The core logic is contained within Ex3Algo.java.

Key Methods
`move(PacmanGame game)`: The main interface method. It coordinates the logic flow and returns the next move direction.

`ghostsMap(PacmanGame game)`: Constructs a Map object representing the "danger zones." It iterates through all ghosts and calculates the distance from every pixel on the board to the nearest ghost.

`moveToPoint(PacmanGame game, int color)`: The primary strategist. It combines the distance to food and the ghost danger map to select the optimal target Index2D.

`findMinIntersection(...)`: A helper that finds the intersection between reachable points and food items to return a list of the closest targets.

`choseBestPoint(...)`: The heuristic filter. It takes the list of closest food items and selects the one with the highest safety rating (furthest from ghosts).




<img width="880" height="870" alt="image" src="https://github.com/user-attachments/assets/5b9a5397-ba3a-466b-8ea0-73a64ddfba8e" />


link to the assignment file:



https://docs.google.com/document/d/1dIZnlusIt11XkjfA61_cfP7LPc-8NtqF/edit


Note, this project isn't completed yet.
