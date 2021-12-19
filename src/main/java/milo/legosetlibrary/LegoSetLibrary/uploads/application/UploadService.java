package milo.legosetlibrary.LegoSetLibrary.uploads.application;

import lombok.AllArgsConstructor;
import milo.legosetlibrary.LegoSetLibrary.uploads.application.port.UploadUseCase;
import milo.legosetlibrary.LegoSetLibrary.uploads.db.UploadJpaRepository;
import milo.legosetlibrary.LegoSetLibrary.uploads.domain.Upload;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class UploadService implements UploadUseCase {

    private final UploadJpaRepository repository;

    @Override
    public Upload save(SaveUploadCommand command) {
        String newId = RandomStringUtils.randomAlphanumeric(8).toLowerCase();
        Upload upload = new Upload(
                command.getFilename(),
                command.getContentType(),
                command.getFile()
                );
        repository.save(upload);
        System.out.println("Upload saved: " + upload.getFilename() + " with id: " + newId);
        return upload;
    }

    @Override
    public Optional<Upload> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void removeById(Long id) {
        repository.deleteById(id);
    }
}
