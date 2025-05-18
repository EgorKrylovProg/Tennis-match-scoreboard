package Controller.Servlets;

import Dto.NamesPlayerDto;
import Exceptions.InvalidUserInputException;
import Service.OngoingMatchesService;
import Service.PlayersManipulationService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;


import java.io.IOException;
import java.util.*;

@WebServlet("/new-match")
@Log4j2
public class NewMatchServlet extends HttpServlet {

    private PlayersManipulationService playersManipulationService;
    private OngoingMatchesService ongoingMatchesService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        playersManipulationService = new PlayersManipulationService();
        ongoingMatchesService = (OngoingMatchesService) getServletContext().getAttribute("matchStorage");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            NamesPlayerDto namesPlayerDto = mapRequest(req);
            playersManipulationService.saveIfNotExist(namesPlayerDto);

            String strUuid = ongoingMatchesService.initializedMatch(namesPlayerDto).toString();

            log.debug("Создан матч с uuid кодом: {}", strUuid);

            resp.sendRedirect("match-score?uuid=" + strUuid);
        } catch (InvalidUserInputException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    private NamesPlayerDto mapRequest(HttpServletRequest req) {
        Map<String, String[]> parameters = req.getParameterMap();

        Iterator<Map.Entry<String, String[]>> entryIterator = parameters.entrySet().iterator();
        String nameFirstPlayer = entryIterator.next().getValue()[0];
        String nameSecondPlayer = entryIterator.next().getValue()[0];

        log.debug("Имена игроков, полученные в запросе на создание нового матча: {}, {}",
                nameFirstPlayer, nameSecondPlayer);

        return new NamesPlayerDto(nameFirstPlayer, nameSecondPlayer);
    }
}
