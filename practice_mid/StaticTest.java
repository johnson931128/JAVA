public class StaticTest {
    public static int port = 80;
    public static void main(String[] args) {
        StaticTest obj1 = new StaticTest();
        StaticTest obj2 = new StaticTest();
        System.out.println(StaticTest.port);
        System.out.println(obj1.port);
        System.out.println(obj2.port);
        StaticTest.port = 1234;
        System.out.println(obj1.port);
        obj2.port = 5678;
        System.out.println(obj1.port);
    }
}