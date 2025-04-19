public class TestDemo {
    public static void main(String[] args) {
        try {
            // 嘗試加載 StaticInitErrorDemo 類別
            // Attempt to load the StaticInitErrorDemo class
            Class.forName("StaticInitErrorDemo");
        } catch (ClassNotFoundException e) {
            System.out.println("類別未找到！/ Class not found!");
        } catch (ExceptionInInitializerError e) {
            // 捕獲 ExceptionInInitializerError
            // Catch ExceptionInInitializerError
            System.err.println("捕獲錯誤 / Caught error: " + e);
            System.err.println("根本原因 / Root cause: " + e.getCause());
        }
    }
}