package finder.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import finder.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByChatId(long id);
}
