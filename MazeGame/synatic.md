# Maze Game 專案 Java 語法與用法總整理（synatic.md）

---

## 1. 基本語法與關鍵字（語意與範例）
- `public` / `private` / `protected`：存取修飾子，決定類別/方法/變數的可見範圍。
  - 例：`public class App { ... }`、`private int x;`
- `class`：定義一個類別。
  - 例：`public class Player { ... }`
- `interface`：定義介面，規範類別必須實作的方法。
  - 例：`public interface ActionListener { ... }`
- `enum`：定義列舉型態。
  - 例：`enum Direction { UP, DOWN, LEFT, RIGHT }`
- `abstract`：抽象類別/方法，不能直接實例化，必須被子類別繼承。
- `implements`：類別實作介面。
  - 例：`class MyPanel implements ActionListener { ... }`
- `extends`：類別繼承父類別。
  - 例：`class MazePanel extends JPanel { ... }`
- `static`：靜態成員，屬於類別本身而非物件。
  - 例：`public static void main(String[] args)`
- `final`：不可變、不可被覆寫。
  - 例：`final int MAX = 100;`
- `void`：無回傳值的方法型態。
  - 例：`public void update() { ... }`
- `int` / `boolean` / `String` / `double` / `char`：基本資料型態與字串。
- `package` / `import`：套件宣告與匯入其他類別。
- `new`：建立新物件。
  - 例：`Player p = new Player(1, 1);`
- `return`：方法回傳值。
- `this`：指向目前物件本身。
- `super`：指向父類別。
- `if` / `else`：條件判斷。
- `switch` / `case` / `default`：多重條件分支。
- `for` / `while` / `do-while`：迴圈。
- `break` / `continue`：跳出/繼續迴圈。
- `null`：空參考。
- `true` / `false`：布林值。
- 陣列宣告：`int[] arr = new int[10];`（宣告一個長度為10的整數陣列）
- for-each：`for (Cell c : row) {...}`（遍歷陣列或集合）

---

## 2. 物件導向設計（OOP）
- 類別與物件：用 class 定義資料結構與行為，new 建立物件。
- 繼承（extends）：子類別繼承父類別，取得其屬性與方法。
- 介面實作（implements）：類別承諾實作介面規範的方法。
- 多型：父類別型別可指向子類別物件，提升彈性。
- 建構子：初始化物件時自動呼叫的方法。
- Getter/Setter：取得/設定物件屬性的方法。
- 成員變數：屬於物件的資料。
- 區域變數：只在方法/區塊內有效的變數。

---

## 3. Swing/GUI 元件與佈局
- `JFrame`：主視窗。
- `JPanel`：面板，可作為容器。
- `JButton`：按鈕。
- `JLabel`：標籤。
- `JOptionPane`：彈出對話框。
- `BoxLayout`：元件垂直/水平排列。
- `BorderLayout`：分為東西南北中五區。
- `Dimension`：尺寸物件。
- `Container`：Swing 容器基礎類別。
- `setLayout(...)`：設定佈局管理器。
- `add(...)` / `remove(...)`：新增/移除元件。
- `revalidate()` / `repaint()`：重新驗證/重繪畫面。
- `setPreferredSize(...)`：設定元件建議大小。
- `setFont(...)`：設定字型。
- `setBackground(...)`：設定背景色。
- `setFocusable(true)`：允許元件取得焦點。
- `requestFocusInWindow()`：請求焦點。
- `SwingUtilities.invokeLater(...)`：確保 UI 操作在 Swing 執行緒執行。

---

## 4. 事件監聽與 Lambda
- `ActionListener`：按鈕等元件的事件監聽介面。
- `KeyListener` / `KeyAdapter`：鍵盤事件監聽。
- `addActionListener(e -> {...})`：用 lambda 寫法註冊事件。
- `addKeyListener(new KeyAdapter() {...})`：匿名類別註冊鍵盤事件。
- `Runnable`：可執行的程式區塊，常用於執行緒或 UI 切換。
- Lambda 表達式：`() -> {...}`，簡化匿名類別寫法。
- 事件物件：`ActionEvent`（按鈕）、`KeyEvent`（鍵盤）。

---

## 5. IO/檔案操作
- `File`：檔案物件。
- `PrintWriter`：文字檔寫入。
- `BufferedReader` / `FileReader`：文字檔讀取。
- 檔案寫入：`new PrintWriter("save.txt")`，用 `out.println(...)` 寫入。
- 檔案讀取：`new BufferedReader(new FileReader(file))`，用 `in.readLine()` 讀取。
- 逐行讀取：`while ((line = in.readLine()) != null) {...}`

---

## 6. 例外處理
- try-catch 區塊：捕捉並處理執行時可能發生的錯誤。
  ```java
  try {
      // 可能拋出例外的程式
  } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
  }
  ```
- 多型 catch：`catch (IOException | NumberFormatException e) {...}`，同時捕捉多種例外（本專案未用到）。

---

## 7. 其他常用語法與技巧
- `Timer`（javax.swing）：定時器，每隔一段時間自動執行指定程式。
- `ArrayList`、`List`：動態陣列集合（本專案主要用陣列）。
- `Math.min(...)`、`Math.max(...)`：數值比較。
- `String.format(...)`：格式化字串。
- `String.substring(...)`、`String.startsWith(...)`：字串處理。
- `System.out.println(...)`、`System.err.println(...)`：印出訊息到主控台。
- `JOptionPane.showMessageDialog(...)`：顯示訊息對話框。
- `JOptionPane.showConfirmDialog(...)`：顯示確認對話框。
- `for (int i = 1; i < level; i++) ...`：for 迴圈控制。
- `switch (keyCode) { ... }`：鍵盤事件處理。
- `@Override`：標註覆寫父類別/介面的方法。

---

## 8. 設計模式與架構
- MVC 分層（Model-View-Controller）：將資料、畫面、控制邏輯分開，提升維護性。
  - Model：資料與邏輯（Cell, Player, MazeGenerator, GameState）
  - View：畫面（MazePanel, GameUI, LoginPanel）
  - Controller：流程控制（GameController, App）
- 單一職責原則：每個類別只做一件事，降低耦合。

---

> 本文件可持續補充，若有新語法、API、設計模式等請隨時加入！ 