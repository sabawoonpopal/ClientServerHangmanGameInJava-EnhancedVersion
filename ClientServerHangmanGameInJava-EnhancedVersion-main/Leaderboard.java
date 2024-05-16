import java.util.HashMap;
import java.util.Map;

public class Leaderboard {
    private final Map<String, Integer> wins = new HashMap<>();

    // Synchronize methods to make them thread-safe
    public synchronized void addWin(String clientName) {
        wins.put(clientName, wins.getOrDefault(clientName, 0) + 1);
    }

    public synchronized int getWins(String clientName) {
        return wins.getOrDefault(clientName, 0);
    }

    public synchronized String getLeaderboard() {
        StringBuilder leaderboard = new StringBuilder("Leaderboard:\n");
        for (Map.Entry<String, Integer> entry : wins.entrySet()) {
            leaderboard.append(entry.getKey()).append(": ").append(entry.getValue()).append(" win(s)\n");
        }
        return leaderboard.toString();
    }
}
