package milo.legosetlibrary.LegoSetLibrary.set.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import milo.legosetlibrary.LegoSetLibrary.set.application.port.LegoSetUseCase;
import milo.legosetlibrary.LegoSetLibrary.set.application.port.LegoSetUseCase.CreateLegoSetCommand;
import milo.legosetlibrary.LegoSetLibrary.set.application.port.LegoSetUseCase.UpdateLegoSetCommand;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoCategory;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSet;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSetStatus;
import milo.legosetlibrary.LegoSetLibrary.web.CreatedURI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static milo.legosetlibrary.LegoSetLibrary.set.application.port.LegoSetUseCase.*;

@RequestMapping("/legoset")
@RestController
@AllArgsConstructor
public class LegoSetController {

    private final LegoSetUseCase service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<LegoSet> getAll() {
        return service.findAll();
    }

    @GetMapping("/category")
    @ResponseStatus(HttpStatus.OK)
    public List<LegoSet> getByCategory(@RequestParam LegoCategory category) {
        return service.findByCategory(category);
    }

    @GetMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public List<LegoSet> getByStatus(@RequestParam LegoSetStatus status) {
        return service.findByStatus(status);
    }

    @GetMapping("/number")
    public ResponseEntity<LegoSet> getByCatalogNumber(@RequestParam String catalogNumber) {
        return service.findByCatalogNumber(catalogNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LegoSet> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addLegoSet(@Valid @RequestBody RestLegoSetCommand command) {
        LegoSet legoSet = service.addLegoSet(command.toCreateCommand());
        return ResponseEntity.created(createdLegoSetUri(legoSet)).build();
    }

    private URI createdLegoSetUri(LegoSet legoSet) {
        return new CreatedURI("/" + legoSet.getId().toString()).uri();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLegoSetById(@PathVariable Long id) {
        service.removeById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateLegoSet(@PathVariable Long id, @RequestBody RestLegoSetCommand command) {
        UpdateLegoSetResponse response = service.updateLegoSet(command.toUpdateCommand(id));
        if (!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @PutMapping("/{id}/cover")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addBoxCover(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("Got file: " + file.getOriginalFilename());
        service.updateBoxCover(new UpdateBoxCoverCommand(
                id,
                file.getBytes(),
                file.getContentType(),
                file.getOriginalFilename()
        ));
    }

    @PatchMapping("/{id}/makepurchased")
    public ResponseEntity<LegoSet> makePurchased(@PathVariable Long id) {
        UpdateLegoSetResponse response = service.changeStatusToPurchased(id);
        if (response.isSuccess()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Data
    private static class RestLegoSetCommand {
        @NotBlank(message = "LegoSet should have catalog number")
        String catalogNumber;
        @NotBlank(message = "LegoSet should have name")
        String title;
        @NotNull(message = "LegoSet should have status")
        LegoSetStatus status;
        LocalDate purchasingDate;
        @NotNull(message = "LegoSet should have status")
        LegoCategory category;
        @Min(value = 1, message = "Number of pieces should be more than 0")
        Integer numberOfPieces;
        @DecimalMin(value = "0.00", message = "Price should be more than 0,00")
        BigDecimal price;
        @NotEmpty
        Set<Long> users = new HashSet<>();

        CreateLegoSetCommand toCreateCommand() {
            return new CreateLegoSetCommand(catalogNumber, title, status, purchasingDate, category, numberOfPieces, price, users);
        }

        UpdateLegoSetCommand toUpdateCommand(Long id) {
            return new UpdateLegoSetCommand(id, catalogNumber, title, status, purchasingDate, category, numberOfPieces, price, users);
        }
    }
}
