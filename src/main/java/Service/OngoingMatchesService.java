package Service;

import Dto.NamesPlayerDto;
import Model.MatchScoreModel;
import Entity.Match;
import Entity.Player;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Log4j2
@ToString
public class OngoingMatchesService {

    private final Map<UUID, MatchScoreModel> currentMatches = new HashMap<>();
    private final PlayersManipulationService playersManipulation = new PlayersManipulationService();

    public UUID initializedMatch(NamesPlayerDto namesPlayerDto) {
        Player firstPlayer = playersManipulation.getPlayerByName(namesPlayerDto.getNamePlayerOne());
        Player secondPlayer = playersManipulation.getPlayerByName(namesPlayerDto.getNamePlayerTwo());

        Match newMatch = Match.builder()
                .playerFirst(firstPlayer)
                .playerSecond(secondPlayer)
                .build();

        MatchScoreModel matchScoreModel = new MatchScoreModel(newMatch);

        UUID uuid = UUID.randomUUID();
        currentMatches.put(uuid, matchScoreModel);
        return uuid;
    }

    public MatchScoreModel getCurrentMatch(UUID uuid) {
        return currentMatches.get(uuid);
    }

    public void deletingCompletedMatch(UUID uuid) {
        if (currentMatches.get(uuid).isMatchOver()) {
            currentMatches.remove(uuid);
        } else {
            throw new IllegalStateException("Попытка удалить из памяти не законченный матч!");
        }
    }
}
