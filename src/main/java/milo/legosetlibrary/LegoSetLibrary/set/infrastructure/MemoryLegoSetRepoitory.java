package milo.legosetlibrary.LegoSetLibrary.set.infrastructure;

import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSet;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSetRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemoryLegoSetRepoitory implements LegoSetRepository {

    private final Map<Long, LegoSet> storage = new ConcurrentHashMap<>();
    private final AtomicLong ID_NEXT_VALUE = new AtomicLong(0L);

    @Override
    public List<LegoSet> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public LegoSet save(LegoSet legoSet) {
        if (legoSet.getId() != null) {
            storage.put(legoSet.getId(), legoSet);
        } else {
            long nextId = nextId();
            legoSet.setId(nextId);
            storage.put(nextId, legoSet);
        }
        return legoSet;
    }

    @Override
    public Optional<LegoSet> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void removeById(Long id) {
        storage.remove(id);
    }

    private long nextId() {
        return ID_NEXT_VALUE.getAndIncrement();
    }
}
