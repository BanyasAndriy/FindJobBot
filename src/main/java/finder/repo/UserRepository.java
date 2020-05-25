package finder.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import finder.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

 /*   @Query("SELECT u FROM User u WHERE u.notified = false " +
            "AND u.phone IS NOT NULL AND u.email IS NOT NULL")
    List<User> findNewUsers();
*/
    User findByChatId(long id);
}
