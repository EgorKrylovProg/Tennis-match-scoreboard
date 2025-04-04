package Service;

import Entity.MatchScoreModel;
import Entity.TypePoints;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

@Log4j2
public class MatchScoreCalculationService {

    private Map<TypePoints, Integer> playerScoredPoint;
    private Map<TypePoints, Integer> opponentScore;

    public void updatingScore(MatchScoreModel matchScore, Integer playerId) {
        log.debug("Передана модель счета: {}", matchScore);
        log.debug("Переданный id игрока выйгравшего очко: {}", playerId);

        playerScoredPoint = matchScore.getScorePlayerById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Игрок с переданным идентификатором не был найден в матче!"));
        opponentScore = matchScore.getOpponentScoreByPlayerId(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Игрок с переданным идентификатором не был найден в матче!"));

        log.debug("Счет выйгравшего очко игрока: {}", playerScoredPoint);
        log.debug("Счет соперника: {}", opponentScore);

        if (isTiebreaker()) {
            winningTiebreakerPoint();
        } else {
            winningPoint();
        }
    }

    private boolean isTiebreaker() {
        return playerScoredPoint.get(TypePoints.GAME).equals(opponentScore.get(TypePoints.GAME))
                && playerScoredPoint.get(TypePoints.GAME) == 6;
    }

    private void winningTiebreakerPoint() {
        playerScoredPoint.put(TypePoints.POINTS, playerScoredPoint.get(TypePoints.POINTS) + 1);
        checkTiebreakVictory();
    }

    private void checkTiebreakVictory() {

        if (playerScoredPoint.get(TypePoints.POINTS) >= 7
                && playerScoredPoint.get(TypePoints.POINTS) - opponentScore.get(TypePoints.POINTS) == 2) {

            playerScoredPoint.put(TypePoints.SET, playerScoredPoint.get(TypePoints.SET) + 1);
            playerScoredPoint.put(TypePoints.GAME, 0);
            playerScoredPoint.put(TypePoints.POINTS, 0);
            opponentScore.put(TypePoints.GAME, 0);
            opponentScore.put(TypePoints.POINTS, 0);
        }
    }

    private void winningPoint() {
        Integer currentPoints = playerScoredPoint.get(TypePoints.POINTS);
        Integer updatedPoints = currentPoints < 30 ? playerScoredPoint.get(TypePoints.POINTS) + 15
                : playerScoredPoint.get(TypePoints.POINTS) + 10;
        playerScoredPoint.put(TypePoints.POINTS, updatedPoints);

        checkGameVictory();
    }

    private void checkGameVictory() {
        Integer pointsFirstPlayer = playerScoredPoint.get(TypePoints.POINTS);
        Integer pointsSecondPlayer = opponentScore.get(TypePoints.POINTS);

        if (pointsFirstPlayer > 40 && pointsFirstPlayer - pointsSecondPlayer >= 20) {
            playerScoredPoint.put(TypePoints.POINTS, 0);
            playerScoredPoint.put(TypePoints.GAME, playerScoredPoint.get(TypePoints.GAME) + 1);
            opponentScore.put(TypePoints.POINTS, 0);

            checkSetVictory();
        }
    }

    private void checkSetVictory() {
        Integer gamesFirstPlayer = playerScoredPoint.get(TypePoints.GAME);
        Integer gamesSecondPlayer = opponentScore.get(TypePoints.GAME);

        if (gamesFirstPlayer >= 6 && gamesFirstPlayer - gamesSecondPlayer == 2) {
            playerScoredPoint.put(TypePoints.SET, playerScoredPoint.get(TypePoints.SET) + 1);
            playerScoredPoint.put(TypePoints.GAME, 0);
            opponentScore.put(TypePoints.GAME, 0);
        }
    }


}
