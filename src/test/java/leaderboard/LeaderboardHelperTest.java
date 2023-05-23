package leaderboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class LeaderboardHelperTest {
    private LeaderboardHelper leaderboardHelper;
    private ObjectMapper objectMapper;
    private String leaderboardFileName;
    private static File file;

    @BeforeEach
    public void setUp() {
        leaderboardHelper = new LeaderboardHelper();
        objectMapper = new ObjectMapper();
        leaderboardFileName = "json.json";
        file = new File(leaderboardFileName);
        if (file.exists()) {
            assertTrue(file.delete());
        }
    }

    @AfterAll
    static void deleteFile() {
        assertTrue(file.delete());
    }

    public Leaderboard addOneSampleLeaderboard() throws IOException {
        List<Leaderboard> leaderboardTest = new ArrayList<>();
        Leaderboard leaderboard = Leaderboard.builder()
                .id(0L).winner("Sample").step(10).duration(100).build();
        leaderboardTest.add(leaderboard);
        objectMapper.writeValue(file, leaderboardTest);
        return leaderboard;
    }

    @Test
    public void testInitIdAndList() throws IOException {


        leaderboardHelper.initIdAndList(objectMapper, leaderboardFileName);

        assertEquals(0L, leaderboardHelper.getId());
        assertEquals(0, leaderboardHelper.getLeaderboardList().size());
        assertNotEquals(null, leaderboardHelper.getLeaderboardList());
        assertNotNull(leaderboardHelper.getLeaderboardList());

        addOneSampleLeaderboard();
        leaderboardHelper.initIdAndList(objectMapper, leaderboardFileName);
        assertEquals(1L, leaderboardHelper.getId());
        assertEquals(1, leaderboardHelper.getLeaderboardList().size());
        assertNotEquals(null, leaderboardHelper.getLeaderboardList());


    }

    @Test
    public void testLeaderboardBuilder() {
        LocalDateTime startTime = LocalDateTime.now();
        String winner = "John";
        int winnerStep = 10;
        LocalDateTime finishTime = startTime.plusHours(1);
        int duration = (int) Duration.between(startTime, finishTime).getSeconds();

        Leaderboard leaderboard = leaderboardHelper.leaderboardBuilder(startTime, winner, winnerStep, finishTime);

        assertEquals(0L, leaderboard.getId());
        assertEquals(leaderboard.getDuration(), duration);
    }

    @Test
    public void testAddLeaderboardToList() throws IOException {
        var test1 = addOneSampleLeaderboard();
        Leaderboard test = Leaderboard.builder()
                .id(1L).winner("Sample").step(5).build();
        Leaderboard test2 = Leaderboard.builder()
                .id(1L).winner("Sample2").step(5).build();
        List<Leaderboard> temp = new ArrayList<>();
        temp.add(test1);
        leaderboardHelper.setLeaderboardList(temp);

        leaderboardHelper.addLeaderboardToList(test);
        List<Leaderboard> leaderboardList = leaderboardHelper.getLeaderboardList();

        assertEquals(1, leaderboardList.size());
        assertEquals(1L, leaderboardList.get(0).getId());
        assertEquals(5, leaderboardList.get(0).getStep());

        leaderboardHelper.addLeaderboardToList(test2);
        List<Leaderboard> leaderboardList2 = leaderboardHelper.getLeaderboardList();

        assertEquals(2, leaderboardList2.size());
        assertEquals(1L, leaderboardList2.get(0).getId());
        assertEquals(5, leaderboardList2.get(0).getStep());
    }

    @Test
    public void testOrderLeaderboardListByStepThenByDuration() throws IOException {
        var test1 = addOneSampleLeaderboard();
        Leaderboard test = Leaderboard.builder()
                .id(1L).winner("Sample2").step(5).build();
        Leaderboard test2 = Leaderboard.builder()
                .id(2L).winner("Sample3").step(1).build();
        leaderboardHelper.setLeaderboardList(List.of(test, test1, test2));

        List<Leaderboard> orderedList = leaderboardHelper.orderLeaderboardListByStepThenByDuration();

        assertEquals(3, orderedList.size());
        assertEquals(test2, orderedList.get(0));
        assertEquals(test, orderedList.get(1));
    }

    @Test
    public void testClearJson() throws IOException {
        addOneSampleLeaderboard();

        leaderboardHelper.clearJson(objectMapper, leaderboardFileName);
        leaderboardHelper.initIdAndList(objectMapper, leaderboardFileName);

        assertEquals(0, leaderboardHelper.getLeaderboardList().size());
    }
}
