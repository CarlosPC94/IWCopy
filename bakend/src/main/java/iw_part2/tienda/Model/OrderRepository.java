package iw_part2.tienda.Model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findAllByuser_id(Long id);
}
