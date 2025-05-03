package Service;

import Entity.Match;
import Model.MatchScoreModel;
import Entity.Player;
import Enum.TypePoints;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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

    private static Stream<Arguments> getMatchScoreData() {
        return Stream.of(
                Arguments.of(List.of(0, 0, 40), List.of(0, 0, 40), List.of(0, 0, 50), List.of(0, 0, 40)),
                Arguments.of(List.of(0, 0, 40), List.of(0, 0, 0), List.of(0, 1, 0), List.of(0, 0, 0)),
                Arguments.of(List.of(0, 6, 40), List.of(0, 0, 0), List.of(1, 0, 0), List.of(0, 0, 0)),
                Arguments.of(List.of(0, 0, 40), List.of(0, 0, 40), List.of(0, 0, 50), List.of(0, 0, 40)),
                Arguments.of(List.of(0, 5, 40), List.of(0, 5, 0), List.of(0, 6, 0), List.of(0, 5, 0)),
                Arguments.of(List.of(0, 6, 0), List.of(0, 6, 0), List.of(0, 6, 1), List.of(0, 6, 0)),
                Arguments.of(List.of(0, 6, 7), List.of(0, 6, 7), List.of(0, 6, 8), List.of(0, 6, 7)),
                Arguments.of(List.of(0, 6, 6), List.of(0, 6, 0), List.of(1, 0, 0), List.of(0, 0, 0))
        );
    }

    @ParameterizedTest(name = "Checking the extreme cases of the score")
    @MethodSource("getMatchScoreData")
    public void tennisMatchScoreCalculationEdgeCases(List<Integer> scoreFirst, List<Integer> scoreSecond,
                                                     List<Integer> scoreReferenceFirst, List<Integer> scoreReferenceSecond) {
        matchScoreModel.setScoreFirstPlayer(createScore(scoreFirst));
        matchScoreModel.setScoreSecondPlayer(createScore(scoreSecond));

        matchScoreCalculationService.updatingScore(matchScoreModel, 1);

        Assertions.assertAll(
                () -> Assertions.assertEquals(createScore(scoreReferenceFirst), matchScoreModel.getScoreFirstPlayer()),
                () -> Assertions.assertEquals(createScore(scoreReferenceSecond), matchScoreModel.getScoreSecondPlayer())
        );
    }

    @Test
    @DisplayName("Checking player win the match")
    public void playerShouldWinMatchWhenScoreSetIsTwo() {
        matchScoreModel.setScoreFirstPlayer(createScore(List.of(1, 5, 40)));
        matchScoreModel.setScoreSecondPlayer(createScore(List.of(0, 0, 0)));

        matchScoreCalculationService.updatingScore(matchScoreModel, 1);

        Assertions.assertEquals(matchScoreModel.getMatch().getWinner(), matchScoreModel.getMatch().getPlayerFirst());
    }


}
