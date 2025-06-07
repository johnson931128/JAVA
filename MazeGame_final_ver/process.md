# Maze Game 開發日記與程式碼解釋（超詳細版）

---

## 目錄
- [Main.App.java](#appjava)
- [gui/LoginPanel.java](#guiloginpaneljava)
- [gui/MazePanel.java](#guimazepaneljava)
- [gui/GameUI.java](#guigameuijava)
- [game/GameController.java](#gamegamecontrollerjava)
- [game/MazeGame.java](#gamemazegamejava)
- [game/GameState.java](#gamegamestatejava)
- [model/Cell.java](#modelcelljava)
- [model/Player.java](#modelplayerjava)
- [model/MazeGenerator.java](#modelmazegeneratorjava)

---

## Main.App.java
### 開發日記與設計理念
- **啟動點**：本檔案是整個遊戲的進入點，負責建立 JFrame 主視窗、初始化遊戲控制器與登入畫面。
- **UI 切換**：設計時考慮到 Java Swing 的元件切換特性，必須 remove/add 並 revalidate/repaint，否則畫面會殘留或無法互動。
- **鍵盤焦點**：每次切換到 MazePanel 必須用 invokeLater 請求 focus，否則玩家無法用鍵盤移動。
- **Lambda 限制**：Java lambda 只能用 final/effectively final 變數，故用陣列包裝 loginPanel 以便在事件中引用。
- **返回Menu功能**：在遊戲主畫面提供返回Menu按鈕，點擊時切換回登入/主選單畫面。

#### 例外狀況與解法
- 若未正確切換元件，會出現畫面殘影或無法操作。
- 若未請求 MazePanel 焦點，鍵盤事件會失效。

#### 互動流程
1. 啟動時顯示 LoginPanel，玩家可選擇「NEW GAME」或「CONTINUE」。
2. 點擊「NEW GAME」會初始化新遊戲並切換到主遊戲畫面。
3. 點擊「CONTINUE」會嘗試讀取存檔，若有則還原進度，否則提示無存檔。
4. 點擊「返回Menu」會切換回登入/主選單畫面。

#### 程式碼片段
```java
Runnable switchAction = () -> {
    Container contentPane = frame.getContentPane();
    contentPane.remove(loginPanelHolder[0]);
    contentPane.add(gamePanel);
    contentPane.revalidate();
    contentPane.repaint();
    SwingUtilities.invokeLater(() -> {
        game.getUiMazePanel().requestFocusInWindow();
    });
};

game.getGameUI().getBackToMenuButton().addActionListener(e -> {
    Container contentPane = frame.getContentPane();
    contentPane.remove(gamePanel);
    contentPane.add(loginPanelHolder[0]);
    contentPane.revalidate();
    contentPane.repaint();
});
```

---

## gui/LoginPanel.java
### 開發日記與設計理念
- **目的**：提供登入/選單畫面，提升專案完整度與用戶體驗。
- **UI 排版**：採用 BoxLayout 垂直排列，並用 Box.createRigidArea/VerticalGlue 控制間距，確保畫面美觀。
- **按鈕設計**：所有按鈕統一樣式，字體、寬度一致，去除焦點框。
- **擴充性**：預留 getter 方便未來擴充（如外部加監聽器）。

#### 互動細節
- 「NEW GAME」按鈕點擊時會呼叫傳入的 Runnable，切換到遊戲主畫面。
- 「CONTINUE」按鈕可由外部加監聽器，實現讀檔功能。

#### 常見問題
- 若未設置 preferredSize，切換畫面時視窗大小會跳動。
- 若未統一按鈕樣式，UI 會顯得雜亂。

#### 程式碼片段
```java
public JButton getContinueButton() { return continueButton; }
```

---

## gui/MazePanel.java
### 開發日記與設計理念
- **繪圖效率**：paintComponent 只在必要時呼叫 repaint，避免效能浪費。
- **縮放與對齊**：CELL_SIZE 固定，確保迷宮再大也能正確顯示。
- **玩家/終點繪製**：用圓點表示，並根據 CELL_SIZE 動態計算座標，確保居中。
- **自動調整大小**：getPreferredSize 依據迷宮大小自動回報，方便外部容器自動調整。

#### 例外狀況
- 迷宮過大時，建議加最大寬高限制，避免超出螢幕。

#### 程式碼片段
```java
@Override
public Dimension getPreferredSize() {
    return new Dimension(
        maze[0].length * CELL_SIZE,
        maze.length * CELL_SIZE
    );
}
```

---

## gui/GameUI.java
### 開發日記與設計理念
- **UI 整合**：將迷宮面板、計時器、存檔按鈕、返回Menu按鈕整合，讓遊戲主流程更易管理。
- **UI 更新**：每次切換新關卡時，正確移除舊 MazePanel 並加上新 MazePanel，避免殘影或事件遺失。
- **存檔按鈕（SAVE）**：提供 saveButton，玩家可隨時手動存檔。
    - 按下 SAVE 時，會將目前關卡、玩家座標寫入 save.txt，並跳出提示「存檔成功！」。
    - 若存檔過程發生錯誤，會在終端顯示錯誤訊息。
    - 存檔格式為純文字，方便日後維護與除錯。
    - 存檔按鈕設計為明顯、易點擊，放在畫面下方 timerPanel。
    - 適合用於玩家想隨時保存進度、或在挑戰高難度迷宮時備份進度。
- **返回Menu按鈕（MENU）**：提供 backToMenuButton，玩家可隨時回到主選單。
    - 按下 MENU 時，會將遊戲主畫面移除，切換回登入/主選單畫面（LoginPanel）。
    - 不會重置遊戲進度，玩家可隨時回來繼續。
    - 若玩家在遊戲中誤觸 MENU，可直接再點 CONTINUE 或 NEW GAME 重新進入。
    - 按鈕設計為明顯、易點擊，與 SAVE 並列於 timerPanel。
    - 提升用戶體驗，讓玩家有「隨時退出」的安全感。

#### 互動細節
- updateMazePanel 會先 remove 舊 MazePanel，再 add 新的，並 revalidate/repaint。
- showLevelCompleteDialog 會彈窗顯示過關資訊，並詢問是否繼續下一關。
- SAVE 按鈕會將進度寫入檔案，MENU 按鈕會切換回登入/主選單畫面。

#### 程式碼片段
```java
timerPanel.add(timerLabel);
timerPanel.add(saveButton);
timerPanel.add(backToMenuButton);

// SAVE 按鈕事件（Main.App.java）
game.getGameUI().getSaveButton().addActionListener(e -> {
    Continue.saveGame(
        game.getCurrentLevel(),
        game.getPlayerX(),
        game.getPlayerY()
    );
    JOptionPane.showMessageDialog(frame, "存檔成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
});

// MENU 按鈕事件（Main.App.java）
game.getGameUI().getBackToMenuButton().addActionListener(e -> {
    Container contentPane = frame.getContentPane();
    contentPane.remove(gamePanel);
    contentPane.add(loginPanelHolder[0]);
    contentPane.revalidate();
    contentPane.repaint();
});
```

### 2025-xx-xx
- 修正 GameUI.java 的 updateMazePanel 方法，僅替換中央 MazePanel，不再移除 SOUTH 的 timerPanel，確保 SAVE、MENU、timerLabel 不會因為切換關卡而消失。

---

## game/GameController.java
### 開發日記與設計理念
- **主流程控制**：負責玩家移動、關卡進度、計時、UI 更新。
- **狀態分離**：將狀態（GameState）、UI（GameUI）、資料（Player, Maze）分離，方便維護。
- **鍵盤事件**：每次切換新關卡時，重新 setupKeyListener 並請求焦點。
- **自動存檔**：可於過關時自動呼叫 Continue.saveGame(...)。

#### 互動細節
- handleKeyPress 根據按鍵計算新座標，並檢查是否合法。
- checkWin 判斷是否到達終點，若過關則 handleLevelComplete。
- startNextLevel 增加關卡與迷宮大小，重設一切狀態。

#### 程式碼片段
```java
public void setPlayerPosition(int x, int y) {
    player.setPosition(x, y);
    ui.getMazePanel().repaint();
}
```

---

## game/MazeGame.java
### 開發日記與設計理念
- **早期版本**：原本所有邏輯都寫在這裡，後來為了結構更清晰，將狀態與控制邏輯拆分到 GameController 與 GameState。
- **自動擴大**：每過一關自動擴大迷宮（最大 61x91），並記錄最佳時間。

#### 程式碼片段
```java
if (player.getX() == cols - 2 && player.getY() == rows - 2) {
    // 過關邏輯
}
```

---

## game/GameState.java
### 開發日記與設計理念
- **狀態管理**：專責管理遊戲狀態（關卡、時間、最佳紀錄、迷宮大小），讓控制器與 UI 不需直接操作這些資料。
- **自動擴大**：每過一關迷宮要自動變大，並且要有最大限制。
- **最佳紀錄**：updateBestTime 只在新時間更短時才更新。

#### 程式碼片段
```java
public void incrementLevel() {
    currentLevel++;
    rows = Math.min(rows + 2, MAX_ROWS);
    cols = Math.min(cols + 4, MAX_COLS);
}
```

---

## model/Cell.java
### 開發日記與設計理念
- **基本單元**：迷宮的基本單元，紀錄每格的座標與是否為牆。
- **擴充性**：可加上道具、陷阱等屬性。

#### 程式碼片段
```java
public boolean isWall() { return isWall; }
public void setWall(boolean wall) { this.isWall = wall; }
```

---

## model/Player.java
### 開發日記與設計理念
- **資料模型**：僅記錄 x, y 座標，方便未來擴充（如加生命值、分數）。

#### 程式碼片段
```java
public void setPosition(int x, int y) {
    this.x = x;
    this.y = y;
}
```

---

## model/MazeGenerator.java
### 開發日記與設計理念
- **DFS 生成**：以 DFS（深度優先搜尋）演算法隨機生成迷宮，確保每次都不同。
- **入口出口**：入口（1,1）與出口（rows-2, cols-2）自動開通。
- **無孤島**：每次遞迴時隨機打亂方向，確保路徑多樣且無孤島。

#### 程式碼片段
```java
private void generateMaze(int x, int y) {
    // 遞迴打通路徑
}
```

### SAVE（存檔）功能點擊後的完整流程

#### 1. 觸發點（UI層）
- **檔案**：src/gui/GameUI.java
- **元件**：saveButton（JButton）
- **說明**：玩家在遊戲主畫面下方點擊「SAVE」按鈕。

#### 2. 事件監聽（主程式層）
- **檔案**：src/Main.App.java
- **程式碼片段**：
```java
game.getGameUI().getSaveButton().addActionListener(e -> {
    Continue.saveGame(
        game.getCurrentLevel(),
        game.getPlayerX(),
        game.getPlayerY()
    );
    JOptionPane.showMessageDialog(frame, "存檔成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
});
```
- **說明**：
  - 這段程式碼在 Main.App.java 的 main 方法中，於遊戲初始化後設定。
  - 當 SAVE 按鈕被點擊時，會呼叫 Continue.saveGame(...)，並彈出提示視窗。

#### 3. 取得遊戲狀態（邏輯層）
- **檔案**：src/game/GameController.java
- **方法**：
  - getCurrentLevel()：取得目前關卡
  - getPlayerX()：取得玩家 X 座標
  - getPlayerY()：取得玩家 Y 座標
- **說明**：
  - 這些方法由 Main.App.java 呼叫，用來取得目前要存檔的遊戲狀態。

#### 4. 執行存檔（存檔邏輯層）
- **檔案**：src/game/Continue.java
- **方法**：public static void saveGame(int level, int playerX, int playerY)
- **說明**：
  - 將傳入的關卡與玩家座標寫入 save.txt 檔案。
  - 若寫入過程有錯誤，會在終端顯示錯誤訊息。

#### 5. 存檔完成（UI回饋）
- **檔案**：src/Main.App.java
- **說明**：
  - 存檔成功後，會跳出 JOptionPane 視窗提示「存檔成功！」，讓玩家明確知道操作已完成。

---

### MENU（返回主選單）功能點擊後的完整流程

#### 1. 觸發點（UI層）
- **檔案**：src/gui/GameUI.java
- **元件**：backToMenuButton（JButton）
- **說明**：玩家在遊戲主畫面下方點擊「返回Menu」按鈕。

#### 2. 事件監聽（主程式層）
- **檔案**：src/Main.App.java
- **程式碼片段**：
```java
game.getGameUI().getBackToMenuButton().addActionListener(e -> {
    Container contentPane = frame.getContentPane();
    contentPane.remove(gamePanel);
    contentPane.add(loginPanelHolder[0]);
    contentPane.revalidate();
    contentPane.repaint();
});
```
- **說明**：
  - 這段程式碼在 Main.App.java 的 main 方法中，於遊戲初始化後設定。
  - 當 MENU 按鈕被點擊時，會將遊戲主畫面移除，切換回登入/主選單畫面（LoginPanel）。

#### 3. 畫面切換（UI層）
- **檔案**：src/Main.App.java
- **說明**：
  - 透過 Swing 的 remove/add/revalidate/repaint 方法，將主畫面切換回登入畫面。
  - 不會重置遊戲進度，玩家可隨時回來繼續。

---

### 流程圖（文字版）
1. 玩家點擊 MENU（GameUI.java 的 backToMenuButton）
2. Main.App.java 的事件監聽器被觸發
3. Main.App.java 執行畫面切換，將遊戲主畫面移除、登入畫面加回

---

### 各檔案職責總結
- **GameUI.java**：負責 MENU 按鈕的 UI 呈現與提供 getter
- **Main.App.java**：負責 MENU 按鈕的事件監聽、畫面切換
- **GameController.java**：提供目前遊戲狀態的查詢方法
- **Continue.java**：負責實際將資料寫入 save.txt 

---

## 如何將專案封裝成 .jar

### 1. 編譯所有原始碼
在專案根目錄下執行：
```bash
// ... existing code ...
javac -d bin src/Main/App.java src/game/*.java src/gui/*.java src/model/*.java
```
- `-d bin` 代表編譯後的 .class 檔案會放到 bin 目錄。
- 確保 bin 目錄已存在，否則請先建立：`mkdir bin`

### 2. 建立 manifest.txt
內容如下（假設主程式為 Main.App）：
```
Main-Class: Main.App
```

### 3. 打包成 jar 檔
在專案根目錄下執行：
```bash
jar cfm MazeGame.jar manifest.txt -C bin .
```
- `MazeGame.jar` 為輸出的 jar 檔名。
- `manifest.txt` 指定主程式。
- `-C bin .` 代表將 bin 目錄下所有 class 檔案打包進 jar。

### 4. 執行 jar 檔
```bash
java -jar MazeGame.jar
```

### 5. 注意事項
- 若有圖片、音效等資源，需一併打包或放在 jar 可存取的目錄。
- 若遇到「找不到主類別」錯誤，請檢查 manifest.txt 是否正確。
- 若有第三方函式庫，需用 `-cp` 或 `Class-Path` 指定。 
