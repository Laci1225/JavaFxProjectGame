package leaderboard;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Leaderboard {

    private String start;
    private String winner;
    private Integer step;
    private String end;
}
