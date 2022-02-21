package milo.legosetlibrary.LegoSetLibrary.user.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import milo.legosetlibrary.LegoSetLibrary.user.application.port.UserUseCase;
import milo.legosetlibrary.LegoSetLibrary.user.application.port.UserUseCase.CreateUserCommand;
import milo.legosetlibrary.LegoSetLibrary.user.application.port.UserUseCase.UpdateUserCommand;
import milo.legosetlibrary.LegoSetLibrary.user.application.port.UserUseCase.UpdateUserResponse;
import milo.legosetlibrary.LegoSetLibrary.user.domain.User;
import milo.legosetlibrary.LegoSetLibrary.web.CreatedURI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Secured({"ROLE_ADMIN"})
public class UserController {

    private final UserUseCase userService;

    //admin
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAll() {
        return userService.findAll();
    }

    //admin
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    //admin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addUser(@Valid @RequestBody RestUserCommand command) {
        User user = userService.addUser(command.toCreateUserCommand());
        URI uri = createdUserUri(user);
        return ResponseEntity.created(uri).build();
    }

    private URI createdUserUri(User user) {
        return new CreatedURI("/" + user.getId().toString()).uri();
    }

    //admin
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id){
        userService.removeById(id);
    }

    //admin
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateUser(@PathVariable Long id, @RequestBody RestUserCommand command){
        UpdateUserResponse response = userService.updateUser(command.toUpdateUserCommand(id));
        if(!response.isSuccess()){
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @Data
    private static class RestUserCommand {
        @NotBlank
        private String login;
        Set<Long> legoSets;

        CreateUserCommand toCreateUserCommand() {
            return new CreateUserCommand(login, legoSets);
        }
        UpdateUserCommand toUpdateUserCommand(Long id) {
            return new UpdateUserCommand(id, login, legoSets);
        }
    }
}
