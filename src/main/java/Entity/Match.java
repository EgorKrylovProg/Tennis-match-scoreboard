package Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Matches")
public class Match {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_1")
    private Player playerFirst;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_2")
    private Player playerSecond;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "winner")
    private Player winner;


    public void setWinnerById(Integer playerId) {
        if (playerFirst.getId().intValue() == playerId) {
            winner = playerFirst;
        } else if (playerSecond.getId().intValue() == playerId) {
            winner = playerSecond;
        } else {
            throw new IllegalArgumentException("Игрок с переданным идентификатором не был найден в матче!");
        }
    }
}
