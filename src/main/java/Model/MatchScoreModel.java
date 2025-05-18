package Model;

import Enum.TypePoints;
import Entity.Match;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Builder
@Data
public class MatchScoreModel {

    private Match match;
    private Map<TypePoints, Integer> scoreFirstPlayer;
    private Map<TypePoints, Integer> scoreSecondPlayer;

    public MatchScoreModel(Match match) {
        this.match = match;
        scoreFirstPlayer = new EnumMap<>(
                Map.of(
                        TypePoints.SET, 0,
                        TypePoints.GAME, 0,
                        TypePoints.POINTS, 0
                )
        );
        scoreSecondPlayer = new EnumMap<>(
                Map.of(
                        TypePoints.SET, 0,
                        TypePoints.GAME, 0,
                        TypePoints.POINTS, 0
                )
        );
    }

    public Optional<Map<TypePoints, Integer>> getScorePlayerById(Integer playerId) {
        if(match.getPlayerFirst().getId().intValue() == playerId) {
            return Optional.of(scoreFirstPlayer);
        } else if(match.getPlayerSecond().getId().intValue() == playerId) {
            return Optional.of(scoreSecondPlayer);
        }
        return Optional.empty();
    }

    public Optional<Map<TypePoints, Integer>> getOpponentScoreByPlayerId(Integer playerId) {
        if (match.getPlayerFirst().getId().intValue() == playerId) {
            return Optional.of(scoreSecondPlayer);
        } else if (match.getPlayerSecond().getId().intValue() == playerId) {
            return Optional.of(scoreFirstPlayer);
        }
        return Optional.empty();
    }

    public boolean isMatchOver() {
        return match.getWinner() != null;
    }


}
