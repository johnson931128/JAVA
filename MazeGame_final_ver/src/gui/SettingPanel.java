package gui;

import javax.swing.*;
import java.awt.*;

public class SettingPanel extends JPanel {
    private final JTextField widthField;
    private final JTextField heightField;

    public SettingPanel(int mazeWidth, int mazeHeight) {
        setLayout(new GridLayout(2, 2, 10, 10));
        add(new JLabel("Maze Width (Rows):"));
        widthField = new JTextField(String.valueOf(mazeWidth));
        add(widthField);
        add(new JLabel("Maze Height (Cols):"));
        heightField = new JTextField(String.valueOf(mazeHeight));
        add(heightField);
    }

    public int getMazeWidth() throws NumberFormatException {
        return Integer.parseInt(widthField.getText());
    }

    public int getMazeHeight() throws NumberFormatException {
        return Integer.parseInt(heightField.getText());
    }
}