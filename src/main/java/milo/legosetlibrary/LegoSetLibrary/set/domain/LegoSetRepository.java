package milo.legosetlibrary.LegoSetLibrary.set.domain;

import java.util.List;
import java.util.Optional;

public interface LegoSetRepository {

    List<LegoSet> findAll();

    LegoSet save (LegoSet set);

    Optional<LegoSet> findById(Long id);

    void removeById(Long id);
}
