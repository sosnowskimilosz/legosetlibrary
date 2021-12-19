package milo.legosetlibrary.LegoSetLibrary.uploads.application.port;

import lombok.AllArgsConstructor;
import lombok.Value;
import milo.legosetlibrary.LegoSetLibrary.uploads.domain.Upload;

import java.util.Optional;

public interface UploadUseCase {

    Upload save(SaveUploadCommand command);

    Optional<Upload> getById(Long id);

    void removeById(Long id);

    @Value
    @AllArgsConstructor
    class SaveUploadCommand {
        String filename;
        String contentType;
        byte[] file;
    }
}
