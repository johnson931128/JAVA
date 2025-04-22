public class ExceptionDemo {
    
    // 使用throws關鍵字聲明方法可能拋出的異常
    public static void checkAge(int age) throws IllegalArgumentException {
        System.out.println("驗證年齡: " + age);
        
        // 使用throw關鍵字手動拋出異常
        if (age < 0) {
            throw new IllegalArgumentException("年齡不能為負數");
        }
        
        if (age > 120) {
            throw new IllegalArgumentException("年齡不能超過120歲");
        }
        
        System.out.println("年齡有效: " + age);
    }
    
    // 使用throws聲明方法可能拋出的已檢查異常
    public static void readFile(String filename) throws java.io.IOException {
        if (filename == null || filename.isEmpty()) {
            throw new java.io.IOException("檔案名不能為空");
        }
        
        // 模擬檔案讀取操作
        System.out.println("讀取檔案: " + filename);
    }
    
    public static void main(String[] args) {
        // 示例1: 處理運行時異常
        try {
            checkAge(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("捕獲到異常: " + e.getMessage());
        }
        
        try {
            checkAge(25);
        } catch (IllegalArgumentException e) {
            System.out.println("這裡不會執行，因為沒有異常拋出");
        }
        
        // 示例2: 處理已檢查異常
        try {
            readFile("");
        } catch (java.io.IOException e) {
            System.out.println("捕獲到IO異常: " + e.getMessage());
        }
        
        // 示例3: 不捕獲異常，而是向上傳播
        try {
            methodWithPropagatedExceptions();
        } catch (Exception e) {
            System.out.println("在main方法中捕獲到異常: " + e.getMessage());
        }
    }
    
    // 示範異常的傳播
    public static void methodWithPropagatedExceptions() throws Exception {
        try {
            readFile(null);
        } catch (java.io.IOException e) {
            // 捕獲後再次拋出，可以添加更多上下文信息
            throw new Exception("處理檔案時發生錯誤: " + e.getMessage(), e);
        }
    }
} 