package iw_part2.tienda.Model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product,Long> {
    @Query(value="select * from product where category=?1", nativeQuery=true)
    List<Product> getAllByCategory(Long num);
    @Query(value="select * from product where subcategory=?1", nativeQuery=true)
    List<Product> getAllBySubcategory(Long num);
    @Query(value="select * from product", nativeQuery=true)
    List<Product> getProducts();
    @Query(value="select * from wishlist_product where wishlist_id=?1", nativeQuery=true)
    List<Product> getAllByWishList(Long id);
    @Query(value="select * from product where name=?1", nativeQuery=true)
    Optional<List<Product>> findByName(String name);
}
