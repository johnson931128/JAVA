package util;

import javazoom.jl.player.Player;
import java.io.InputStream;

public class AudioPlayer {
    private boolean paused = false;
    private final Object pauseLock = new Object();
    private Player player;
    private Thread playThread;
    private volatile boolean running = false;

    public void playLoop(String resourcePath) {
        stop();
        running = true;
        playThread = new Thread(() -> {
            while (running) {
                try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
                    if (is == null) {
                        System.err.println("音樂檔案找不到: " + resourcePath);
                        break;
                    }
                    player = new Player(is) {
                        @Override
                        public boolean play(int frames) {
                            try {
                                for (int i = 0; i < frames && running; i++) {
                                    synchronized (pauseLock) {
                                        while (paused) pauseLock.wait();
                                    }
                                    try {
                                        super.play(1);
                                    } catch (javazoom.jl.decoder.JavaLayerException e) {
                                        e.printStackTrace();
                                        return false;
                                    }
                                }
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            return running;
                        }
                    };
                    player.play();
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        playThread.start();
    }

    public void stop() {
        running = false;
        if (player != null) {
            player.close();
        }
        if (playThread != null && playThread.isAlive()) {
            playThread.interrupt();
        }
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
        synchronized (pauseLock) { pauseLock.notifyAll(); }
    }

    public boolean isPaused() { return paused; }
}