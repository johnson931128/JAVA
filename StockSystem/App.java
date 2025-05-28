import gui.StockSystemGUI;
import javax.swing.*;
import provider.LocalStockProvider;

public class App {
    public static void main(String[] args) {
        LocalStockProvider localStockProvider = new LocalStockProvider();
        StockSystemGUI gui = new StockSystemGUI(localStockProvider);
        JFrame frame = new JFrame("Stock System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gui);
        frame.pack();
        frame.setVisible(true);
    }
}



