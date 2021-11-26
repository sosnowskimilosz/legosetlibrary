package milo.legosetlibrary.LegoSetLibrary;

import lombok.AllArgsConstructor;
import milo.legosetlibrary.LegoSetLibrary.set.application.port.LegoSetUseCase;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoCategory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static milo.legosetlibrary.LegoSetLibrary.set.application.port.LegoSetUseCase.*;

@Component
@AllArgsConstructor
public class ApplicationStartup implements CommandLineRunner {

    private final LegoSetUseCase service;

    @Override
    public void run(String... args) {
        initData();
        printAll();
        printWithId(1L);
        updateLegoSetWithCatalogNumber("60215");
        changeStatusOfLegoSetToPurchased(2L);
        printAll();
    }

    private void initData() {
        service.addLegoSet(new CreateLegoSetCommand("60215", "Fire brigade", LegoCategory.DISNEY, 230, BigDecimal.valueOf(239)));
        service.addLegoSet(new CreateLegoSetCommand("60264", "Ocean submarine", LegoCategory.CITY, 123, BigDecimal.valueOf(120)));
        service.addLegoSet(new CreateLegoSetCommand("75257", "Star Wars - Millenium", LegoCategory.STAR_WARS, 1351, BigDecimal.valueOf(700)));
    }

    private void printAll() {
        System.out.println("\nPrint all:");
        service.findAll()
                .forEach(System.out::println);
    }

    private void printWithId(Long id) {
        if (service.findById(id).isPresent()) {
            System.out.println("\nPrint with ID=" + id + ":\n" + service.findById(id).get());
        } else {
            System.out.println("There is no LegoSet with ID: " + id);
        }
    }

    private void updateLegoSetWithCatalogNumber(String catalogNumber) {
        service.findByCatalogNumber(catalogNumber).ifPresent(
                legoSet -> {
                    UpdateLegoSetCommand command = UpdateLegoSetCommand
                            .builder()
                            .id(legoSet.getId())
                            .category(LegoCategory.CITY)
                            .numberOfPieces(250)
                            .build();
                    UpdateLegoSetResponse response = service.updateLegoSet(command);
                    System.out.println("\nUpdating LegoSet with ID: " + legoSet.getId() + "\nResult: " + response.isSuccess());
                });
    }

    private void changeStatusOfLegoSetToPurchased(Long id) {
        service.changeStatusToPurchased(id);
    }
}
