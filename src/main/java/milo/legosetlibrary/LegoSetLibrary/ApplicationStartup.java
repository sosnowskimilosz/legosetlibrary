package milo.legosetlibrary.LegoSetLibrary;

import lombok.AllArgsConstructor;
import milo.legosetlibrary.LegoSetLibrary.set.application.port.LegoSetUseCase;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoCategory;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSetStatus;
import milo.legosetlibrary.LegoSetLibrary.user.application.port.UserUseCase;
import milo.legosetlibrary.LegoSetLibrary.user.db.UserJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

import static milo.legosetlibrary.LegoSetLibrary.set.application.port.LegoSetUseCase.*;

@Component
@AllArgsConstructor
public class ApplicationStartup implements CommandLineRunner {

    private final LegoSetUseCase legoSetService;
    private final UserUseCase userService;
    private final UserJpaRepository userRepository;


    @Override
    public void run(String... args) {
        initData();
        printAllLegoSet();
        printWithId(1L);
        updateLegoSetWithCatalogNumber("60215");
        changeStatusOfLegoSetToPurchased(2L);
        printAllLegoSet();
        initSomeUsers();
    }

    private void initData() {
        legoSetService.addLegoSet(new CreateLegoSetCommand("60215", "Fire brigade", LegoSetStatus.ON_DREAM_LIST, null, LegoCategory.DISNEY, 230, BigDecimal.valueOf(239)));
        legoSetService.addLegoSet(new CreateLegoSetCommand("60264", "Ocean submarine", LegoSetStatus.ON_DREAM_LIST, null, LegoCategory.CITY, 123, BigDecimal.valueOf(120)));
        legoSetService.addLegoSet(new CreateLegoSetCommand("75257", "Star Wars - Millenium", LegoSetStatus.ON_DREAM_LIST, null, LegoCategory.STAR_WARS, 1351, BigDecimal.valueOf(700)));
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

    private void initSomeUsers() {
        userService.addUser(new UserUseCase.CreateUserCommand("user123"));
        userService.addUser(new UserUseCase.CreateUserCommand("user1321"));
        userService.addUser(new UserUseCase.CreateUserCommand("user987"));
        userService.addUser(new UserUseCase.CreateUserCommand("user1222"));
    }
}
