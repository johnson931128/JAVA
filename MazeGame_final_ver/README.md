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

### 2025-03-28
1. Added a simple login panel (`gui.LoginPanel`), including title and "Continue", "New Game", "Sign Up" buttons, layout inspired by design
2. Modified `Main.App.java` to display this login panel when starting

## Project Structure
- `src/`: Source code directory
  - `model/`: Game data models
  - `gui/`: User interface components
  - `game/`: Game logic and controllers
- `bin/`: Compiled class files
- `build.bat/build.sh`: Build scripts
- `MazeGame.jar`: Executable game file

## use app
```
javac src/Main.App.java src/game/*.java src/gui/*.java src/model/*.java
java -cp src Main.App
```

## 錯誤訊息
![出現的話，不要修正](螢幕擷取畫面 2025-05-02 140218.png)
先不要修正，修正會動不了。

## 202505XX
* 新增 src/game/Continue.java，專責處理遊戲進度的存檔與讀檔（關卡、玩家座標）。
* LoginPanel、GameController 等相關檔案將配合呼叫 Continue 進行存讀檔。
* 玩家可於登入畫面點擊 CONTINUE 讀取上次進度，或過關時自動存檔。
* 修正 linter 錯誤：將 printStackTrace 改為 System.err.println，並提醒 package 宣告需與檔案路徑一致。
* 遊戲主畫面新增 SAVE（存檔）按鈕，玩家可隨時手動存檔：
    1. GameUI.java 新增 saveButton，並加到 timerPanel，下方顯示。
    2. GameUI 提供 getSaveButton() 方法。
    3. GameController.java 提供 getCurrentLevel(), getPlayerX(), getPlayerY(), getGameUI() 方法。
    4. Main.App.java 初始化遊戲後，取得 saveButton 並加事件監聽，呼叫 Continue.saveGame(...) 完成存檔。
    5. 存檔成功會跳出提示視窗。
* 遊戲主畫面新增『返回Menu』按鈕：
    1. GameUI.java 新增 backToMenuButton，並加到 timerPanel，下方顯示。
    2. GameUI 提供 getBackToMenuButton() 方法。
    3. Main.App.java 初始化遊戲後，取得 backToMenuButton 並加事件監聽，點擊時切換回登入/主選單畫面。

## 20250507
* 設定畫面大小為```frame.setSize(865,560);```

* 為 SAVE 按鈕串接存檔功能：
    1. GameController.java 新增 getCurrentLevel(), getPlayerX(), getPlayerY() 方法，提供目前關卡與玩家座標。
    2. Main.App.java 取得這些資訊並呼叫 Continue.saveGame(...)，點擊 SAVE 按鈕即可存檔。
    3. 存檔成功會跳出提示視窗。

## 2025-05-XX 詳細更新記錄

### 1. `Main.App.java` 程式碼重構與優化

**目的：** 提高程式碼的可讀性、可維護性，並減少重複程式碼。

**詳細修改內容：**

*   **定義常數取代魔法數字：**
    *   **問題：** 遊戲視窗尺寸（`1062, 694`）和迷宮初始尺寸（`21, 21`）在 `Main.App.java` 中是硬編碼的「魔法數字」，難以理解其含義且修改不便。
    *   **解決方案：** 在 `Main.App` 類別中定義了四個 `final static int` 常數：`MAZE_WIDTH`, `MAZE_HEIGHT`, `FRAME_WIDTH`, 和 `FRAME_HEIGHT`。
    *   **涉及檔案：** `src/Main.App.java`
    *   **具體改動：**
        ```java
        // ... existing code ...
        public class Main.App {
            private static final int MAZE_WIDTH = 21;
            private static final int MAZE_HEIGHT = 21;
            private static final int FRAME_WIDTH = 1062;
            private static final int FRAME_HEIGHT = 694;
            // ... existing code ...
            GameController game = new GameController(MAZE_WIDTH, MAZE_HEIGHT, frame);
            // ... existing code ...
            frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        }
        ```

