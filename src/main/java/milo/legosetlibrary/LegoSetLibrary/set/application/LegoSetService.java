package milo.legosetlibrary.LegoSetLibrary.set.application;

import lombok.AllArgsConstructor;
import milo.legosetlibrary.LegoSetLibrary.set.application.port.LegoSetUseCase;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoCategory;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSet;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSetRepository;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSetStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LegoSetService implements LegoSetUseCase {

    private final LegoSetRepository repository;

    @Override
    public List<LegoSet> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<LegoSet> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<LegoSet> findByCatalogNumber(String number) {
        return repository.findAll()
                .stream()
                .filter(legoSet -> legoSet.getCatalogNumber().startsWith(number))
                .findFirst();
    }

    @Override
    public List<LegoSet> findByStatus(LegoSetStatus status) {
        return repository.findAll()
                .stream()
                .filter(legoSet -> legoSet.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    @Override
    public List<LegoSet> findByCategory(LegoCategory category) {
        return repository.findAll()
                .stream()
                .filter(legoSet -> legoSet.getCategory() == category)
                .collect(Collectors.toList());
    }

    @Override
    public void removeById(Long id) {
        repository.removeById(id);
    }

    @Override
    public LegoSet addLegoSet(CreateLegoSetCommand command) {
        LegoSet legoSet = command.toLegoSet();
        return repository.save(legoSet);
    }

    @Override
    public UpdateLegoSetResponse updateLegoSet(UpdateLegoSetCommand command) {
        return repository.findById(command.getId())
                .map(legoSet -> {
                    LegoSet updatedLegoSet = command.updateFields(legoSet);
                    repository.save(updatedLegoSet);
                    return UpdateLegoSetResponse.SUCCESS;
                }).orElseGet(() -> new UpdateLegoSetResponse(
                        false, Collections.singletonList("LegoSet not found with id: " + command.getId())));
    }

    @Override
    public UpdateLegoSetResponse changeStatusToPurchased(Long id) {
        return repository.findById(id)
                .map(legoSet -> {
                    if (legoSet.getStatus().equals(LegoSetStatus.ON_DREAM_LIST)) {
                        legoSet.setStatus(LegoSetStatus.PURCHASED);
                        legoSet.setPurchasingDate(LocalDate.now());
                        repository.save(legoSet);
                        return UpdateLegoSetResponse.SUCCESS;
                    } else {
                        return new UpdateLegoSetResponse(false, Collections.singletonList("Set with id=" + id + " is purchased"));
                    }
                }).orElseGet(() -> new UpdateLegoSetResponse(false, Collections.singletonList("Set with id=" + id + " is not found")));
    }
}
