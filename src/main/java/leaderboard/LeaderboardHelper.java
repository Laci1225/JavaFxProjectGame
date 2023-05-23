package leaderboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Helper class read JSON files and operate on it.
 */
@Data
@NoArgsConstructor
public class LeaderboardHelper {
    private long id;
    private List<Leaderboard> leaderboardList;

    /**
     * Initializes the ID and leaderboard list.
     * If the file is empty, a new {@code leaderboardList} is created and the ID is set to 0
     * Otherwise, the existing {@code leaderboardList} is loaded, and the ID is set to {@code maxId} + 1
     *
     * @param objectMapper        the ObjectMapper instance used for JSON deserialization
     * @param leaderboardFileName the name of the JSON file containing leaderboard data
     * @throws IOException if an I/O error occurs while reading the JSON file
     */
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

    /**
     * Builds a {@link Leaderboard} with {@link lombok.Builder}.
     *
     * @param startTime  the start time of the game
     * @param winner     the name of the winner
     * @param winnerStep the winning steps
     * @param finishTime the finish time of the game
     * @return a constructed Leaderboard object
     */
    public Leaderboard leaderboardBuilder(LocalDateTime startTime, String winner, int winnerStep, LocalDateTime finishTime) {
        int durationSeconds = (int) Duration.between(startTime, finishTime).getSeconds();

        return Leaderboard.builder()
                .id(this.id)
                .start(formatDateTime(startTime))
                .winner(winner)
                .step(winnerStep)
                .duration(durationSeconds)
                .build();
    }

    /**
     * Adds a {@link Leaderboard} object to {@code leaderboardList}.
     * Replacing an existing entry with the same winner name
     * if the new entry has a lower step count.
     *
     * @param leaderboard a {@link Leaderboard} object to add
     */
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

    /**
     * Orders the {@code leaderboardList} by step then by duration.
     *
     * @return the ordered {@link Leaderboard} list
     */
    public List<Leaderboard> orderLeaderboardListByStepThenByDuration() {
        return leaderboardList.stream().sorted(Comparator.comparing(
                Leaderboard::getStep).thenComparing(Leaderboard::getDuration)).toList();
    }

    /**
     * Clears the JSON file by adding an empty {@link ArrayList} to it.
     *
     * @param objectMapper        the ObjectMapper instance used for JSON deserialization
     * @param leaderboardFileName the name of the JSON file containing leaderboard data
     * @throws IOException if an I/O error occurs while reading the JSON file
     */
    public void clearJson(ObjectMapper objectMapper, String leaderboardFileName) throws IOException {
        objectMapper.writeValue(new FileWriter(leaderboardFileName), new ArrayList<>());
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return String.format("%d.%d.%d %02d:%02d:%02d",
                dateTime.getYear(),
                dateTime.getMonthValue(),
                dateTime.getDayOfMonth(),
                dateTime.getHour(),
                dateTime.getMinute(),
                dateTime.getSecond());
    }
}

