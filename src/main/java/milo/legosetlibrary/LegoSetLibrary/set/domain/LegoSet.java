package milo.legosetlibrary.LegoSetLibrary.set.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class LegoSet {
    Long id;
    String catalogNumber;
    LegoSetStatus status;
    LocalDate purchasingDate;
    LegoCategory category;
    Integer numberOfPieces;
    BigDecimal price;

    public LegoSet(String catalogNumber, LegoCategory category, Integer numberOfPieces, BigDecimal price) {
        this.catalogNumber = catalogNumber;
        this.status = LegoSetStatus.ON_DREAM_LIST;
        this.category = category;
        this.numberOfPieces = numberOfPieces;
        this.price = price;
    }
}
