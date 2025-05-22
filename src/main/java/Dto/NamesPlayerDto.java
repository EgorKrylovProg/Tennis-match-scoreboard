package Dto;

import Exceptions.InvalidUserInputException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Data
@NoArgsConstructor
@Log4j2
public class NamesPlayerDto {

    private String namePlayerOne;
    private String namePlayerTwo;

    public NamesPlayerDto (String namePlayerOne, String namePlayerTwo) {
        setNamePlayerOne(namePlayerOne);
        setNamePlayerTwo(namePlayerTwo);
    }

    private void setNamePlayerOne (String name) {
        if(name == null || name.isBlank()) {
            log.error("Пользователь не ввел имя первого игрока!");
            throw new InvalidUserInputException("Необходимо ввести имя первого игрока!");
        }
        this.namePlayerOne = name;
    }

    private void setNamePlayerTwo (String name) {
        if(name == null || name.isBlank()) {
            log.error("Пользователь не ввел имя первого игрока!");
            throw new InvalidUserInputException("Необходимо ввести имя первого игрока!");
        }
        this.namePlayerTwo = name;
    }

}
