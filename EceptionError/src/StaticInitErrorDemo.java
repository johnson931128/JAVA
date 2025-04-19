/**
 * StaticInitErrorDemo 類別展示了 Java 中靜態初始化的行為，以及在靜態初始化過程中
 * 發生異常時的處理方式。
 *
 * <p>此類別包含一個靜態變數 {@code data}，該變數透過靜態方法 {@code initializeData()} 
 * 進行初始化。而 {@code initializeData()} 方法故意拋出 {@link RuntimeException}，
 * 以模擬靜態初始化失敗的情況。此外，類別中還包含一個靜態區塊，用於展示類別載入時的執行順序。</p>
 *
 * <p>當執行 {@code main} 方法時，程式會嘗試使用 {@link Class#forName(String)} 載入類別。
 * 如果靜態初始化失敗，將拋出 {@link ExceptionInInitializerError}，並在 {@code main} 方法中
 * 捕捉並處理該異常。</p>
 *
 * <p>此範例說明了以下幾個重點：</p>
 * <ul>
 *   <li>靜態變數會在靜態區塊執行之前初始化。</li>
 *   <li>如果靜態變數初始化失敗，類別載入過程將拋出 {@link ExceptionInInitializerError}。</li>
 *   <li>{@code ExceptionInInitializerError} 會包裹導致失敗的原始異常。</li>
 * </ul>
 *
 * <p>注意：{@code main} 方法中還包含一個 {@link ClassNotFoundException} 的捕捉區塊，
 * 用於處理類別無法找到的情況，儘管在此範例中不太可能發生。</p>
 */
public class StaticInitErrorDemo {

    private static String data = initializeData();

    static {
        System.out.println("Static block executed successfully.");
    }

    private static String initializeData() {
        throw new RuntimeException("Static variable initialization failed!");
    }

    public static void main(String[] args) {
        try {
            // 嘗試觸發類別載入
            Class.forName("StaticInitErrorDemo");
            System.out.println("Data: " + data); // Access the static field to use it
        } catch (ExceptionInInitializerError e) {
            System.out.println("Caught ExceptionInInitializerError:");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found!");
        }
    }
}