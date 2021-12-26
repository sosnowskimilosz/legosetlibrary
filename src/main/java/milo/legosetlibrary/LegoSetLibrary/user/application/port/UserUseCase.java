package milo.legosetlibrary.LegoSetLibrary.user.application.port;

import lombok.Value;
import milo.legosetlibrary.LegoSetLibrary.user.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserUseCase {

    List<User> findAll();

    Optional<User> findById(Long id);

    void removeById(Long id);

    UpdateUserResponse updateUser(UpdateUserCommand command);

    User addUser(CreateUserCommand command);

    @Value
    class CreateUserCommand {
        String login;
        Set<Long> legoSets;
    }

    @Value
    class UpdateUserCommand {
        Long id;
        String login;
        Set<Long> legoSets;
    }

    @Value
    class UpdateUserResponse {
        public static UpdateUserResponse SUCCESS = new UpdateUserResponse(true, List.of());

        boolean success;
        List<String> errors;
    }
}
