import gui.StockSystemGUI;
import provider.LocalStockProvider;
import provider.RemoteStockProvider;
import provider.StockProvider;
import javax.swing.*;

public class App {
    public static void main(String[] args) {
        
        LocalStockProvider localStockProvider = new LocalStockProvider();
        RemoteStockProvider remoteStockProvider = new RemoteStockProvider();


        StockSystemGUI gui = new StockSystemGUI(localStockProvider);


        JFrame frame = new JFrame("Stock System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gui);
        frame.pack();
        frame.setVisible(true);
    }
}



