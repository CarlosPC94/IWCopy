package iw_part2.tienda.Model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductImageRepository extends CrudRepository<ProductImage, Long> {
    @Query(value="select * from product_image where product=?1", nativeQuery=true)
    List<ProductImage> getProductImages(Long productID);
}
