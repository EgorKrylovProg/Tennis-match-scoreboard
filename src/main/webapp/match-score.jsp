<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Tennis match - текущий матч</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
<header class="navbar">
    <nav class="navigation">
        <a class="aboutProject" href="https://github.com/EgorKrylovProg/Tennis-match-scoreboard.git" target="_blank">
            О проекте
        </a>
        <a class="mainPageLink" href="index.html">На главную</a>
        <a class="matchListPageLink" href="#">Список матчей</a>
    </nav>
</header>
<main class="content-match-score-page">
    <div class="match-score-tables">
        <div class="table-row">
            <div class="cell-name-and-image">
                <img src="images/tennis_racket.png" alt>
                <div class="cell-name">
                    <span>${nameFirst}</span>
                </div>
            </div>
            <div class="cells-set">
                <div class="cell-type-points">
                    <span>Set</span>
                </div>
                <div class="cell">
                    <span>${setPointsFirst}</span>
                </div>
            </div>
            <div class="cells-game">
                <div class="cell-type-points">
                    <span>Game</span>
                </div>
                <div class="cell">
                    <span>${gamePointsFirst}</span>
                </div>
            </div>
            <div class="cell-points">
                <span>
                    <%
                        int points = (Integer) request.getAttribute("pointsFirst");
                        if (points > 40) {
                            out.print("Ad");
                        } else {
                            out.print(points);
                        }
                    %>
                </span>
            </div>
            <form class="request-submission-form" action="match-score" method="post">
                <input type="hidden" name="uuid" value="${uuid}"/>

                <button name="playerId" type="submit" value=${idFirst}>Очко</button>
            </form>
        </div>
        <div class="table-row">
            <div class="cell-name">
                <span>${nameSecond}</span>
            </div>
            <div class="cell">
                <span>${setPointsSecond}</span>
            </div>
            <div class="cell">
                <span>${gamePointsSecond}</span>
            </div>
            <div class="cell-points">
                <span>
                    <%
                        points = (Integer) request.getAttribute("pointsSecond");
                        if (points > 40) {
                            out.print("Ad");
                        } else {
                            out.print(points);
                        }
                    %>
                </span>
            </div>
            <form class="request-submission-form" action="match-score" method="post">
                <input type="hidden" name="uuid" value="${uuid}"/>

                <button name="playerId" type="submit" value=${idSecond}>Очко</button>
            </form>
        </div>
    </div>
</main>
</body>
</html>