package milo.legosetlibrary.LegoSetLibrary.set.web;

import milo.legosetlibrary.LegoSetLibrary.set.application.port.LegoSetUseCase;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoCategory;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSet;
import milo.legosetlibrary.LegoSetLibrary.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LegoSetController.class})
class LegoSetControllerTest {

    @MockBean
    LegoSetUseCase legoSetUseCase;

    @Autowired
    LegoSetController legoSetController;

    @Test
    public void shouldGetAllLegoSet() {
        //given
        LegoSet legoSetCity = givenLegoSetCity();
        LegoSet legoSetStarWars = givenLegoSetStarWars();
        Mockito.when(legoSetUseCase.findAll()).thenReturn(List.of(legoSetCity, legoSetStarWars));

        //when
        List<LegoSet> allLegoSet = legoSetController.getAll();

        //then
        assertEquals(2, allLegoSet.size());
    }


    private LegoSet givenLegoSetCity() {
        LegoSet legoSet = new LegoSet("61655",
                "set for test from Lego City",
                LegoCategory.CITY,
                1000,
                new BigDecimal("100.00"));
        return legoSet;
    }

    private LegoSet givenLegoSetStarWars() {
        LegoSet legoSet = new LegoSet("79655",
                "set for test from Lego Star Wars",
                LegoCategory.STAR_WARS,
                500,
                new BigDecimal("50.00"));
        return legoSet;
    }

    private User givenUserNo1() {
        return new User("marian123");
    }

    private User givenUserNo2() {
        return new User("milosz321");
    }
}
