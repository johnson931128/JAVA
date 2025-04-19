# Maze Game

A Java-based maze game with increasing difficulty levels.

## Features
- Randomly generated mazes
- Multiple levels with increasing difficulty
- Timer and best time tracking
- Automatic maze size increase per level

## How to Play
1. Use arrow keys to move the red player dot
2. Navigate through the maze to reach the green exit point
3. Complete each level to progress to a larger, more challenging maze
4. Try to beat your best time!

## Controls
- ↑: Move Up
- ↓: Move Down
- ←: Move Left
- →: Move Right

## System Requirements
- Java Runtime Environment (JRE) 8 or higher

## Running the Game
Double-click the MazeGame.jar file or run:
```bash
java -jar MazeGame.jar
```

## Development Log

### 2025-03-27
1. Initial project setup
2. Implemented basic maze generation using DFS algorithm
3. Created basic game components:
   - Cell: Basic maze unit
   - Player: Player movement and position
   - MazeGenerator: Maze generation logic
4. Implemented GUI components:
   - MazePanel: Maze rendering
   - Timer display
5. Added game features:
   - Level progression system
   - Timer and best time tracking
   - Automatic maze size increase
6. Code organization and refactoring:
   - Separated code into model/view/controller pattern
   - Created dedicated packages for different components:
     - model: Cell, Player, MazeGenerator
     - gui: MazePanel, GameUI
     - game: GameState, GameController
7. Project packaging:
   - Created build scripts for both Windows and Unix systems
   - Added manifest file for JAR creation
   - Created comprehensive README

## Project Structure
- `src/`: Source code directory
  - `model/`: Game data models
  - `gui/`: User interface components
  - `game/`: Game logic and controllers
- `bin/`: Compiled class files
- `build.bat/build.sh`: Build scripts
- `MazeGame.jar`: Executable game file
