package iw_part2.tienda.Model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;

public interface GroupRepository extends CrudRepository<Group, Long> {
    Optional<Group> findById(Long id);
    Optional<Group> findByName(String name);

    @Query(value="select * from groups", nativeQuery=true)
    List<Group> getGroups();
}
