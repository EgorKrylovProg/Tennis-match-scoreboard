package Service;

import Entity.Match;
import Repository.Impl.MatchDao;
import Repository.Interface.Dao;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class FinishedMatchesPersistenceService {

    private final Dao<Integer, Match> matchDao = new MatchDao();

    public void savingMatch(Match match) {
        matchDao.save(match);
    }

}