*   **重構面板切換邏輯：**
    *   **問題：** 在 `Main.App.java` 中，切換不同 Swing 面板（如登入面板和遊戲面板）的邏輯 (`contentPane.remove()`, `contentPane.add()`, `revalidate()`, `repaint()`) 在多個地方重複出現（例如 `switchAction`、`CONTINUE` 按鈕事件和「返回 Menu」按鈕事件）。
    *   **解決方案：** 將重複的面板切換程式碼提取到一個新的私有靜態方法 `switchPanel(JFrame frame, JPanel panelToRemove, JPanel panelToAdd)` 中。
    *   **涉及檔案：** `src/Main.App.java`
    *   **具體改動：**
        ```java
        // ... existing code ...
        // 在 main 方法下方新增此方法
        private static void switchPanel(JFrame frame, JPanel panelToRemove, JPanel panelToAdd) {
            Container contentPane = frame.getContentPane();
            contentPane.remove(panelToRemove);
            contentPane.add(panelToAdd);
            contentPane.revalidate();
            contentPane.repaint();
        }
        // ... existing code ...
        // 在 switchAction 中呼叫
        Runnable switchAction = () -> {
            switchPanel(frame, loginPanelHolder[0], gamePanel);
            // ... existing code ...
        };
        // ... existing code ...
        // 在 CONTINUE 按鈕事件中呼叫
        loginPanelHolder[0].getContinueButton().addActionListener(e -> {
            // ... existing code ...
            switchPanel(frame, loginPanelHolder[0], gamePanel);
            // ... existing code ...
        });
        // ... existing code ...
        // 在 返回Menu 按鈕事件中呼叫
        game.getGameUI().getBackToMenuButton().addActionListener(e -> {
            switchPanel(frame, gamePanel, loginPanelHolder[0]);
        });
        // ... existing code ...
        ```

### 2. 修正載入存檔後遊戲關卡跳過頭的問題

**目的：** 確保玩家載入存檔時，遊戲能精確地恢復到存檔時的關卡，而不是跳到更後面的關卡。

**問題成因：**

*   **`GameController` 的持久性：** `Main.App.java` 中的 `GameController` 物件在遊戲主循環中只會被實例化一次。當玩家從遊戲畫面返回選單時，這個 `GameController` 物件並不會被銷毀或重置，其內部 `currentLevel` 等狀態依然保留。
*   **`CONTINUE` 按鈕邏輯問題：** 之前的 `CONTINUE` 按鈕事件處理邏輯是透過 `for (int i = 1; i < save.level; i++) game.startNextLevel();` 來「推進」關卡。這段程式碼的設計意圖是假設遊戲從 Level 1 開始，然後依據存檔的關卡數重複呼叫 `startNextLevel()`。但當 `GameController` 的 `currentLevel` 在返回選單後未重置時，它會在現有基礎上再次推進關卡，導致關卡數疊加，例如從 Level 4 又推進了 3 次，跳到 Level 7。

**詳細修改內容：**

*   **`src/game/GameState.java`：**
    *   **新增欄位：** 為了能夠在重新設定關卡時保持迷宮尺寸的計算邏輯一致，新增 `private int initialRows;` 和 `private int initialCols;` 欄位來儲存遊戲最初始的迷宮尺寸。
    *   **新增方法：** 增加了 `public void setCurrentLevel(int currentLevel)`、`public void setRows(int rows)`、`public void setCols(int cols)` 等 Setter 方法，以及 `public int getInitialRows()`、`public int getInitialCols()` Getter 方法，以提供更精細的遊戲狀態控制。
    *   **具體改動：**
        ```java
        // ... existing code ...
        public class GameState {
            // ... existing fields ...
            private int initialRows;
            private int initialCols;
            // ... existing code ...
            public GameState(int initialRows, int initialCols) {
                // ... existing initializations ...
                this.initialRows = initialRows;
                this.initialCols = initialCols;
            }
            // ... existing methods ...
            public void setCurrentLevel(int currentLevel) { this.currentLevel = currentLevel; }
            public void setRows(int rows) { this.rows = rows; }
            public void setCols(int cols) { this.cols = cols; }
            public int getInitialRows() { return initialRows; }
            public int getInitialCols() { return initialCols; }
            // ... existing methods ...
        }
        ```

