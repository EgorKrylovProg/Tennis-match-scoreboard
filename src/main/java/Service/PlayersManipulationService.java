package Service;

import Dto.NamesPlayerDto;
import Entity.Player;
import Repository.Impl.PlayerDao;
import Repository.Interface.NamedEntityDao;

public class PlayersManipulationService {

    private final NamedEntityDao<Integer, Player> playerDao = new PlayerDao();

    public void saveIfNotExist(NamesPlayerDto namesPlayerDto) {
        String nameFirstPlayer = namesPlayerDto.getNamePlayerOne();
        String nameSecondPlayer = namesPlayerDto.getNamePlayerTwo();

        if(playerDao.getByName(nameFirstPlayer).isEmpty()) {

            Player newPlayer = Player.builder()
                    .name(nameFirstPlayer)
                    .build();

            playerDao.save(newPlayer);
        }
        if (playerDao.getByName(nameSecondPlayer).isEmpty()) {

            Player newPlayer = Player.builder()
                    .name(nameSecondPlayer)
                    .build();

            playerDao.save(newPlayer);
        }
    }
}
