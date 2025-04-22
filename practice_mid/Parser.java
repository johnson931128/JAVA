import java.util.Scanner;
import java.util.StringTokenizer;

public class Parser {
    public static void main(String args[]) {
        // 建立 Scanner 物件，讀取使用者輸入
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter a sentence and I'll display each word you entered: ");
        
        // 取得使用者輸入的句子
        String sentence = keyboard.nextLine();
        
        // 使用 StringTokenizer 分割字串（根據空格）
        StringTokenizer tk = new StringTokenizer(sentence, " ");
        
        // 顯示分割後的 tokens
        System.out.println("Here are the tokens: ");
        while (tk.hasMoreTokens()) {
            System.out.println(tk.nextToken());
        }
        
        // Close the Scanner to prevent resource leak
        keyboard.close();
    }
}
