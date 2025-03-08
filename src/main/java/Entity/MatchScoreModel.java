package Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MatchScoreModel {

    private Match match;
    private byte firstPlayerScore = 0;
    private byte secondPlayerScore = 0;


}
