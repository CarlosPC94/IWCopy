package iw_part2.tienda.Model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String s);

    @Query(value="select * from users", nativeQuery=true)
    List<User> getUsers();
}
