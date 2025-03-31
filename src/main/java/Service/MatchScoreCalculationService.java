package Service;

import Entity.MatchScoreModel;
import Entity.TypePoints;

import java.util.Map;
import java.util.UUID;

public class MatchScoreCalculationService {

    private final OngoingMatchesService ongoingMatchesService = new OngoingMatchesService();
    private MatchScoreModel matchScore;
    private Map<TypePoints, Integer> scoreFirstPlayer;
    private Map<TypePoints, Integer> scoreSecondPlayer;

    public void updatingScore(UUID matchUuid, Integer playerId) {
        matchScore = ongoingMatchesService.getCurrentMatch(matchUuid);
        scoreFirstPlayer = matchScore.getScoreFirstPlayer();
        scoreSecondPlayer = matchScore.getScoreSecondPlayer();

        if (scoreFirstPlayer.get(TypePoints.GAME).equals(scoreSecondPlayer.get(TypePoints.GAME))
                && scoreFirstPlayer.get(TypePoints.GAME) == 6) {
            winningTiebreakerPoint(playerId);
            checkingTiebreakerVictory();
        } else {
            winningPoint(playerId);
        }

    }

    private void winningTiebreakerPoint(Integer playerId) {
        if(matchScore.getMatch().getPlayerFirst().getId().intValue() == playerId) {
            scoreFirstPlayer.put(TypePoints.POINTS, scoreFirstPlayer.get(TypePoints.POINTS) + 1);
        } else {
            scoreSecondPlayer.put(TypePoints.POINTS, scoreSecondPlayer.get(TypePoints.POINTS) + 1);
        }
    }

    private void checkingTiebreakerVictory() {
        if(scoreFirstPlayer.get(TypePoints.POINTS) >= 7
                && scoreFirstPlayer.get(TypePoints.POINTS) - scoreSecondPlayer.get(TypePoints.POINTS) == 2) {

            scoreFirstPlayer.put(TypePoints.SET, scoreFirstPlayer.get(TypePoints.SET) + 1);
            scoreFirstPlayer.put(TypePoints.GAME, 0);
            scoreFirstPlayer.put(TypePoints.POINTS, 0);
            scoreSecondPlayer.put(TypePoints.GAME, 0);
            scoreSecondPlayer.put(TypePoints.POINTS, 0);

        } else if (scoreSecondPlayer.get(TypePoints.POINTS) >= 7
                && scoreSecondPlayer.get(TypePoints.POINTS) - scoreFirstPlayer.get(TypePoints.POINTS) == 2) {

            scoreSecondPlayer.put(TypePoints.SET, scoreSecondPlayer.get(TypePoints.SET) + 1);
            scoreSecondPlayer.put(TypePoints.GAME, 0);
            scoreSecondPlayer.put(TypePoints.POINTS, 0);
            scoreFirstPlayer.put(TypePoints.GAME, 0);
            scoreFirstPlayer.put(TypePoints.POINTS, 0);

        }
    }

    private void winningPoint(Integer playerId) {
        Integer currentPoints = scoreFirstPlayer.get(TypePoints.POINTS);
        Integer updatedPoints = currentPoints < 30 ? scoreFirstPlayer.get(TypePoints.POINTS) + 15
                : scoreFirstPlayer.get(TypePoints.POINTS) + 10;

        if(matchScore.getMatch().getPlayerFirst().getId().intValue() == playerId) {
            scoreFirstPlayer.put(TypePoints.POINTS, updatedPoints);
        } else {
            scoreSecondPlayer.put(TypePoints.POINTS, updatedPoints);
        }
    }


}
