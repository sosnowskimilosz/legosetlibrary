package milo.legosetlibrary.LegoSetLibrary.set.infrastructure;

import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSet;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSetRepository;

import java.util.List;
import java.util.Optional;

public class MemoryLegoSetRepoitory implements LegoSetRepository {

    @Override
    public List<LegoSet> findAll() {
        return null;
    }

    @Override
    public LegoSet save(LegoSet set) {
        return null;
    }

    @Override
    public Optional<LegoSet> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void removeById(Long id) {

    }
}
