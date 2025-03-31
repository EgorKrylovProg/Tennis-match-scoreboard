package Service;

import Entity.MatchScoreModel;
import Entity.Player;
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

        if (matchScore.isTiebreaker()) {
            winningTiebreakerPoint(playerId);
            checkTiebreakVictory();
        } else {
            winningPoint(playerId);
            checkGameVictory();
            checkSetVictory();
        }
        if (isMatchOver()) {

            matchScore.getMatch().getWinner().setId();
        }
    }

    private void winningTiebreakerPoint(Integer playerId) {
        if(matchScore.getMatch().getPlayerFirst().getId().intValue() == playerId) {
            scoreFirstPlayer.put(TypePoints.POINTS, scoreFirstPlayer.get(TypePoints.POINTS) + 1);
        } else {
            scoreSecondPlayer.put(TypePoints.POINTS, scoreSecondPlayer.get(TypePoints.POINTS) + 1);
        }
    }

    private void checkTiebreakVictory() {
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
        if(matchScore.getMatch().getPlayerFirst().getId().intValue() == playerId) {
            Integer currentPoints = scoreFirstPlayer.get(TypePoints.POINTS);
            Integer updatedPoints = currentPoints < 30 ? scoreFirstPlayer.get(TypePoints.POINTS) + 15
                    : scoreFirstPlayer.get(TypePoints.POINTS) + 10;
            scoreFirstPlayer.put(TypePoints.POINTS, updatedPoints);
        } else {
            Integer currentPoints = scoreSecondPlayer.get(TypePoints.POINTS);
            Integer updatedPoints = currentPoints < 30 ? scoreFirstPlayer.get(TypePoints.POINTS) + 15
                    : scoreFirstPlayer.get(TypePoints.POINTS) + 10;
            scoreSecondPlayer.put(TypePoints.POINTS, updatedPoints);
        }
    }

    private void checkGameVictory() {
        Integer pointsFirstPlayer = scoreFirstPlayer.get(TypePoints.POINTS);
        Integer pointsSecondPlayer = scoreSecondPlayer.get(TypePoints.POINTS);

        if (pointsFirstPlayer > 40 && pointsFirstPlayer - pointsSecondPlayer >= 20) {
            scoreFirstPlayer.put(TypePoints.POINTS, 0);
            scoreFirstPlayer.put(TypePoints.GAME, scoreFirstPlayer.get(TypePoints.GAME) + 1);
            scoreSecondPlayer.put(TypePoints.POINTS, 0);
        } else if (pointsSecondPlayer > 40 && pointsSecondPlayer - pointsFirstPlayer >= 20) {
            scoreSecondPlayer.put(TypePoints.POINTS, 0);
            scoreSecondPlayer.put(TypePoints.GAME, scoreFirstPlayer.get(TypePoints.GAME) + 1);
            scoreFirstPlayer.put(TypePoints.POINTS, 0);
        }
    }

    private void checkSetVictory() {
        Integer gamesFirstPlayer = scoreFirstPlayer.get(TypePoints.GAME);
        Integer gamesSecondPlayer = scoreSecondPlayer.get(TypePoints.GAME);

        if (gamesFirstPlayer >= 6 && gamesFirstPlayer - gamesSecondPlayer == 2) {
            scoreFirstPlayer.put(TypePoints.SET, scoreFirstPlayer.get(TypePoints.SET) + 1);
            scoreFirstPlayer.put(TypePoints.GAME, 0);
            scoreSecondPlayer.put(TypePoints.GAME, 0);
        } else if (gamesSecondPlayer >= 6 && gamesSecondPlayer - gamesFirstPlayer == 2) {
            scoreSecondPlayer.put(TypePoints.SET, scoreSecondPlayer.get(TypePoints.SET) + 1);
            scoreSecondPlayer.put(TypePoints.GAME, 0);
            scoreFirstPlayer.put(TypePoints.GAME, 0);
        }
    }

    public boolean isMatchOver() {
        return scoreFirstPlayer.get(TypePoints.SET) == 2 || scoreSecondPlayer.get(TypePoints.SET) == 2;
    }


}
