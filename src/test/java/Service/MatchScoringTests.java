package Service;

import Entity.Match;
import Entity.MatchScoreModel;
import Entity.Player;
import Entity.TypePoints;
import org.junit.jupiter.api.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class MatchScoringTests {

    private MatchScoreModel matchScoreModel;
    private final MatchScoreCalculationService matchScoreCalculationService = new MatchScoreCalculationService();

    private Map<TypePoints, Integer> createScore(List<Integer> score) {
        return new EnumMap<>(
                Map.of(
                        TypePoints.SET, score.get(0),
                        TypePoints.GAME, score.get(1),
                        TypePoints.POINTS, score.get(2)
                )
        );
    }

    @BeforeEach
    public void initializationMatch() {
        Player playerFirst = new Player(1, null);
        Player playerSecond = new Player(2, null);

        Match match = new Match(null, playerFirst, playerSecond, null);
        matchScoreModel = new MatchScoreModel(match, null, null);
    }

    @Test
    @DisplayName("Score 40-40")
    public void gameShouldContinueWhenScoreIsTied() {
        matchScoreModel.setScoreFirstPlayer(createScore(List.of(0, 0, 40)));
        matchScoreModel.setScoreSecondPlayer(createScore(List.of(0, 0, 40)));

        matchScoreCalculationService.updatingScore(matchScoreModel, 1);

        Assertions.assertAll(
                () -> Assertions.assertEquals(createScore(List.of(0, 0, 50)), matchScoreModel.getScoreFirstPlayer()),
                () -> Assertions.assertEquals(createScore(List.of(0, 0, 40)), matchScoreModel.getScoreSecondPlayer())
        );
    }

    @Test
    @DisplayName("Score 40-0")
    public void gameShouldEndWhenScoreIsFortyZero() {
        matchScoreModel.setScoreFirstPlayer(createScore(List.of(0, 0, 40)));
        matchScoreModel.setScoreSecondPlayer(createScore(List.of(0, 0, 0)));

        matchScoreCalculationService.updatingScore(matchScoreModel, 1);

        Assertions.assertAll(
                () -> Assertions.assertEquals(createScore(List.of(0, 1, 0)), matchScoreModel.getScoreFirstPlayer()),
                () -> Assertions.assertEquals(createScore(List.of(0, 0, 0)), matchScoreModel.getScoreSecondPlayer())
        );
    }

    @Test
    @DisplayName("Checking the end of the set")
    public void setShouldEndWhenScoreGameIsSixZero() {
        matchScoreModel.setScoreFirstPlayer(createScore(List.of(0, 6, 40)));
        matchScoreModel.setScoreSecondPlayer(createScore(List.of(0, 0, 0)));

        matchScoreCalculationService.updatingScore(matchScoreModel, 1);

        Assertions.assertAll(
                () -> Assertions.assertEquals(createScore(List.of(1, 0, 0)), matchScoreModel.getScoreFirstPlayer()),
                () -> Assertions.assertEquals(createScore(List.of(0, 0, 0)), matchScoreModel.getScoreSecondPlayer())
        );
    }

    @Test
    @DisplayName("The set must not end")
    public void setShouldContinueWhenScoreGameFiveFive() {
        matchScoreModel.setScoreFirstPlayer(createScore(List.of(0, 5, 40)));
        matchScoreModel.setScoreSecondPlayer(createScore(List.of(0, 5, 0)));

        matchScoreCalculationService.updatingScore(matchScoreModel, 1);

        Assertions.assertAll(
                () -> Assertions.assertEquals(createScore(List.of(0, 6, 0)), matchScoreModel.getScoreFirstPlayer()),
                () -> Assertions.assertEquals(createScore(List.of(0, 5, 0)), matchScoreModel.getScoreSecondPlayer())
        );
    }

    @Test
    @DisplayName("Check start tiebreak")
    public void tiebreakShouldStartWhenScoreGamesIsTied() {
        matchScoreModel.setScoreFirstPlayer(createScore(List.of(0, 6, 0)));
        matchScoreModel.setScoreSecondPlayer(createScore(List.of(0, 6, 0)));

        matchScoreCalculationService.updatingScore(matchScoreModel, 1);

        Assertions.assertAll(
                () -> Assertions.assertEquals(createScore(List.of(0, 6, 1)), matchScoreModel.getScoreFirstPlayer()),
                () -> Assertions.assertEquals(createScore(List.of(0, 6, 0)), matchScoreModel.getScoreSecondPlayer())
        );
    }

    @Test
    @DisplayName("Tiebreak score 7-7")
    public void tiebreakShouldNotEndWhenScorePointsIsTied() {
        matchScoreModel.setScoreFirstPlayer(createScore(List.of(0, 6, 7)));
        matchScoreModel.setScoreSecondPlayer(createScore(List.of(0, 6, 7)));

        matchScoreCalculationService.updatingScore(matchScoreModel, 1);

        Assertions.assertAll(
                () -> Assertions.assertEquals(createScore(List.of(0, 6, 8)), matchScoreModel.getScoreFirstPlayer()),
                () -> Assertions.assertEquals(createScore(List.of(0, 6, 7)), matchScoreModel.getScoreSecondPlayer())
        );
    }

    @Test
    @DisplayName("Player win the tiebreak")
    public void playerShouldWinTiebreak() {
        matchScoreModel.setScoreFirstPlayer(createScore(List.of(0, 6, 6)));
        matchScoreModel.setScoreSecondPlayer(createScore(List.of(0, 6, 0)));

        matchScoreCalculationService.updatingScore(matchScoreModel, 1);

        Assertions.assertAll(
                () -> Assertions.assertEquals(createScore(List.of(1, 0, 0)), matchScoreModel.getScoreFirstPlayer()),
                () -> Assertions.assertEquals(createScore(List.of(0, 0, 0)), matchScoreModel.getScoreSecondPlayer())
        );
    }

    @Test
    @DisplayName("Checking player win the match")
    public void playerShouldWinMatchWhenScoreSetIsTwo() {
        matchScoreModel.setScoreFirstPlayer(createScore(List.of(1, 5, 40)));
        matchScoreModel.setScoreSecondPlayer(createScore(List.of(0, 0, 0)));

        matchScoreCalculationService.updatingScore(matchScoreModel, 1);

        Assertions.assertNotNull(matchScoreModel.getMatch().getWinner());
    }


}
