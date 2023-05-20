package leaderboard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class Leaderboard {
    private Long id;
    private String start;
    private String winner;
    private Integer step;
    private String end;
    private Long duration;


    @JsonCreator
    public Leaderboard(@JsonProperty("id") Long id,
                       @JsonProperty("start") String start,
                       @JsonProperty("winner") String winner,
                       @JsonProperty("step") Integer step,
                       @JsonProperty("end") String end,
                       @JsonProperty("duration") Long duration) {
        this.id = id;
        this.start = start;
        this.winner = winner;
        this.step = step;
        this.end = end;
        this.duration = duration;
    }
}
