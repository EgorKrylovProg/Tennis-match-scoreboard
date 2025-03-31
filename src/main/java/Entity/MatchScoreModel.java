package Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.EnumMap;
import java.util.Map;

@AllArgsConstructor
@Builder
@Data
public class MatchScoreModel {

    private Match match;
    private Map<TypePoints, Integer> scoreFirstPlayer;
    private Map<TypePoints, Integer> scoreSecondPlayer;

    public MatchScoreModel() {
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

    public boolean isTiebreaker() {
        return scoreFirstPlayer.get(TypePoints.GAME).equals(scoreSecondPlayer.get(TypePoints.GAME))
                && scoreFirstPlayer.get(TypePoints.GAME) == 6;
    }


}
