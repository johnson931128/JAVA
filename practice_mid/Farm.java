public class Farm{
    public static void main(String[] args){

        Duck duck = new Duck();

        boolean canTheDuckFly = duck.canfly;
        if (canTheDuckFly == true){
            System.out.println("The duck can fly");
        }

        duck.quack();

        String food = "Hamburger";
        String message = duck.eat(food);
        System.out.println(message);
    }
}