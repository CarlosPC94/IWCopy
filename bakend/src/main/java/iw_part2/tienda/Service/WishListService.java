package iw_part2.tienda.Service;

import iw_part2.tienda.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class WishListService {

    private WishListRepository wishListRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;


    @Autowired
    public WishListService(WishListRepository wishListRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.wishListRepository = wishListRepository;
        this.productRepository=productRepository;
        this.userRepository=userRepository;
    }

    @Transactional
    public Optional<WishList> findById(Long id){
        return wishListRepository.findById(id);
    }

    @Transactional
    public WishList registrar(WishList wishList) {
        Optional <WishList> wishListDB = wishListRepository.findWishListByUser(wishList.getUser().getId());
        if (wishListDB.isPresent())
            throw new RuntimeException("El usuario" + wishList.getUser().getEmail() + " ya posee una WishList");
        else
            return wishListRepository.save(wishList);
    }

    @Transactional
    public WishList addObjects(Long id, Long id_product){

        WishList wish = wishListRepository.findById(id).orElse(null);

        Product product = productRepository.findById(id_product).orElse(null);


        Set<Product>productos= new HashSet<Product>(wish.getProducts());
        productos.add(product);

        wish.setProducts(productos);

        List<WishList> wishLists = product.getWishlists();
        wishLists.add(wish);
        product.setWishlists(wishLists);

        wishListRepository.save(wish);
        productRepository.save(product);
        return  wish;

    }

    @Transactional
    public List<Product> getObjects(Long id) {
       WishList wishListDB = wishListRepository.findById(id).orElse(null);

        List<Product> productList = new ArrayList(wishListDB.getProducts());

      return  productList ;

    }

    @Transactional
    public Set<Product> deleteProduct(Long id, Long id_product){
        WishList wishListDB = wishListRepository.findById(id).orElse(null);
        Product product = productRepository.findById(id_product).orElse((null));
        if (wishListDB.getProducts().contains(product)){
            Set<Product> productList = new HashSet<>(wishListDB.getProducts());
            List<WishList> wishLists = new ArrayList<>(product.getWishlists());
            productList.remove(product);
            wishLists.remove(wishListDB);
            product.setWishlists(wishLists);
            wishListDB.setProducts(productList);
            productRepository.save(product);
            wishListRepository.save(wishListDB);
            return wishListDB.getProducts();
        }
        else {
            throw new RuntimeException("Ese producto no pertenece a tu wishlist");
        }
    }

}
