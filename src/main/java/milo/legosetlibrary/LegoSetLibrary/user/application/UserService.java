package milo.legosetlibrary.LegoSetLibrary.user.application;

import lombok.AllArgsConstructor;
import milo.legosetlibrary.LegoSetLibrary.user.application.port.UserUseCase;
import milo.legosetlibrary.LegoSetLibrary.user.db.UserJpaRepository;
import milo.legosetlibrary.LegoSetLibrary.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserUseCase {

    private final UserJpaRepository userRepository;

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
    public UpdateUserResponse updateUser(UpdateUserCommand command) {
        return userRepository
                .findById(command.getId())
                .map(user -> {
                    User updatedUser = updateFields(command,user);
                    userRepository.save(updatedUser);
                    return UpdateUserResponse.SUCCESS;
                }).orElseGet(()-> new UpdateUserResponse(false, Collections.singletonList("Book not found with id: " + command.getId())));
    }

    private User updateFields(UpdateUserCommand command, User user) {
        if (command.getLogin() != null) {
                user.setLogin(command.getLogin());
        }
        return user;
    }

    @Override
    public User addUser(CreateUserCommand command) {
        User user = toUser(command);
        return userRepository.save(user);
    }

    private User toUser(CreateUserCommand command) {
        User user = new User(command.getLogin());
        return user;
    }
}
