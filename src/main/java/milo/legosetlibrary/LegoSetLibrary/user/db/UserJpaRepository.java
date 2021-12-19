package milo.legosetlibrary.LegoSetLibrary.user.db;

import milo.legosetlibrary.LegoSetLibrary.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User,Long> {
}
