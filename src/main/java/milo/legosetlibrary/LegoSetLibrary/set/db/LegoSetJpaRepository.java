package milo.legosetlibrary.LegoSetLibrary.set.db;

import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LegoSetJpaRepository extends JpaRepository<LegoSet, Long> {
}
