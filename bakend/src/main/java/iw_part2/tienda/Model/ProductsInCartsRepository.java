package iw_part2.tienda.Model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductsInCartsRepository extends CrudRepository<ProductsInCarts, ProductsInCartsKey> {

    @Query(value="select * from products_in_carts where product_id=?1 and cart_id=?2", nativeQuery=true)
    ProductsInCarts getAllWhere(Long product_id, Long Cart_id);

    @Query(value="select * from products_in_carts where cart_id=?1", nativeQuery=true)
    List<ProductsInCarts> getAllbyIdCart( Long Cart_id);
}
