import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class StringMethodsDemo {

    public static void main(String[] args) {
        String str = "Hello World! Hello Java! ";
        String strLower = "hello world! hello java! ";
        String emptyStr = "";
        String spacedStr = "   Trim Me   ";

        // 1. charAt(int index)
        // 返回指定索引處的字符 (char)。索引從 0 開始。
        char ch = str.charAt(1); // 取得索引 1 的字元
        System.out.println("1. charAt(1): " + ch); // 輸出: e

        // 2. codePointAt(int index)
        // 返回指定索引處的字符的 Unicode 碼點 (整數)。
        int unicode = str.codePointAt(0); // 取得索引 0 ('H') 的 Unicode 碼點
        System.out.println("2. codePointAt(0): " + unicode); // 輸出: 72

        // 3. codePointBefore(int index)
        // 返回指定索引之前的字符的 Unicode 碼點。
        int unicodeBefore = str.codePointBefore(1); // 取得索引 1 之前 ('H') 的 Unicode 碼點
        System.out.println("3. codePointBefore(1): " + unicodeBefore); // 輸出: 72

        // 4. codePointCount(int beginIndex, int endIndex)
        // 返回此 String 指定文本範圍內的 Unicode 碼點數。
        int count = str.codePointCount(0, 5); // 計算索引 0 到 4 ("Hello") 的碼點數
        System.out.println("4. codePointCount(0, 5): " + count); // 輸出: 5

        // 5. compareTo(String anotherString)
        // 按字典順序比較兩個字串。
        // 如果此字串等於參數字串，返回 0；
        // 如果此字串小於參數字串，返回負值；
        // 如果此字串大於參數字串，返回正值。 (區分大小寫)
        String strCompare = "Hello World!";
        System.out.println("5. compareTo(\"Hello World!\"): " + str.compareTo(strCompare)); // 輸出: 13 (因為 str 比較長且不同)
        System.out.println("5. compareTo(\"Hello A\"): " + "Hello B".compareTo("Hello A")); // 輸出: 1 ('B' > 'A')
        System.out.println("5. compareTo(\"Hello C\"): " + "Hello B".compareTo("Hello C")); // 輸出: -1 ('B' < 'C')
        System.out.println("5. compareTo(\"hello world! hello java! \"): " + str.compareTo(strLower)); // 輸出: -32 ('H' < 'h')

        // 6. compareToIgnoreCase(String str)
        // 按字典順序比較兩個字串，忽略大小寫。
        System.out.println("6. compareToIgnoreCase(strLower): " + str.compareToIgnoreCase(strLower)); // 輸出: 0 (忽略大小寫後相等)

        // 7. concat(String str)
        // 將指定字串連接到此字串的結尾。
        String concatenated = str.concat(" Have fun!");
        System.out.println("7. concat(): " + concatenated); // 輸出: Hello World! Hello Java!  Have fun!

        // 8. contains(CharSequence s)
        // 檢查字串是否包含指定的 CharSequence (字元序列)。
        boolean hasHello = str.contains("Hello");
        boolean hasBye = str.contains("Bye");
        System.out.println("8. contains(\"Hello\"): " + hasHello); // 輸出: true
        System.out.println("8. contains(\"Bye\"): " + hasBye);     // 輸出: false

        // 9. contentEquals(CharSequence cs) / contentEquals(StringBuffer sb)
        // 將此字串與指定的 CharSequence 或 StringBuffer 進行比較。內容相同則返回 true。
        StringBuffer sb = new StringBuffer("Hello World! Hello Java! ");
        StringBuilder sbuilder = new StringBuilder("Hello World! Hello Java! ");
        System.out.println("9. contentEquals(StringBuffer): " + str.contentEquals(sb));       // 輸出: true
        System.out.println("9. contentEquals(StringBuilder): " + str.contentEquals(sbuilder)); // 輸出: true
        System.out.println("9. contentEquals(\"Different\"): " + str.contentEquals("Different")); // 輸出: false

        // 10. copyValueOf(char[] data) / copyValueOf(char[] data, int offset, int count)
        // [靜態方法] 返回指定陣列中字符的字串表示形式。
        char[] charArray = {'J', 'a', 'v', 'a'};
        String fromChars = String.copyValueOf(charArray);
        String fromCharsPart = String.copyValueOf(charArray, 1, 2); // 從索引 1 開始，取 2 個字元
        System.out.println("10. copyValueOf(charArray): " + fromChars);       // 輸出: Java
        System.out.println("10. copyValueOf(charArray, 1, 2): " + fromCharsPart); // 輸出: av

        // 11. endsWith(String suffix)
        // 測試此字串是否以指定的後綴結尾。
        boolean endsWithSpace = str.endsWith(" ");
        boolean endsWithJava = str.endsWith("Java! ");
        System.out.println("11. endsWith(\" \"): " + endsWithSpace);     // 輸出: true
        System.out.println("11. endsWith(\"Java! \"): " + endsWithJava); // 輸出: true
        System.out.println("11. endsWith(\"World!\"): " + str.endsWith("World!")); // 輸出: false

        // 12. equals(Object anObject)
        // 將此字串與指定物件進行比較。內容相同且大小寫一致返回 true。
        String strSame = "Hello World! Hello Java! ";
        System.out.println("12. equals(strSame): " + str.equals(strSame));     // 輸出: true
        System.out.println("12. equals(strLower): " + str.equals(strLower));   // 輸出: false (大小寫不同)
        System.out.println("12. equals(null): " + str.equals(null));         // 輸出: false

        // 13. equalsIgnoreCase(String anotherString)
        // 將此 String 與另一個 String 比較，忽略大小寫。
        System.out.println("13. equalsIgnoreCase(strLower): " + str.equalsIgnoreCase(strLower)); // 輸出: true

        // 14. format(String format, Object... args)
        // [靜態方法] 使用指定的格式字串和參數返回格式化的字串。
        String name = "Alice";
        int age = 30;
        String formattedString = String.format("Name: %s, Age: %d", name, age);
        System.out.println("14. format(): " + formattedString); // 輸出: Name: Alice, Age: 30

        // 15. getBytes() / getBytes(Charset charset) / getBytes(String charsetName)
        // 使用平台默認/指定字符集將此 String 編碼為 byte 序列。
        try {
            byte[] bytesDefault = str.getBytes(); // 使用平台預設編碼
            byte[] bytesUTF8 = str.getBytes(StandardCharsets.UTF_8); // 使用 UTF-8 編碼
            byte[] bytesSpecific = str.getBytes("ISO-8859-1"); // 使用特定編碼名稱
            System.out.println("15. getBytes() (default length): " + bytesDefault.length);
            System.out.println("15. getBytes(UTF_8 length): " + bytesUTF8.length);
            System.out.println("15. getBytes(\"ISO-8859-1\" length): " + bytesSpecific.length);
        } catch (UnsupportedEncodingException e) {
            System.out.println("15. getBytes() Error: " + e.getMessage());
        }

        // 16. getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin)
        // 將字符從此字串複製到目標字符陣列中。
        char[] targetChars = new char[5];
        str.getChars(6, 11, targetChars, 0); // 將 "World" (索引 6 到 10) 複製到 targetChars 從索引 0 開始
        System.out.print("16. getChars(6, 11, target, 0): ");
        System.out.println(targetChars); // 輸出: World

        // 17. hashCode()
        // 返回此字串的哈希碼。
        int hashCode = str.hashCode();
        System.out.println("17. hashCode(): " + hashCode); // 輸出: 根據字串內容計算出的整數哈希值

        // 18. indexOf(int ch) / indexOf(int ch, int fromIndex) / indexOf(String str) / indexOf(String str, int fromIndex)
        // 返回指定字符/字串在此字串中第一次出現處的索引。可以指定起始搜索索引。找不到返回 -1。
        System.out.println("18. indexOf('o'): " + str.indexOf('o'));             // 輸出: 4 (第一個 'o')
        System.out.println("18. indexOf('o', 5): " + str.indexOf('o', 5));         // 輸出: 7 (從索引 5 開始找 'o')
        System.out.println("18. indexOf(\"Hello\"): " + str.indexOf("Hello"));       // 輸出: 0 (第一個 "Hello")
        System.out.println("18. indexOf(\"Hello\", 1): " + str.indexOf("Hello", 1)); // 輸出: 13 (從索引 1 開始找 "Hello")
        System.out.println("18. indexOf(\"Bye\"): " + str.indexOf("Bye"));         // 輸出: -1 (找不到)

        // 19. intern()
        // 返回字串物件的規範化表示形式 (從字串池 String Pool 中獲取)。
        String s1 = new String("PoolTest"); // 在堆上創建新對象
        String s2 = "PoolTest";           // 使用字串池中的對象
        String s3 = s1.intern();          // 從字串池獲取 s1 內容對應的對象
        System.out.println("19. s1 == s2: " + (s1 == s2)); // 輸出: false (不同對象)
        System.out.println("19. s2 == s3: " + (s2 == s3)); // 輸出: true (都指向字串池中的同一個對象)

        // 20. isEmpty()
        // 檢查字串是否為空 (長度為 0)。
        System.out.println("20. isEmpty() for str: " + str.isEmpty());         // 輸出: false
        System.out.println("20. isEmpty() for emptyStr: " + emptyStr.isEmpty()); // 輸出: true

        // 21. join(CharSequence delimiter, CharSequence... elements) / join(CharSequence delimiter, Iterable<? extends CharSequence> elements)
        // [靜態方法] 使用指定的分隔符將多個字串或一個 Iterable 中的字串連接起來。
        String joined = String.join(", ", "Apple", "Banana", "Orange");
        System.out.println("21. join(\", \", ...): " + joined); // 輸出: Apple, Banana, Orange
        List<String> fruits = Arrays.asList("Grape", "Lemon", "Lime");
        String joinedList = String.join(" | ", fruits);
        System.out.println("21. join(\" | \", List): " + joinedList); // 輸出: Grape | Lemon | Lime

        // 22. lastIndexOf(int ch) / lastIndexOf(int ch, int fromIndex) / lastIndexOf(String str) / lastIndexOf(String str, int fromIndex)
        // 返回指定字符/字串在此字串中最後一次出現處的索引。可以指定反向搜索的起始索引。找不到返回 -1。
        System.out.println("22. lastIndexOf('o'): " + str.lastIndexOf('o'));             // 輸出: 16 (最後一個 'o')
        System.out.println("22. lastIndexOf('o', 15): " + str.lastIndexOf('o', 15));     // 輸出: 7 (從索引 15 開始反向找 'o')
        System.out.println("22. lastIndexOf(\"Hello\"): " + str.lastIndexOf("Hello"));   // 輸出: 13 (最後一個 "Hello")
        System.out.println("22. lastIndexOf(\"Hello\", 12): " + str.lastIndexOf("Hello", 12)); // 輸出: 0 (從索引 12 開始反向找 "Hello")

        // 23. length()
        // 返回此字串的長度。
        System.out.println("23. length(): " + str.length()); // 輸出: 26

        // 24. matches(String regex)
        // 告知此字串是否匹配給定的正規表示式。
        String email = "test@example.com";
        String notEmail = "test-example.com";
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        System.out.println("24. matches(email regex) for email: " + email.matches(regex));       // 輸出: true
        System.out.println("24. matches(email regex) for notEmail: " + notEmail.matches(regex)); // 輸出: false

        // 25. offsetByCodePoints(int index, int codePointOffset)
        // 返回此 String 中從給定 index 處偏移 codePointOffset 個 Unicode 碼點的索引。
        // 假設 str = "Hi 🙂 there" (笑臉是一個碼點，但佔用兩個 char 位置)
        String complexStr = "Hi 🙂 there"; // 🙂 是 U+1F642
        // ' ' 在索引 2, 't' 在索引 5 (因為 🙂 佔了索引 3 和 4 的 char 位置)
        int offsetIndex = complexStr.offsetByCodePoints(2, 1); // 從索引 2 (' ') 偏移 1 個碼點
        System.out.println("25. offsetByCodePoints(2, 1) on \"Hi 🙂 there\": " + offsetIndex); // 輸出: 5 (指向 't')

        // 26. regionMatches(boolean ignoreCase, int toffset, String other, int ooffset, int len)
        // / regionMatches(int toffset, String other, int ooffset, int len)
        // 測試兩個字串區域是否相等。可以選擇是否忽略大小寫。
        String part = "World";
        // 比較 str 中從索引 6 開始的 5 個字符 ("World") 和 part 從索引 0 開始的 5 個字符 ("World")
        boolean regionMatchCase = str.regionMatches(6, part, 0, 5);
        // 忽略大小寫比較 str 中從索引 6 開始的 5 個字符 ("World") 和 "world"
        boolean regionMatchIgnoreCase = str.regionMatches(true, 6, "world", 0, 5);
        System.out.println("26. regionMatches(case sensitive): " + regionMatchCase);         // 輸出: true
        System.out.println("26. regionMatches(ignore case): " + regionMatchIgnoreCase);   // 輸出: true

        // 27. replace(char oldChar, char newChar) / replace(CharSequence target, CharSequence replacement)
        // 返回一個新字串，它是通過用 newChar/replacement 替換此字串中出現的所有 oldChar/target 而生成的。
        String replacedL = str.replace('l', 'X');
        String replacedHello = str.replace("Hello", "Hi");
        System.out.println("27. replace('l', 'X'): " + replacedL);       // 輸出: HeXXo WorXd! HeXXo Java!
        System.out.println("27. replace(\"Hello\", \"Hi\"): " + replacedHello); // 輸出: Hi World! Hi Java!

        // 28. replaceFirst(String regex, String replacement)
        // 使用給定的 replacement 替換此字串匹配給定正規表示式的第一個子字串。
        String replacedFirstHello = str.replaceFirst("Hello", "Greetings");
        System.out.println("28. replaceFirst(\"Hello\", \"Greetings\"): " + replacedFirstHello); // 輸出: Greetings World! Hello Java!

        // 29. replaceAll(String regex, String replacement)
        // 使用給定的 replacement 替換此字串所有匹配給定正規表示式的子字串。
        String replacedAllHello = str.replaceAll("Hello", "Yo");
        String replacedWhitespace = "Tab\tSpace Space".replaceAll("\\s+", "_"); // 將所有連續空白換成底線
        System.out.println("29. replaceAll(\"Hello\", \"Yo\"): " + replacedAllHello);       // 輸出: Yo World! Yo Java!
        System.out.println("29. replaceAll whitespace: " + replacedWhitespace); // 輸出: Tab_Space_Space

        // 30. split(String regex) / split(String regex, int limit)
        // 根據匹配給定的正規表示式來拆分此字串。limit 參數控制模式應用的次數。
        String toSplit = "one:two:three:four";
        String[] parts = toSplit.split(":");
        String[] partsLimited = toSplit.split(":", 3); // 最多分割成 3 部分
        System.out.println("30. split(\":\"): " + Arrays.toString(parts));             // 輸出: [one, two, three, four]
        System.out.println("30. split(\":\", 3): " + Arrays.toString(partsLimited)); // 輸出: [one, two, three:four]

        // 31. startsWith(String prefix) / startsWith(String prefix, int toffset)
        // 測試此字串是否以指定的前綴開始。可以指定開始比較的索引。
        System.out.println("31. startsWith(\"Hello\"): " + str.startsWith("Hello"));         // 輸出: true
        System.out.println("31. startsWith(\"World\"): " + str.startsWith("World"));         // 輸出: false
        System.out.println("31. startsWith(\"World\", 6): " + str.startsWith("World", 6)); // 輸出: true (從索引 6 開始比較)

        // 32. subSequence(int beginIndex, int endIndex)
        // 返回一個新的字符序列，它是此序列的一個子序列。
        CharSequence subSeq = str.subSequence(6, 11); // 從索引 6 到 10 ("World")
        System.out.println("32. subSequence(6, 11): " + subSeq); // 輸出: World

        // 33. substring(int beginIndex) / substring(int beginIndex, int endIndex)
        // 返回一個新字串，它是此字串的一個子字串。
        String subFrom6 = str.substring(6);       // 從索引 6 到結尾
        String sub6to11 = str.substring(6, 11);   // 從索引 6 到 10
        System.out.println("33. substring(6): " + subFrom6); // 輸出: World! Hello Java!
        System.out.println("33. substring(6, 11): " + sub6to11); // 輸出: World

        // 34. toCharArray()
        // 將此字串轉換為一個新的字符陣列。
        char[] charsFromString = str.toCharArray();
        System.out.println("34. toCharArray() (first 5): " + Arrays.toString(Arrays.copyOfRange(charsFromString, 0, 5))); // 輸出: [H, e, l, l, o]

        // 35. toLowerCase() / toLowerCase(Locale locale)
        // 使用預設/指定 Locale 的規則將此 String 中的所有字符都轉換為小寫。
        System.out.println("35. toLowerCase(): " + str.toLowerCase()); // 輸出: hello world! hello java!
        // 土耳其語的特殊情況：'I' 變 'ı'
        // System.out.println("35. toLowerCase(Turkish 'I'): " + "I".toLowerCase(new Locale("tr", "TR"))); // 輸出: ı

        // 36. toString()
        // 返回字串本身。主要用於保持一致性，因為所有物件都有 toString()。
        System.out.println("36. toString(): " + str.toString()); // 輸出: Hello World! Hello Java!

        // 37. toUpperCase() / toUpperCase(Locale locale)
        // 使用預設/指定 Locale 的規則將此 String 中的所有字符都轉換為大寫。
        System.out.println("37. toUpperCase(): " + str.toUpperCase()); // 輸出: HELLO WORLD! HELLO JAVA!
        // 土耳其語的特殊情況：'i' 變 'İ'
        // System.out.println("37. toUpperCase(Turkish 'i'): " + "i".toUpperCase(new Locale("tr", "TR"))); // 輸出: İ

        // 38. trim()
        // 返回一個副本，移除了字串首尾的空白字符。
        System.out.println("38. trim() on spacedStr: '" + spacedStr.trim() + "'"); // 輸出: 'Trim Me'

        // 39. valueOf(...)
        // [靜態方法] 返回各種基本類型、物件、字符陣列等的字串表示形式。
        String fromInt = String.valueOf(123);
        String fromDouble = String.valueOf(3.14);
        String fromBoolean = String.valueOf(true);
        String fromCharArrValue = String.valueOf(charArray); // charArray is {'J','a','v','a'}
        Object obj = new Object();
        String fromObject = String.valueOf(obj); // 會調用 obj.toString()

        System.out.println("39. valueOf(123): " + fromInt);           // 輸出: 123
        System.out.println("39. valueOf(3.14): " + fromDouble);       // 輸出: 3.14
        System.out.println("39. valueOf(true): " + fromBoolean);      // 輸出: true
        System.out.println("39. valueOf(charArray): " + fromCharArrValue); // 輸出: Java
        System.out.println("39. valueOf(Object): " + fromObject);      // 輸出: java.lang.Object@... (物件的預設 toString)
    }
}