*   **`src/game/GameController.java`：**
    *   **新增 `loadGameData` 方法：** 引入了一個新的公共方法 `public void loadGameData(int level, int playerX, int playerY)`，這個方法負責根據存檔的關卡、玩家座標來直接設定遊戲狀態。
    *   **邏輯：** 在 `loadGameData` 內部，它會直接設定 `GameState` 的當前關卡、根據關卡計算迷宮尺寸、重新初始化 `MazeGenerator` 並生成新迷宮、設定玩家位置、重置計時器並更新 UI。這一步確保了遊戲可以直接跳轉到存檔時的準確狀態，而不是逐步推進。
    *   **具體改動：**
        ```java
        // ... existing code ...
        public class GameController {
            // ... existing fields ...
            public void loadGameData(int level, int playerX, int playerY) {
                state.setCurrentLevel(level);
                state.setRows(state.getInitialRows() + (level - 1) * 2); // 重新計算迷宮行數
                state.setCols(state.getInitialCols() + (level - 1) * 2); // 重新計算迷宮列數

                mazeGenerator = new MazeGenerator(state.getRows(), state.getCols()); // 重新生成迷宮
                maze = mazeGenerator.generate();
                
                player.setPosition(playerX, playerY); // 設定玩家位置
                
                state.resetTimer(); // 重置計時器
                
                ui.updateMazePanel(maze, player); // 更新 UI
                setupKeyListener();
                timer.start(); // 啟動計時器
            }
            // ... existing methods ...
        }
        ```

*   **`src/Main.App.java`：**
    *   **修改 `CONTINUE` 按鈕事件：** 將原本 `for` 迴圈呼叫 `game.startNextLevel()` 的邏輯，替換為直接呼叫 `game.loadGameData(save.level, save.playerX, save.playerY)`。
    *   **具體改動：**
        ```java
        // ... existing code ...
        loginPanelHolder[0].getContinueButton().addActionListener(e -> {
            Continue.SaveData save = Continue.loadGame();
            if (save != null) {
                game.loadGameData(save.level, save.playerX, save.playerY); // 直接載入存檔數據
                // ... existing code ...
            }
            // ... existing code ...
        });
        // ... existing code ...
        ```

### 3. 修正載入存檔後迷宮地圖變化的問題

**目的：** 確保玩家載入存檔時，迷宮地圖與存檔時完全一致，提供更一致的遊戲體驗。

**問題成因：**

*   **迷宮生成隨機性：** `MazeGenerator` 在每次初始化時，如果沒有提供固定的隨機數「種子」，它就會基於系統時間等隨機因素生成一個全新的、不同的迷宮。
*   **存檔資料不足：** 之前的 `Continue.SaveData` 只儲存了關卡和玩家座標，並沒有儲存生成迷宮所使用的隨機數種子或迷宮本身的結構。
*   **`loadGameData` 的行為：** 在 `GameController` 的 `loadGameData` 方法中，每次都會創建一個新的 `MazeGenerator` 並生成迷宮，由於沒有固定種子，每次生成的迷宮都是不同的。

**詳細修改內容：**

