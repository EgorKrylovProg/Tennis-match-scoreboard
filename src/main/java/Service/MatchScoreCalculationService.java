package Service;

import Entity.MatchScoreModel;
import Entity.TypePoints;

import java.util.Map;
import java.util.UUID;

public class MatchScoreCalculationService {

    private OngoingMatchesService ongoingMatchesService = new OngoingMatchesService();
    private Map<TypePoints, Integer> resultFirstPlayer;
    private Map<TypePoints, Integer> resultSecondPlayer;

    public void updatingScore(UUID matchUuid, Integer playerId) {
        MatchScoreModel matchScore = ongoingMatchesService.getCurrentMatch(matchUuid);
        resultFirstPlayer = matchScore.getResultsFirstPlayer();
        resultSecondPlayer = matchScore.getResultsSecondPlayer();

        if (resultFirstPlayer.get(TypePoints.GAME).equals(resultSecondPlayer.get(TypePoints.GAME))
                && resultFirstPlayer.get(TypePoints.GAME) == 6) {
            updatingPointsTiebreaker(matchScore, playerId);
            checkingTiebreakerVictory();
        }

    }

    private void updatingPointsTiebreaker(MatchScoreModel matchScore, Integer playerId) {
        if(matchScore.getMatch().getPlayerFirst().getId().intValue() == playerId) {
            resultFirstPlayer.put(TypePoints.POINTS, resultFirstPlayer.get(TypePoints.POINTS) + 1);
        } else {
            resultSecondPlayer.put(TypePoints.POINTS, resultSecondPlayer.get(TypePoints.POINTS) + 1);
        }
    }

    private void checkingTiebreakerVictory() {
        if(resultFirstPlayer.get(TypePoints.POINTS) >= 7
                && resultFirstPlayer.get(TypePoints.POINTS) - resultSecondPlayer.get(TypePoints.POINTS) == 2) {

            resultFirstPlayer.put(TypePoints.SET, resultFirstPlayer.get(TypePoints.SET) + 1);
            resultFirstPlayer.put(TypePoints.GAME, 0);
            resultFirstPlayer.put(TypePoints.POINTS, 0);
            resultSecondPlayer.put(TypePoints.GAME, 0);
            resultSecondPlayer.put(TypePoints.POINTS, 0);

        } else if (resultSecondPlayer.get(TypePoints.POINTS) >= 7
                && resultSecondPlayer.get(TypePoints.POINTS) - resultFirstPlayer.get(TypePoints.POINTS) == 2) {

            resultSecondPlayer.put(TypePoints.SET, resultSecondPlayer.get(TypePoints.SET) + 1);
            resultSecondPlayer.put(TypePoints.GAME, 0);
            resultSecondPlayer.put(TypePoints.POINTS, 0);
            resultFirstPlayer.put(TypePoints.GAME, 0);
            resultFirstPlayer.put(TypePoints.POINTS, 0);

        }
    }


}
