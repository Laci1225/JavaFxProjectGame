package leaderboard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Leaderboard {

    private String start;
    private String winner;
    private Integer step;
    private String end;
    private Integer duration;

    @JsonCreator
    public Leaderboard(@JsonProperty("start") String start,
                       @JsonProperty("winner") String winner,
                       @JsonProperty("step") int step,
                       @JsonProperty("end") String end,
                       @JsonProperty("duration") Integer duration) {
        this.start = start;
        this.winner = winner;
        this.step = step;
        this.end = end;
        this.duration = duration;
    }
}
