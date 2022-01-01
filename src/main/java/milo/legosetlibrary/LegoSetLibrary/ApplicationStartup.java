package milo.legosetlibrary.LegoSetLibrary;

import lombok.AllArgsConstructor;
import milo.legosetlibrary.LegoSetLibrary.set.application.port.LegoSetUseCase;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoCategory;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSet;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSetStatus;
import milo.legosetlibrary.LegoSetLibrary.user.application.port.UserUseCase;
import milo.legosetlibrary.LegoSetLibrary.user.domain.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static milo.legosetlibrary.LegoSetLibrary.set.application.port.LegoSetUseCase.*;

@Component
@AllArgsConstructor
public class ApplicationStartup implements CommandLineRunner {

    private final LegoSetUseCase legoSetService;
    private final UserUseCase userService;

    @Override
    public void run(String... args) {
        initData();
        printAllLegoSet();
        updateLegoSetWithCatalogNumber("60215");
        changeStatusOfLegoSetToPurchased(2L);
        printAllLegoSet();
    }

    private void initData() {
        LegoSet legoSetFireBrigade = legoSetService.addLegoSet(new CreateLegoSetCommand("60215", "Fire brigade", LegoCategory.DISNEY, 230, BigDecimal.valueOf(239), Set.of()));
        LegoSet legoSetOceanSubmarine = legoSetService.addLegoSet(new CreateLegoSetCommand("60264", "Ocean submarine", LegoCategory.CITY, 123, BigDecimal.valueOf(120), Set.of()));
        LegoSet legoSetStarWars = legoSetService.addLegoSet(new CreateLegoSetCommand("75257", "Star Wars - Millenium", LegoCategory.STAR_WARS, 1351, BigDecimal.valueOf(700), Set.of()));

        User user1 = userService.addUser(new UserUseCase.CreateUserCommand("user123", Set.of(legoSetFireBrigade.getId(), legoSetOceanSubmarine.getId())));
        User user2 = userService.addUser(new UserUseCase.CreateUserCommand("user1321", Set.of(legoSetStarWars.getId())));
        User user3 = userService.addUser(new UserUseCase.CreateUserCommand("user987", Set.of(legoSetFireBrigade.getId(), legoSetStarWars.getId())));
        User user4 = userService.addUser(new UserUseCase.CreateUserCommand("user1222", Set.of()));
    }

    private void printAllLegoSet() {
        System.out.println("\nPrint all:");
        legoSetService.findAll()
                .forEach(System.out::println);
    }

    private void printWithId(Long id) {
        if (legoSetService.findById(id).isPresent()) {
            System.out.println("\nPrint with ID=" + id + ":\n" + legoSetService.findById(id).get());
        } else {
            System.out.println("There is no LegoSet with ID: " + id);
        }
    }

    private void updateLegoSetWithCatalogNumber(String catalogNumber) {
        legoSetService.findByCatalogNumber(catalogNumber).ifPresent(
                legoSet -> {
                    UpdateLegoSetCommand command = UpdateLegoSetCommand
                            .builder()
                            .id(legoSet.getId())
                            .status(LegoSetStatus.PURCHASED)
                            .purchasingDate(LocalDate.of(2021, 12, 24))
                            .category(LegoCategory.CITY)
                            .numberOfPieces(250)
                            .build();
                    UpdateLegoSetResponse response = legoSetService.updateLegoSet(command);
                    System.out.println("\nUpdating LegoSet with ID: " + legoSet.getId() + "\nResult: " + response.isSuccess());
                });
    }

    private void changeStatusOfLegoSetToPurchased(Long id) {
        legoSetService.changeStatusToPurchased(id);
    }

}
