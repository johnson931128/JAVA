import java.util.ArrayList;

public class WrapperExample {
    public static void main(String[] args) {
        // Step 1: 建立 ArrayList 來存儲整數（必須用 Integer，而非 int）
        ArrayList<Integer> numbers = new ArrayList<>();

        // Step 2: Autoboxing（基本型別 int -> 包裝類別 Integer）
        numbers.add(10); // int 自動轉換為 Integer
        numbers.add(20);
        numbers.add(30);
        numbers.add(40);
        numbers.add(50);

        // Step 3: 計算總和與平均值
        int sum = 0;
        for (Integer num : numbers) {
            sum += num; // Unboxing（Integer -> int）
        }
        
        double average = (double) sum / numbers.size(); // 計算平均值
        
        // Step 4: 顯示結果
        System.out.println("數字列表：" + numbers);
        System.out.println("總和：" + sum);
        System.out.println("平均值：" + average);
        
        // Step 5: 使用 Integer 的方法來比較數值
        Integer a = 100;
        Integer b = 200;
        
        System.out.println("最大值：" + Integer.max(a, b));
        System.out.println("最小值：" + Integer.min(a, b));
        System.out.println("a 與 b 相等？" + a.equals(b));
    }
}