*   **`src/game/Continue.java`：**
    *   **修改 `SaveData` 結構：** 在 `public static class SaveData` 中新增 `public final long mazeSeed;` 欄位，用於儲存迷宮生成時的隨機數種子。
    *   **修改 `saveGame` 方法：** 新增 `long mazeSeed` 參數，並將其寫入 `save.txt` 檔案，格式為 `mazeSeed=XXXXX`。
    *   **修改 `loadGame` 方法：** 在讀取 `save.txt` 時，解析 `mazeSeed` 欄位，並將其傳遞給 `SaveData` 的建構函式。
    *   **具體改動：**
        ```java
        // ... existing code ...
        public static void saveGame(int level, int playerX, int playerY, long mazeSeed) {
            try (PrintWriter out = new PrintWriter(SAVE_FILE)) {
                out.println("level=" + level);
                out.println("playerX=" + playerX);
                out.println("playerY=" + playerY);
                out.println("mazeSeed=" + mazeSeed); // 新增行
            }
            // ... existing code ...
        }

        public static SaveData loadGame() {
            // ... existing code ...
            long mazeSeed = 0; // 初始化 mazeSeed
            while ((line = in.readLine()) != null) {
                // ... existing parsing ...
                if (line.startsWith("mazeSeed=")) mazeSeed = Long.parseLong(line.substring(9));
            }
            return new SaveData(level, x, y, mazeSeed); // 傳遞 mazeSeed
            // ... existing code ...
        }

        public static class SaveData {
            public final int level;
            public final int playerX;
            public final int playerY;
            public final long mazeSeed; // 新增欄位
            public SaveData(int level, int playerX, int playerY, long mazeSeed) {
                this.level = level;
                this.playerX = playerX;
                this.playerY = playerY;
                this.mazeSeed = mazeSeed; // 初始化
            }
        }
        // ... existing code ...
        ```

*   **`src/model/MazeGenerator.java`：**
    *   **新增 `seed` 欄位：** `private long seed;` 用於儲存迷宮生成時所使用的種子。
    *   **新增帶種子參數的建構函式：** `public MazeGenerator(int rows, int cols, long seed)`，允許外部傳入特定種子來初始化 `Random` 物件 (`this.random = new Random(seed);`)，確保迷宮的可重現性。
    *   **修改原建構函式：** `public MazeGenerator(int rows, int cols)` 被修改為呼叫帶種子參數的新建構函式，並使用 `System.nanoTime()` 作為預設的隨機種子。
    *   **新增 `getSeed()` 方法：** `public long getSeed()`，用於在 `GameController` 中獲取當前生成迷宮所使用的種子。
    *   **具體改動：**
        ```java
        // ... existing code ...
        public class MazeGenerator {
            // ... existing fields ...
            private long seed;

            public MazeGenerator(int rows, int cols) {
                this(rows, cols, System.nanoTime()); // 呼叫帶種子建構函式，使用隨機種子
            }

            public MazeGenerator(int rows, int cols, long seed) {
                this.rows = rows;
                this.cols = cols;
                this.seed = seed; // 儲存種子
                this.random = new Random(seed); // 使用種子初始化 Random
                this.maze = new Cell[rows][cols];
            }
            // ... existing methods ...
            public long getSeed() {
                return seed;
            }
        }
        ```

*   **`src/game/GameController.java`：**
    *   **新增 `currentMazeSeed` 欄位：** `private long currentMazeSeed;` 用於在遊戲運行過程中儲存當前迷宮的種子。
    *   **在迷宮生成時儲存種子：** 在 `initializeGame()` 和 `startNextLevel()` 方法中，每次初始化 `MazeGenerator` 後，將其 `getSeed()` 返回的種子儲存到 `currentMazeSeed`。
    *   **新增 `getCurrentMazeSeed()` 方法：** `public long getCurrentMazeSeed()`，允許 `Main.App.java` 在存檔時獲取當前迷宮的種子。
    *   **修改 `loadGameData` 方法：** 將 `loadGameData` 的簽名修改為 `public void loadGameData(int level, int playerX, int playerY, long mazeSeed)`，使其能夠接收來自存檔的 `mazeSeed`。在方法內部，使用這個傳入的 `mazeSeed` 來初始化 `MazeGenerator` (`mazeGenerator = new MazeGenerator(state.getRows(), state.getCols(), mazeSeed);`)，確保生成相同的迷宮。
    *   **具體改動：**
        ```java
        // ... existing code ...
        public class GameController {
            // ... existing fields ...
            private long currentMazeSeed; // 新增欄位

            // ... existing code ...
            private void initializeGame() {
                mazeGenerator = new MazeGenerator(state.getRows(), state.getCols());
                this.currentMazeSeed = mazeGenerator.getSeed(); // 儲存種子
                // ... existing code ...
            }
            // ... existing code ...
            public void startNextLevel() {
                // ... existing code ...
                mazeGenerator = new MazeGenerator(state.getRows(), state.getCols());
                this.currentMazeSeed = mazeGenerator.getSeed(); // 儲存種子
                // ... existing code ...
            }
            // ... existing code ...
            public long getCurrentMazeSeed() { // 新增方法
                return currentMazeSeed;
            }

            public void loadGameData(int level, int playerX, int playerY, long mazeSeed) { // 修改簽名
                // ... existing state updates ...
                mazeGenerator = new MazeGenerator(state.getRows(), state.getCols(), mazeSeed); // 使用傳入的種子
                this.currentMazeSeed = mazeSeed; // 更新當前種子
                // ... existing code ...
            }
        }
        ```

