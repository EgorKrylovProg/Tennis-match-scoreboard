package Utilit;

import Model.MatchScoreModel;
import jakarta.servlet.http.HttpServletRequest;
import Enum.TypePoints;

public class Utilities {

    public static void setMatchAttributes(MatchScoreModel matchScoreModel, HttpServletRequest req) {
        req.setAttribute("uuid", req.getParameter("uuid"));

        req.setAttribute("nameFirst", matchScoreModel.getMatch().getPlayerFirst().getName());
        req.setAttribute("nameSecond", matchScoreModel.getMatch().getPlayerSecond().getName());

        req.setAttribute("idFirst", matchScoreModel.getMatch().getPlayerFirst().getId());
        req.setAttribute("idSecond", matchScoreModel.getMatch().getPlayerSecond().getId());

        req.setAttribute("setPointsFirst", matchScoreModel.getScoreFirstPlayer().get(TypePoints.SET));
        req.setAttribute("gamePointsFirst", matchScoreModel.getScoreFirstPlayer().get(TypePoints.GAME));
        req.setAttribute("pointsFirst", matchScoreModel.getScoreFirstPlayer().get(TypePoints.POINTS));

        req.setAttribute("setPointsSecond", matchScoreModel.getScoreSecondPlayer().get(TypePoints.SET));
        req.setAttribute("gamePointsSecond", matchScoreModel.getScoreSecondPlayer().get(TypePoints.GAME));
        req.setAttribute("pointsSecond", matchScoreModel.getScoreSecondPlayer().get(TypePoints.POINTS));
    }

}
