package leaderboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@NoArgsConstructor
public class LeaderboardHelper {
    private long id;
    private List<Leaderboard> leaderboardList;

    public void initIdAndList(ObjectMapper objectMapper, String leaderboardFileName) throws IOException {
        File file = new File(leaderboardFileName);
        if (file.length() == 0) {
            leaderboardList = new ArrayList<>();
            id = 0L;
        } else {
            leaderboardList = objectMapper.readValue(new FileReader(leaderboardFileName),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Leaderboard.class));
            var maxId = leaderboardList.stream().map(Leaderboard::getId).max(Long::compareTo).orElse(0L);
            id = maxId + 1;
        }
    }

    public Leaderboard leaderboardBuilder(LocalDateTime startTime, String winner, int winnerStep, LocalDateTime finnishTime) {
        return Leaderboard.builder()
                .id(this.id)
                .start(startTime.getYear() + " " + startTime.getMonthValue() + " " +
                        startTime.getDayOfMonth() + " " + startTime.getHour() + " " +
                        startTime.getMinute() + " " + startTime.getSecond())
                .winner(winner)
                .step(winnerStep)
                .end(finnishTime.getYear() + " " + finnishTime.getMonthValue() + " " +
                        startTime.getDayOfMonth() + " " + finnishTime.getHour() + " " +
                        finnishTime.getMinute() + " " + finnishTime.getSecond())
                .duration((finnishTime.getHour() * 3600 + finnishTime.getMinute() * 60 + finnishTime.getSecond())
                        - (startTime.getHour() * 3600 + startTime.getMinute() * 60 + startTime.getSecond()))
                .build();
    }

    public void addLeaderboardToList(Leaderboard leaderboard) {
        var sameName = leaderboardList.stream().
                filter(leaderboard1 -> leaderboard1.getWinner().equals(leaderboard.getWinner())).findFirst();
        if (sameName.isPresent()) {
            if (sameName.get().getStep() > leaderboard.getStep()) {
                leaderboardList.remove(sameName.get());
                leaderboardList.add(leaderboard);
            }
        } else
            leaderboardList.add(leaderboard);
    }

    public List<Leaderboard> orderLeaderboardListByStepThenByDuration() {
        return leaderboardList.stream().sorted(Comparator.comparing(
                Leaderboard::getStep).thenComparing(Leaderboard::getDuration)).toList();
    }

    public void clearJson(ObjectMapper objectMapper, String leaderboardFileName) throws IOException {
        objectMapper.writeValue(new FileWriter(leaderboardFileName), new ArrayList<>());
    }
}

