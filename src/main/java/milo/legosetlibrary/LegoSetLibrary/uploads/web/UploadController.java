package milo.legosetlibrary.LegoSetLibrary.uploads.web;

import lombok.AllArgsConstructor;
import lombok.Value;
import milo.legosetlibrary.LegoSetLibrary.uploads.application.port.UploadUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/uploads")
@AllArgsConstructor
public class UploadController {

    private final UploadUseCase upload;

    @GetMapping("/{id}")
    public ResponseEntity<UploadResponse> getUpload(@PathVariable String id) {
        return upload.getById(id)
                .map(file -> {
                    UploadResponse response = new UploadResponse(
                            file.getId(),
                            file.getContentType(),
                            file.getFilename(),
                            file.getCreatedAt()
                    );
                    return ResponseEntity.ok(response);
                }).orElse(ResponseEntity.notFound().build());
    }

    @Value
    @AllArgsConstructor
    static class UploadResponse {
        String id;
        String contentType;
        String filename;
        LocalDateTime createdAt;
    }
}
