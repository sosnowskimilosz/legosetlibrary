package milo.legosetlibrary.LegoSetLibrary.set.application;

import milo.legosetlibrary.LegoSetLibrary.set.application.port.LegoSetUseCase;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoCategory;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSet;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSetStatus;

import java.util.List;
import java.util.Optional;

public class LegoSetService implements LegoSetUseCase {

    @Override
    public List<LegoSet> findAll() {
        return null;
    }

    @Override
    public Optional<LegoSet> findById() {
        return Optional.empty();
    }

    @Override
    public List<LegoSet> findByStatus(LegoSetStatus status) {
        return null;
    }

    @Override
    public List<LegoSet> findByCategory(LegoCategory category) {
        return null;
    }

    @Override
    public void removeById(Long id) {

    }

    @Override
    public LegoSet addBeehive(CreateLegoSetCommand command) {
        return null;
    }

    @Override
    public UpdateLegoSetResponse updateLegoSet(UpdateLegoSetCommand command) {
        return null;
    }
}
