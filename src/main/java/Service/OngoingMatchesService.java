package Service;

import Dto.NamesPlayerDto;
import Entity.MatchScoreModel;
import Entity.Match;
import Entity.Player;
import Exceptions.NoDataException;
import Repository.Impl.PlayerDao;
import Repository.Interface.NamedEntityDao;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OngoingMatchesService {

    private final Map<UUID, MatchScoreModel> currentMatches = new HashMap<>();
    private final NamedEntityDao<Integer, Player> playerDao = new PlayerDao();

    public UUID initializedMatch(NamesPlayerDto namesPlayerDto) throws NoDataException {
        Player firstPlayer = playerDao.getByName(namesPlayerDto.getNamePlayerOne())
                .orElseThrow(() -> new NoDataException(String.format("The player '%s' was not found in the database!",
                        namesPlayerDto.getNamePlayerOne()))
                );

        Player secondPlayer = playerDao.getByName(namesPlayerDto.getNamePlayerTwo())
                .orElseThrow(() -> new NoDataException(String.format("The player '%s' was not found in the database!",
                        namesPlayerDto.getNamePlayerTwo()))
                );

        Match newMatch = Match.builder()
                .playerFirst(firstPlayer)
                .playerSecond(secondPlayer)
                .build();

        MatchScoreModel matchScoreModel = MatchScoreModel.builder()
                .match(newMatch)
                .build();

        UUID uuid = UUID.randomUUID();
        currentMatches.put(uuid, matchScoreModel);
        return uuid;
    }

    public MatchScoreModel getCurrentMatch(UUID uuid) {
        return currentMatches.get(uuid);
    }
}
