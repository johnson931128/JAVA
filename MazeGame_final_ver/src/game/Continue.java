package game;

import Main.App;

import javax.swing.*;
import java.io.*;

public class Continue {
    private static final String SAVE_FILE = "save.txt";

    public static void saveGame(int level, int playerX, int playerY, long mazeSeed, int width, int height) {
        try (PrintWriter out = new PrintWriter(SAVE_FILE)) {
            out.println("level=" + level);
            out.println("playerX=" + playerX);
            out.println("playerY=" + playerY);
            out.println("mazeSeed=" + mazeSeed);
            out.println("width=" + width);
            out.println("height=" + height);
        } catch (Exception e) {
            System.err.println("Error saving game: " + e.getMessage());
        }

        try (FileWriter writer = new FileWriter("src/environment/config.txt")) {
            writer.write("MAZE_WIDTH=" + width + "\n");
            writer.write("MAZE_HEIGHT=" + height + "\n");
            App.setMazeWidth(width);
            App.setMazeHeight(height);
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Failed to save settings to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static SaveData loadGame(int currentRows, int currentCols) {
        File file = new File(SAVE_FILE);
        if (!file.exists()) return null;
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            int level = 1, x = 1, y = 1, width = 0, height = 0;
            long mazeSeed = 0;
            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("level=")) level = Integer.parseInt(line.substring(6));
                if (line.startsWith("playerX=")) x = Integer.parseInt(line.substring(8));
                if (line.startsWith("playerY=")) y = Integer.parseInt(line.substring(8));
                if (line.startsWith("mazeSeed=")) mazeSeed = Long.parseLong(line.substring(9));
                if (line.startsWith("width=")) width = Integer.parseInt(line.substring(6));
                if (line.startsWith("height=")) height = Integer.parseInt(line.substring(7));
            }
            if (width == 0 || height == 0) {
                JOptionPane.showMessageDialog(null, "沒有存檔可繼續，請先開始新遊戲！", "提示", JOptionPane.INFORMATION_MESSAGE);
                return null;
            }
            if (width != currentRows || height != currentCols) {
                JOptionPane.showMessageDialog(null, "存檔的迷宮尺寸 (" + width + ", " + height + ") 與目前設定不符！", "錯誤", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            return new SaveData(level, x, y, mazeSeed, width, height);
        } catch (Exception e) {
            System.err.println("Error loading game: " + e.getMessage());
            return null;
        }
    }

    // 存檔資料結構
    public static class SaveData {
        public final int level;
        public final int playerX;
        public final int playerY;
        public final long mazeSeed;
        public final int width;
        public final int height;
        public SaveData(int level, int playerX, int playerY, long mazeSeed, int width, int height) {
            this.level = level;
            this.playerX = playerX;
            this.playerY = playerY;
            this.mazeSeed = mazeSeed;
            this.width = width;
            this.height = height;
        }
    }
} 