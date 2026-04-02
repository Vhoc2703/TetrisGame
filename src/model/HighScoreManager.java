package model;

import java.io.*;
import java.util.*;

/**
 * Quản lý điểm cao - Lưu bằng File Binary (highscore.dat)
 */
public class HighScoreManager {
    
    private static final int MAX_SCORES = 10;
    private static final String FILE_NAME = "highscore.dat";
    
    private List<HighScoreEntry> highScores;
    
    public HighScoreManager() {
        this.highScores = new ArrayList<>();
        loadHighScores();
    }
    
    public int addScore(String playerName, int score) {
        if (highScores.size() < MAX_SCORES || score > highScores.get(highScores.size() - 1).getScore()) {
            HighScoreEntry newEntry = new HighScoreEntry(playerName, score, new Date());
            highScores.add(newEntry);
            highScores.sort((a, b) -> b.getScore() - a.getScore());
            
            if (highScores.size() > MAX_SCORES) {
                highScores = highScores.subList(0, MAX_SCORES);
            }
            
            int rank = -1;
            for (int i = 0; i < highScores.size(); i++) {
                if (highScores.get(i).getPlayerName().equals(playerName) && 
                    highScores.get(i).getScore() == score) {
                    rank = i + 1;
                    break;
                }
            }
            
            saveHighScores();
            return rank;
        }
        return -1;
    }
    
    public List<HighScoreEntry> getHighScores() {
        return new ArrayList<>(highScores);
    }
    
    public void clearAllScores() {
        highScores.clear();
        saveHighScores();
    }
    
    private void saveHighScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(highScores);
        } catch (IOException e) {
            System.err.println("Lỗi lưu điểm cao: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadHighScores() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            createDefaultScores();
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            highScores = (List<HighScoreEntry>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            createDefaultScores();
        }
    }
    
    private void createDefaultScores() {
        highScores.clear();
        highScores.add(new HighScoreEntry("Admin", 5000, new Date()));
        highScores.add(new HighScoreEntry("Player1", 2500, new Date()));
        highScores.add(new HighScoreEntry("Player2", 1800, new Date()));
        highScores.add(new HighScoreEntry("Player3", 1200, new Date()));
        highScores.add(new HighScoreEntry("Player4", 800, new Date()));
        saveHighScores();
    }
    
    public static class HighScoreEntry implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String playerName;
        private final int score;
        private final Date date;
        
        public HighScoreEntry(String playerName, int score, Date date) {
            this.playerName = playerName;
            this.score = score;
            this.date = date;
        }
        
        public String getPlayerName() { return playerName; }
        public int getScore() { return score; }
        public Date getDate() { return date; }
    }
}