public class Example {
    // call by value
    public static void modifyPrimitive(int num){
        num = 10;
    }

    public static void modifyObject(StringBuilder text){
        text.append(" word");
    }


    public static void main(String[] args){
        int number = 5;
        modifyPrimitive(number);
        System.out.println(number);

        StringBuilder message = new StringBuilder("Hello");
        modifyObject(message);
        System.out.println(message);
    }   
}