*   **`src/Main.App.java`：**
    *   **修改 `SAVE` 按鈕事件：** 在呼叫 `Continue.saveGame` 時，新增一個參數 `game.getCurrentMazeSeed()`，將當前迷宮的種子一併存入檔案。
    *   **修改 `CONTINUE` 按鈕事件：** 在呼叫 `game.loadGameData` 時，新增一個參數 `save.mazeSeed`，將從存檔中讀取的種子傳遞給 `GameController`。
    *   **具體改動：**
        ```java
        // ... existing code ...
        game.getGameUI().getSaveButton().addActionListener(e -> {
            Continue.saveGame(
                game.getCurrentLevel(),
                game.getPlayerX(),
                game.getPlayerY(),
                game.getCurrentMazeSeed() // 新增參數
            );
            // ... existing code ...
        });
        // ... existing code ...
        loginPanelHolder[0].getContinueButton().addActionListener(e -> {
            Continue.SaveData save = Continue.loadGame();
            if (save != null) {
                game.loadGameData(save.level, save.playerX, save.playerY, save.mazeSeed); // 新增參數
                // ... existing code ...
            }
            // ... existing code ...
        });
        // ... existing code ...
        ```

## 開發者快速指令：重新編譯、清空 bin/、打包與啟動

### 1. 清空 bin/ 目錄（移除所有 .class 檔案）
Windows PowerShell：
```powershell
Remove-Item -Recurse -Force bin\*.class
```
或（若有子資料夾）
```powershell
Remove-Item -Recurse -Force bin\*
```

### 2. 重新編譯所有 Java 原始碼

#### PowerShell 寫法（推薦）
```powershell
$files = Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName }
javac -cp "lib/gson-2.10.1.jar;src" -d bin $files
```

#### 或直接寫死所有子資料夾（PowerShell/CMD 通用）
```powershell
javac -cp "lib/gson-2.10.1.jar;src" -d bin src\Main\*.java src\game\*.java src\gui\*.java src\model\*.java src\net\*.java
```

#### CMD（命令提示字元）也可用：
```cmd
javac -cp "lib/gson-2.10.1.jar;src" -d bin src\Main\*.java src\game\*.java src\gui\*.java src\model\*.java src\net\*.java
```

> **注意：** PowerShell 不支援 `src/**/*.java` 這種萬用字元展開，請勿直接複製 bash 教學的寫法。

### 3. 重新打包 MazeGame.jar
```powershell
jar cfe MazeGame.jar Main.App -C bin .
```

### 4. 啟動伺服器（Server）
```powershell
java -cp "lib/gson-2.10.1.jar;MazeGame.jar" net.GameServer
```

### 5. 啟動主程式（App.java，單人/多人入口）
```powershell
java -cp "lib/gson-2.10.1.jar;MazeGame.jar" Main.App
```

---

- 多人模式請先啟動伺服器，再於主程式選擇「MULTIPLE」啟動 client。
- 若要直接啟動 client 測試：
```powershell
java -cp "lib/gson-2.10.1.jar;MazeGame.jar" Main.AppClient localhost
```