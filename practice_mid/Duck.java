public class Duck {
    public boolean canfly = false;
    public void quack() {
        System.out.println("Quck!!");
    }

    public String eat(String food){
        String message = "Thank you !The "+food+" is good";
        return message;
    }
}
