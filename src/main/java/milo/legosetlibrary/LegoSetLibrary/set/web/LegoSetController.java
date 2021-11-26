package milo.legosetlibrary.LegoSetLibrary.set.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import milo.legosetlibrary.LegoSetLibrary.set.application.port.LegoSetUseCase;
import milo.legosetlibrary.LegoSetLibrary.set.application.port.LegoSetUseCase.CreateLegoSetCommand;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoCategory;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<LegoSet> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LegoSet addLegoSet(@RequestBody RestCreateLegoSetCommand command) {
        return service.addLegoSet(command.toCommand());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLegoSetById(@PathVariable Long id){
        service.removeById(id);
    }

    @Data
    private static class RestCreateLegoSetCommand {
        String catalogNumber;
        String title;
        LegoCategory category;
        Integer numberOfPieces;
        BigDecimal price;

        CreateLegoSetCommand toCommand() {
            return new CreateLegoSetCommand(catalogNumber, title, category, numberOfPieces, price);
        }
    }
}
