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

}
