package milo.legosetlibrary.LegoSetLibrary.set.application.port;

import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoCategory;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSet;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSetStatus;

import java.util.List;
import java.util.Optional;

public interface LegoSetUseCase {

    List<LegoSet> findAll();

    Optional<LegoSet> findById();

    List<LegoSet> findByStatus(LegoSetStatus status);

    List<LegoSet> findByCategory(LegoCategory category);

    void removeById(Long id);

    LegoSet addBeehive(CreateLegoSetCommand command);

    UpdateLegoSetResponse updateLegoSet(UpdateLegoSetCommand command);

    class CreateLegoSetCommand {
    }

    class UpdateLegoSetResponse {
    }

    class UpdateLegoSetCommand {
    }

}
