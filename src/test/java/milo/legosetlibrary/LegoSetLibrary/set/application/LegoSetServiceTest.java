package milo.legosetlibrary.LegoSetLibrary.set.application;

import milo.legosetlibrary.LegoSetLibrary.set.application.port.LegoSetUseCase.CreateLegoSetCommand;
import milo.legosetlibrary.LegoSetLibrary.set.application.port.LegoSetUseCase.UpdateLegoSetCommand;
import milo.legosetlibrary.LegoSetLibrary.set.application.port.LegoSetUseCase.UpdateLegoSetResponse;
import milo.legosetlibrary.LegoSetLibrary.set.db.LegoSetJpaRepository;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoCategory;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSet;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSetStatus;
import milo.legosetlibrary.LegoSetLibrary.uploads.application.UploadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({LegoSetService.class, UploadService.class})
class LegoSetServiceTest {

    @Autowired
    LegoSetJpaRepository legoSetRepository;

    @Autowired
    UploadService uploadService;

    @Autowired
    LegoSetService legoSetService;

    @Test
    public void twoLegoSetsShouldBeAdded() {
        //given
        CreateLegoSetCommand legoSetCity = givenLegoSetCityCreateCommand();
        CreateLegoSetCommand legoSetStarWars = givenLegoSetStarWarsCreateCommand();

        //when
        legoSetService.addLegoSet(legoSetCity);
        legoSetService.addLegoSet(legoSetStarWars);

        //then
        assertEquals(2, legoSetService.findAll().size());
    }

    @Test
    public void twoLegoSetsShouldBeFoundByCategory() {
        //given
        CreateLegoSetCommand legoSetCity = givenLegoSetCityCreateCommand();
        CreateLegoSetCommand legoSetStarWars = givenLegoSetStarWarsCreateCommand();
        CreateLegoSetCommand legoSetStarWars2 = givenLegoSetStarWarsNo2CreateCommand();

        //when
        legoSetService.addLegoSet(legoSetCity);
        legoSetService.addLegoSet(legoSetStarWars);
        legoSetService.addLegoSet(legoSetStarWars2);

        //then
        assertEquals(2, legoSetService.findByCategory(LegoCategory.STAR_WARS).size());
    }

    @Test
    public void newLegoSetsShouldHaveOnDreamListStatus() {
        //given
        CreateLegoSetCommand legoSetCity = givenLegoSetCityCreateCommand();
        CreateLegoSetCommand legoSetStarWars = givenLegoSetStarWarsCreateCommand();

        //when
        legoSetService.addLegoSet(legoSetCity);
        legoSetService.addLegoSet(legoSetStarWars);

        //then
        assertEquals(2, legoSetService.findByStatus(LegoSetStatus.ON_DREAM_LIST).size());
    }

    @Test
    public void statusOfLegoSetShouldBeChangedOnPurchased() {
        //given
        CreateLegoSetCommand legoSetCityCommand = givenLegoSetCityCreateCommand();
        CreateLegoSetCommand legoSetStarWarsCommand = givenLegoSetStarWarsCreateCommand();

        //when
        LegoSet legoSetCity = legoSetService.addLegoSet(legoSetCityCommand);
        LegoSet legoSetStarWars = legoSetService.addLegoSet(legoSetStarWarsCommand);

        legoSetService.changeStatusToPurchased(legoSetCity.getId());
        legoSetService.changeStatusToPurchased(legoSetStarWars.getId());

        //then
        assertEquals(2, legoSetService.findByStatus(LegoSetStatus.PURCHASED).size());
    }

    @Test
    public void legoSetShouldBeUpdated() {
        //given
        CreateLegoSetCommand legoSetCityCommand = givenLegoSetCityCreateCommand();


        //when
        LegoSet legoSetCity = legoSetService.addLegoSet(legoSetCityCommand);
        UpdateLegoSetCommand updateLegoSetCommand = givenLegoSetCityUpdateCommand(legoSetCity.getId());
        UpdateLegoSetResponse response = legoSetService.updateLegoSet(updateLegoSetCommand);

        //then
        assertTrue(response.isSuccess());
        assertEquals("654321", legoSetCity.getCatalogNumber());
        assertEquals(999, legoSetCity.getNumberOfPieces());
    }

    private CreateLegoSetCommand givenLegoSetCityCreateCommand() {
        return new CreateLegoSetCommand(
                "61655",
                "set for test No2",
                LegoCategory.CITY,
                1000,
                new BigDecimal("100.00"),
                Set.of());
    }

    private CreateLegoSetCommand givenLegoSetStarWarsCreateCommand() {
        return new CreateLegoSetCommand(
                "61232",
                "set for test No2",
                LegoCategory.STAR_WARS,
                20,
                new BigDecimal("10.00"),
                Set.of()
        );
    }

    private CreateLegoSetCommand givenLegoSetStarWarsNo2CreateCommand() {
        return new CreateLegoSetCommand(
                "61111",
                "set for test No2",
                LegoCategory.STAR_WARS,
                30,
                new BigDecimal("15.00"),
                Set.of()
        );
    }

    private UpdateLegoSetCommand givenLegoSetCityUpdateCommand(Long id) {
        return UpdateLegoSetCommand
                .builder()
                .id(id)
                .catalogNumber("654321")
                .numberOfPieces(999)
                .build();
    }
}