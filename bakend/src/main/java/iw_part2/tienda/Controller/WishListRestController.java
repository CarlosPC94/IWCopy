package iw_part2.tienda.Controller;

import iw_part2.tienda.Model.Product;
import iw_part2.tienda.Model.WishList;
import iw_part2.tienda.Service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/wishList/")
public class WishListRestController {

    @Autowired
    private WishListService wishListService;

    @PostMapping(value = "/{id_wishlist}/add/{id}")
    public WishList add(@PathVariable(value = "id_wishlist") Long idwishlist,@PathVariable(value = "id") Long idProduct){
        return wishListService.addObjects(idwishlist,idProduct);
    }
    @PostMapping(value = "/create")
    public WishList create(@PathVariable(value = "id_wishlist") Long idwishlist,@PathVariable(value = "id") Long idProduct){
        return wishListService.addObjects(idwishlist,idProduct);
    }
    @GetMapping(value = "/products/{id}")
    public List<Product> getproducts(@PathVariable(value = "id") Long id) {
        return wishListService.getObjects(id);
    }

    @PostMapping(value = "{wish_id}/deleteProduct/{prod_id}")
    public Set<Product> deleteProduct(@PathVariable(value = "wish_id") Long wish_id, @PathVariable(value = "prod_id") Long prod_id){
        Set<Product> products = wishListService.deleteProduct(wish_id,prod_id);
        return products;
    }


}
