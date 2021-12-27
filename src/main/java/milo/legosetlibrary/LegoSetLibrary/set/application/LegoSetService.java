package milo.legosetlibrary.LegoSetLibrary.set.application;

import lombok.AllArgsConstructor;
import milo.legosetlibrary.LegoSetLibrary.set.application.port.LegoSetUseCase;
import milo.legosetlibrary.LegoSetLibrary.set.db.LegoSetJpaRepository;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoCategory;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSet;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSetStatus;
import milo.legosetlibrary.LegoSetLibrary.uploads.application.port.UploadUseCase;
import milo.legosetlibrary.LegoSetLibrary.uploads.application.port.UploadUseCase.SaveUploadCommand;
import milo.legosetlibrary.LegoSetLibrary.uploads.domain.Upload;
import milo.legosetlibrary.LegoSetLibrary.user.db.UserJpaRepository;
import milo.legosetlibrary.LegoSetLibrary.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LegoSetService implements LegoSetUseCase {

    private final UserJpaRepository userRepository;
    private final LegoSetJpaRepository legoSetRepository;
    private final UploadUseCase upload;

    @Override
    public List<LegoSet> findAll() {
        return legoSetRepository.findAll();
    }

    @Override
    public Optional<LegoSet> findById(Long id) {
        return legoSetRepository.findById(id);
    }

    @Override
    public Optional<LegoSet> findByCatalogNumber(String number) {
        return legoSetRepository.findAll()
                .stream()
                .filter(legoSet -> legoSet.getCatalogNumber().startsWith(number))
                .findFirst();
    }

    @Override
    public List<LegoSet> findByStatus(LegoSetStatus status) {
        return legoSetRepository.findAll()
                .stream()
                .filter(legoSet -> legoSet.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    @Override
    public List<LegoSet> findByCategory(LegoCategory category) {
        return legoSetRepository.findAll()
                .stream()
                .filter(legoSet -> legoSet.getCategory() == category)
                .collect(Collectors.toList());
    }

    @Override
    public void removeById(Long id) {
        legoSetRepository.deleteById(id);
    }

    @Override
    @Transactional
    public LegoSet addLegoSet(CreateLegoSetCommand command) {
        LegoSet legoSet = toLegoSet(command);
        return legoSetRepository.save(legoSet);
    }

    public LegoSet toLegoSet(CreateLegoSetCommand command) {
        LegoSet legoSet = new LegoSet(command.getCatalogNumber(), command.getTitle(), command.getCategory(), command.getNumberOfPieces(), command.getPrice());
        Set<User> users = fetchUsersByIds(command.getUsers());
        updateLegoSets(legoSet, users);
        return legoSet;
    }

    private void updateLegoSets(LegoSet legoSet, Set<User> users) {
        legoSet.removeUsers();
        users.forEach(user -> legoSet.addUser(user));
    }

    private Set<User> fetchUsersByIds(Set<Long> users) {
        return users
                .stream()
                .map(userId -> userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("Unable to find user with id: " + userId))
                )
                .collect(Collectors.toSet());
    }

    public LegoSet updateFields(UpdateLegoSetCommand command, LegoSet legoSet) {
        if (command.getCatalogNumber() != null) {
            legoSet.setCatalogNumber(command.getCatalogNumber());
        }
        if (command.getTitle() != null) {
            legoSet.setTitle(command.getTitle());
        }
        if (command.getStatus() != null) {
            legoSet.setStatus(command.getStatus());
        }
        if (command.getPurchasingDate() != null) {
            legoSet.setPurchasingDate(command.getPurchasingDate());
        }
        if (command.getCategory() != null) {
            legoSet.setCategory(command.getCategory());
        }
        if (command.getNumberOfPieces() != null) {
            legoSet.setNumberOfPieces(command.getNumberOfPieces());
        }
        if (command.getPrice() != null) {
            legoSet.setPrice(command.getPrice());
        }
        if (command.getUsers() != null && command.getUsers().size() > 0) {
            updateLegoSets(legoSet, fetchUsersByIds(command.getUsers()));
        }
        return legoSet;
    }


    @Override
    @Transactional
    public UpdateLegoSetResponse updateLegoSet(UpdateLegoSetCommand command) {
        return legoSetRepository.findById(command.getId())
                .map(legoSet -> {
                    LegoSet updatedLegoSet = updateFields(command, legoSet);
                    legoSetRepository.save(updatedLegoSet);
                    return UpdateLegoSetResponse.SUCCESS;
                }).orElseGet(() -> new UpdateLegoSetResponse(
                        false, Collections.singletonList("LegoSet not found with id: " + command.getId())));
    }

    @Override
    public UpdateLegoSetResponse changeStatusToPurchased(Long id) {
        return legoSetRepository.findById(id)
                .map(legoSet -> {
                    if (legoSet.getStatus().equals(LegoSetStatus.ON_DREAM_LIST)) {
                        legoSet.setStatus(LegoSetStatus.PURCHASED);
                        legoSet.setPurchasingDate(LocalDate.now());
                        legoSetRepository.save(legoSet);
                        return UpdateLegoSetResponse.SUCCESS;
                    } else {
                        return new UpdateLegoSetResponse(false, Collections.singletonList("Set with id=" + id + " is purchased"));
                    }
                }).orElseGet(() -> new UpdateLegoSetResponse(false, Collections.singletonList("Set with id=" + id + " is not found")));
    }

    @Override
    public void updateBoxCover(UpdateBoxCoverCommand command) {
        int length = command.getFile().length;
        System.out.println("Receive cover command: " + command.getFilename() + " bytes: " + length);
        legoSetRepository.findById(command.getId())
                .ifPresent(legoSet -> {
                    Upload savedUpload = upload.save(new SaveUploadCommand(command.getFilename(), command.getContentType(), command.getFile()));
                    legoSet.setCoverOfBoxId(savedUpload.getId());
                    legoSetRepository.save(legoSet);
                });
    }

    @Override
    public void removeBoxCover(Long id) {
        legoSetRepository.findById(id)
                .ifPresent(legoSet -> {
                    if (legoSet.getCoverOfBoxId() != null) {
                        upload.removeById(legoSet.getCoverOfBoxId());
                        legoSet.setCoverOfBoxId(null);
                        legoSetRepository.save(legoSet);
                    }
                });
    }
}
