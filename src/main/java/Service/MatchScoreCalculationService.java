package Service;

import Entity.MatchScoreModel;
import Entity.TypePoints;
import Exceptions.MatchAlreadyFinishedException;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

@Log4j2
public class MatchScoreCalculationService {

    private Map<TypePoints, Integer> scorePlayerWonPoint;
    private Map<TypePoints, Integer> opponentScore;

    public void updatingScore(MatchScoreModel matchScore, Integer playerId) {
        log.debug("Передана модель счета: {}", matchScore);
        log.debug("Переданный id игрока выйгравшего очко: {}", playerId);

        scorePlayerWonPoint = matchScore.getScorePlayerById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Игрок с переданным идентификатором не был найден в матче!"));
        opponentScore = matchScore.getOpponentScoreByPlayerId(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Игрок с переданным идентификатором не был найден в матче!"));

        log.debug("Счет выйгравшего очко игрока до изменения: {}", scorePlayerWonPoint);
        log.debug("Счет соперника до изменения: {}", opponentScore);

        if (isMatchVictory()) {
            throw new MatchAlreadyFinishedException("Матч уже завершен!");
        } else if (isTiebreaker()) {
            winningTiebreakerPoint();
        } else {
            winningPoint();
        }

        if (isMatchVictory()) {
            matchScore.getMatch().setWinnerById(playerId);
        }

        log.debug("Счет выйгравшего очко игрока после изменения: {}", scorePlayerWonPoint);
        log.debug("Счет соперника после изменения: {}", opponentScore);
    }

    private boolean isTiebreaker() {
        return scorePlayerWonPoint.get(TypePoints.GAME).equals(opponentScore.get(TypePoints.GAME))
                && scorePlayerWonPoint.get(TypePoints.GAME) == 6;
    }

    private void winningTiebreakerPoint() {
        scorePlayerWonPoint.put(TypePoints.POINTS, scorePlayerWonPoint.get(TypePoints.POINTS) + 1);
        checkTiebreakVictory();
    }

    private void checkTiebreakVictory() {

        if (scorePlayerWonPoint.get(TypePoints.POINTS) >= 7
                && scorePlayerWonPoint.get(TypePoints.POINTS) - opponentScore.get(TypePoints.POINTS) == 2) {

            scorePlayerWonPoint.put(TypePoints.SET, scorePlayerWonPoint.get(TypePoints.SET) + 1);
            scorePlayerWonPoint.put(TypePoints.GAME, 0);
            scorePlayerWonPoint.put(TypePoints.POINTS, 0);
            opponentScore.put(TypePoints.GAME, 0);
            opponentScore.put(TypePoints.POINTS, 0);
        }
    }

    private void winningPoint() {
        Integer currentPoints = scorePlayerWonPoint.get(TypePoints.POINTS);
        Integer updatedPoints = currentPoints < 30 ? scorePlayerWonPoint.get(TypePoints.POINTS) + 15
                : scorePlayerWonPoint.get(TypePoints.POINTS) + 10;
        scorePlayerWonPoint.put(TypePoints.POINTS, updatedPoints);

        checkGameVictory();
    }

    private void checkGameVictory() {
        Integer pointsFirstPlayer = scorePlayerWonPoint.get(TypePoints.POINTS);
        Integer pointsSecondPlayer = opponentScore.get(TypePoints.POINTS);

        if (pointsFirstPlayer > 40 && pointsFirstPlayer - pointsSecondPlayer >= 20) {
            scorePlayerWonPoint.put(TypePoints.POINTS, 0);
            scorePlayerWonPoint.put(TypePoints.GAME, scorePlayerWonPoint.get(TypePoints.GAME) + 1);
            opponentScore.put(TypePoints.POINTS, 0);

            checkSetVictory();
        }
    }

    private void checkSetVictory() {
        Integer gamesFirstPlayer = scorePlayerWonPoint.get(TypePoints.GAME);
        Integer gamesSecondPlayer = opponentScore.get(TypePoints.GAME);

        if (gamesFirstPlayer >= 6 && gamesFirstPlayer - gamesSecondPlayer == 2) {
            scorePlayerWonPoint.put(TypePoints.SET, scorePlayerWonPoint.get(TypePoints.SET) + 1);
            scorePlayerWonPoint.put(TypePoints.GAME, 0);
            opponentScore.put(TypePoints.GAME, 0);
        }
    }

    private boolean isMatchVictory() {
         return scorePlayerWonPoint.get(TypePoints.SET) == 2;
    }



}
