package milo.legosetlibrary.LegoSetLibrary.set.application.port;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoCategory;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSet;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSetStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public interface LegoSetUseCase {

    List<LegoSet> findAll();

    Optional<LegoSet> findById(Long id);

    Optional<LegoSet> findByCatalogNumbe(String number);

    List<LegoSet> findByStatus(LegoSetStatus status);

    List<LegoSet> findByCategory(LegoCategory category);

    void removeById(Long id);

    LegoSet addLegoSet(CreateLegoSetCommand command);

    UpdateLegoSetResponse updateLegoSet(UpdateLegoSetCommand command);

    UpdateLegoSetResponse changeStatusToPurchased(Long id);

    @Value
    class CreateLegoSetCommand {
        String catalogNumber;
        LegoCategory category;
        Integer numberOfPieces;
        BigDecimal price;

        public LegoSet toLegoSet() {
            return new LegoSet(catalogNumber, category, numberOfPieces, price);
        }
    }

    @Value
    class UpdateLegoSetResponse {
        public static UpdateLegoSetResponse SUCCESS = new UpdateLegoSetResponse(true, emptyList());

        boolean success;
        List<String> errors;
    }

    @Value
    @Builder
    @AllArgsConstructor
    class UpdateLegoSetCommand {
        Long id;
        String catalogNumber;
        LegoSetStatus status;
        LocalDate purchasingDate;
        LegoCategory category;
        Integer numberOfPieces;
        BigDecimal price;

        public LegoSet updateFields(LegoSet legoSet) {
            if (catalogNumber != null) {
                legoSet.setCatalogNumber(catalogNumber);
            }
            if (status != null) {
                legoSet.setStatus(status);
            }
            if (purchasingDate != null) {
                legoSet.setPurchasingDate(purchasingDate);
            }
            if (category != null) {
                legoSet.setCategory(category);
            }
            if (numberOfPieces != null) {
                legoSet.setNumberOfPieces(numberOfPieces);
            }
            if (price != null) {
                legoSet.setPrice(price);
            }
            return legoSet;
        }
    }

}