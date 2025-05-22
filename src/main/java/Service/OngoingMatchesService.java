package Service;

import Dto.NamesPlayerDto;
import Model.MatchScoreModel;
import Entity.Match;
import Entity.Player;
import Exceptions.NoDataException;
import Repository.Impl.MatchDao;
import Repository.Impl.PlayerDao;
import Repository.Interface.Dao;
import Repository.Interface.NamedEntityDao;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Log4j2
@ToString
public class OngoingMatchesService {

    private final Map<UUID, MatchScoreModel> currentMatches = new HashMap<>();
    private final NamedEntityDao<Integer, Player> playerDao = new PlayerDao();

    public UUID initializedMatch(NamesPlayerDto namesPlayerDto) {
        Player firstPlayer = playerDao.getByName(namesPlayerDto.getNamePlayerOne())
                .orElseThrow(() -> new NoDataException(String.format("Игрок '%s' не был найден в базе данных!",
                        namesPlayerDto.getNamePlayerOne()))
                );

        Player secondPlayer = playerDao.getByName(namesPlayerDto.getNamePlayerTwo())
                .orElseThrow(() -> new NoDataException(String.format("Игрок '%s' не был найден в базе данных!",
                        namesPlayerDto.getNamePlayerTwo()))
                );

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
