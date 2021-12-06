package milo.legosetlibrary.LegoSetLibrary.uploads.application.port;

import lombok.AllArgsConstructor;
import lombok.Value;
import milo.legosetlibrary.LegoSetLibrary.uploads.domain.Upload;

import java.util.Optional;

public interface UploadUseCase {

    Upload save(SaveUploadCommand command);

    Optional<Upload> getById(String id);

    void removeById(String coverOfBoxId);

    @Value
    @AllArgsConstructor
    class SaveUploadCommand {
        String filename;
        byte[] file;
        String contentType;
    }
}