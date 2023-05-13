package leaderboard;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class IdAndLeaderboardList {
    private long id;
    private List<Leaderboard> leaderboard;
}

