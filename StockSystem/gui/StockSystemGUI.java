package gui;

import javax.swing.*;
import provider.StockProvider;
import model.Stock;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StockSystemGUI extends JPanel {
    private StockProvider stockProvider;
    private JTextField stockCodeField;
    private JTextArea resultArea;

    public StockSystemGUI(StockProvider stockProvider) {
        this.stockProvider = stockProvider;
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        stockCodeField = new JTextField(10);
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String stockCode = stockCodeField.getText();
                stockProvider.getStock(stockCode).ifPresentOrElse(
                    stock -> resultArea.setText("Stock: " + stock.getName() + "\nPrice: $" + stock.getPrice()),
                    () -> resultArea.setText("Stock not found.")
                );
            }
        });

        inputPanel.add(new JLabel("Enter Stock Code:"));
        inputPanel.add(stockCodeField);
        inputPanel.add(searchButton);

        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
} 