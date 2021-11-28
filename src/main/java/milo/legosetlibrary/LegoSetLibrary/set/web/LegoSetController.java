package milo.legosetlibrary.LegoSetLibrary.set.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import milo.legosetlibrary.LegoSetLibrary.set.application.port.LegoSetUseCase;
import milo.legosetlibrary.LegoSetLibrary.set.application.port.LegoSetUseCase.CreateLegoSetCommand;
import milo.legosetlibrary.LegoSetLibrary.set.application.port.LegoSetUseCase.UpdateLegoSetCommand;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoCategory;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSet;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSetStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    public LegoSet addLegoSet(@Valid @RequestBody RestLegoSetCommand command) {
        return service.addLegoSet(command.toCreateCommand());
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
        @Min(0)
        Integer numberOfPieces;
        @DecimalMin("0.00")
        BigDecimal price;

        CreateLegoSetCommand toCreateCommand() {
            return new CreateLegoSetCommand(catalogNumber, title, status, purchasingDate, category, numberOfPieces, price);
        }

        UpdateLegoSetCommand toUpdateCommand(Long id) {
            return new UpdateLegoSetCommand(id, catalogNumber, title, status, purchasingDate, category, numberOfPieces, price);
        }
    }
}
