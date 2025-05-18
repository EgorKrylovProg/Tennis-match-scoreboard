package Controller.Servlets;

import Model.MatchScoreModel;
import Service.MatchScoreCalculationService;
import Service.OngoingMatchesService;
import Utilit.Utilities;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
@Log4j2
public class MatchScoreServlet extends HttpServlet {

    private OngoingMatchesService ongoingMatchesService;
    private MatchScoreCalculationService calculationScoreService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ongoingMatchesService = (OngoingMatchesService) getServletContext().getAttribute("matchStorage");
        calculationScoreService = new MatchScoreCalculationService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            UUID uuid = UUID.fromString(req.getParameter("uuid"));
            MatchScoreModel matchScoreModel = ongoingMatchesService.getCurrentMatch(uuid);

            Utilities.setMatchAttributes(matchScoreModel, req);

            req.getRequestDispatcher("/match-score.jsp").forward(req, resp);
        } catch (Exception ex) {
            log.error(ex);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            log.debug(req.getParameter("uuid"));
            log.debug(req.getParameter("playerId"));
            UUID uuid = UUID.fromString(req.getParameter("uuid"));
            Integer playerId = Integer.parseInt(req.getParameter("playerId"));

            MatchScoreModel matchScoreModel = ongoingMatchesService.getCurrentMatch(uuid);
            calculationScoreService.updatingScore(matchScoreModel, playerId);

            if(matchScoreModel.isMatchOver()) {
                resp.sendRedirect("index.html");
            }

            resp.sendRedirect("match-score?uuid=" + uuid);
        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }


    }
}
