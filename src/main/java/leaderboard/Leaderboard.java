package leaderboard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;


/**
 * Represents a scoreboard for the ProjectGame results.
 */
@Builder
@Getter
public class Leaderboard {
    private Long id;
    private String start;
    private String winner;
    private Integer step;
    private Integer duration;

    /**
     * Constructs a new Leaderboard object.
     *
     * @param id       the ID of the leaderboard
     * @param start    the start time of the game
     * @param winner   the name of the winner
     * @param step     the winning step count
     * @param duration the duration of the game
     */
    @JsonCreator
    public Leaderboard(@JsonProperty("id") Long id,
                       @JsonProperty("start") String start,
                       @JsonProperty("winner") String winner,
                       @JsonProperty("step") Integer step,
                       @JsonProperty("duration") Integer duration) {
        this.id = id;
        this.start = start;
        this.winner = winner;
        this.step = step;
        this.duration = duration;
    }
}
