package iw_part2.tienda.Model;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WishListRepository extends CrudRepository<WishList, Long> {
    Optional<WishList> findWishListByUser(Long id);
    Optional<WishList> findWishListById(Long id);
}
