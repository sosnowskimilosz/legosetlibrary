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
    String category;
    Integer numberOfPieces;
    Integer numberOfMinifigures;
    BigDecimal price;

    public LegoSet(String catalogNumber, LegoSetStatus status, LocalDate purchasingDate, String category, Integer numberOfPieces, Integer numberOfMinifigures, BigDecimal price) {
        this.catalogNumber = catalogNumber;
        this.status = status;
        this.purchasingDate = purchasingDate;
        this.category = category;
        this.numberOfPieces = numberOfPieces;
        this.numberOfMinifigures = numberOfMinifigures;
        this.price = price;
    }
}
