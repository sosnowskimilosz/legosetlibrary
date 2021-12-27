package milo.legosetlibrary.LegoSetLibrary.user.application;

import lombok.AllArgsConstructor;
import milo.legosetlibrary.LegoSetLibrary.set.db.LegoSetJpaRepository;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSet;
import milo.legosetlibrary.LegoSetLibrary.user.application.port.UserUseCase;
import milo.legosetlibrary.LegoSetLibrary.user.db.UserJpaRepository;
import milo.legosetlibrary.LegoSetLibrary.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserUseCase {

    private final UserJpaRepository userRepository;
    private final LegoSetJpaRepository legoSetRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void removeById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public UpdateUserResponse updateUser(UpdateUserCommand command) {
        return userRepository
                .findById(command.getId())
                .map(user -> {
                    User updatedUser = updateFields(command, user);
                    userRepository.save(updatedUser);
                    return UpdateUserResponse.SUCCESS;
                }).orElseGet(() -> new UpdateUserResponse(false, Collections.singletonList("Book not found with id: " + command.getId())));
    }

    private User updateFields(UpdateUserCommand command, User user) {
        if (command.getLogin() != null) {
            user.setLogin(command.getLogin());
        }
        if (command.getLegoSets() != null && command.getLegoSets().size() > 0) {
            updateUsers(user, fetchLegoSetsByIds(command.getLegoSets()));
        }
        return user;
    }

    @Override
    @Transactional
    public User addUser(CreateUserCommand command) {
        User user = toUser(command);
        return userRepository.save(user);
    }

    private User toUser(CreateUserCommand command) {
        User user = new User(command.getLogin());
        Set<LegoSet> legoSets = fetchLegoSetsByIds(command.getLegoSets());
        updateUsers(user, legoSets);
        return user;
    }

    private void updateUsers(User user, Set<LegoSet> legoSets) {
        user.removeLegoSets();
        legoSets.forEach(legoSet -> user.addLegoSet(legoSet));
    }

    private Set<LegoSet> fetchLegoSetsByIds(Set<Long> legoSets) {
        return legoSets
                .stream()
                .map(legoSetId -> legoSetRepository.findById(legoSetId)
                        .orElseThrow(() -> new IllegalArgumentException("Unable to find legoSet with Id: " + legoSetId))
                )
                .collect(Collectors.toSet());
    }


}
