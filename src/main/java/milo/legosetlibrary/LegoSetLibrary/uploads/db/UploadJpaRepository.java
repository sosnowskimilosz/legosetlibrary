package milo.legosetlibrary.LegoSetLibrary.uploads.db;

import milo.legosetlibrary.LegoSetLibrary.uploads.domain.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadJpaRepository extends JpaRepository<Upload, Long> {
}
