package iw_part2.tienda.Model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProductInOrdersRepository extends CrudRepository<ProductsInOrders, ProductsInOrdersKey> {

}

