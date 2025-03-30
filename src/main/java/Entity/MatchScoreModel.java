package Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Builder
@Data
public class MatchScoreModel {

    private Match match;
    private Map<TypePoints, Integer> resultsFirstPlayer;
    private Map<TypePoints, Integer> resultsSecondPlayer;

    public MatchScoreModel() {
        resultsFirstPlayer = new EnumMap<>(
                Map.of(
                        TypePoints.SET, 0,
                        TypePoints.GAME, 0,
                        TypePoints.POINTS, 0
                )
        );
        resultsSecondPlayer = new EnumMap<>(
                Map.of(
                        TypePoints.SET, 0,
                        TypePoints.GAME, 0,
                        TypePoints.POINTS, 0
                )
        );
    }

}
