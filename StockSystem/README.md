# JAVA 股票查詢系統

## 專案簡介
本專案是一個簡易的股票查詢系統，具備本地與遠端查詢功能，並以 Swing 製作圖形化介面。使用者可輸入股票代碼，查詢本地預設股票或透過 API 查詢即時股價。

## 專案結構
- `App.java`：主程式，負責啟動 GUI 並注入本地股票供應者。
- `gui/StockSystemGUI.java`：Swing 製作的主視窗，提供股票查詢介面。
- `provider/StockProvider.java`：股票供應者介面，定義查詢股票的方法。
- `provider/LocalStockProvider.java`：本地股票供應者，內建三檔股票資料。
- `provider/RemoteStockProvider.java`：遠端股票供應者，連接 Alpha Vantage API 查詢即時股價。
- `model/Stock.java`：股票資料結構，包含名稱與價格。

## 開發流程
1. **需求分析與設計**
   - 決定以 Swing 製作簡易 GUI。
   - 設計 `StockProvider` 介面，方便擴充本地與遠端查詢。
   - 規劃本地與遠端兩種股票資料來源。
2. **資料結構與介面**
   - 實作 `Stock` 類別，封裝股票名稱與價格。
   - 定義 `StockProvider` 介面，統一查詢方法。
3. **本地供應者**
   - 實作 `LocalStockProvider`，以 `HashMap` 儲存三檔股票資料，實現查詢功能。
4. **遠端供應者**
   - 實作 `RemoteStockProvider`，利用 Java 內建 `HttpClient` 連接 Alpha Vantage API，解析 JSON 回傳即時股價。
   - 使用 `gson` 進行 JSON 解析。
5. **圖形化介面**
   - 實作 `StockSystemGUI`，提供輸入股票代碼、查詢與顯示結果的功能。
   - 將供應者注入 GUI，方便切換本地或遠端查詢。
6. **主程式整合**
   - 在 `App.java` 中建立 GUI 與供應者，啟動應用程式。

## 功能說明
- 啟動後會顯示股票查詢視窗。
- 使用者可輸入股票代碼（如 AAPL、TSLA、GOOGL）查詢本地股票資訊。
- 若要查詢即時股價，可擴充 GUI 並注入 `RemoteStockProvider`。

## 執行方式
1. 確認已安裝 JDK 17 以上版本。
2. 若需遠端查詢，請確保已加入 `gson` 函式庫。
3. 編譯所有 `.java` 檔案，執行 `App` 主程式。

---