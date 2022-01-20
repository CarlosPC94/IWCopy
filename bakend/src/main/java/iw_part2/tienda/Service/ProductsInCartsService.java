package iw_part2.tienda.Service;

import iw_part2.tienda.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductsInCartsService {

    private ProductsInCartsRepository productsInCartsRepository;

    @Autowired
    public ProductsInCartsService(ProductsInCartsRepository productsInCartsRepository){
        this.productsInCartsRepository = productsInCartsRepository;
    }

    @Transactional
    public void addProductToCart(Product product, Cart cart){
        ProductsInCarts productsInCartsDB = productsInCartsRepository.getAllWhere(product.getId(), cart.getId());
        if (productsInCartsDB == null){
            ProductsInCartsKey productsInCartsKey = new ProductsInCartsKey();
            productsInCartsKey.setProductId(product.getId());
            productsInCartsKey.setCartId(cart.getId());
            ProductsInCarts productsInCarts = new ProductsInCarts();
            productsInCarts.setProduct(product);
            productsInCarts.setCart(cart);
            productsInCarts.setId(productsInCartsKey);
            productsInCarts.setQuantity(1);
            productsInCartsRepository.save(productsInCarts);
        } else {
            productsInCartsDB.setQuantity(productsInCartsDB.getQuantity()+1);
            productsInCartsRepository.save(productsInCartsDB);
        }
    }

    @Transactional
    public void deleteProductCard(Product product, Cart cart) {
        ProductsInCarts productsInCartsDB = productsInCartsRepository.getAllWhere(product.getId(), cart.getId());
        productsInCartsRepository.delete(productsInCartsDB);
    }

    @Transactional
    public void deleteAllProductCard(Cart cart) {
        List<ProductsInCarts> productsInCartsDB = productsInCartsRepository.getAllbyIdCart(cart.getId());

        for(int i=0;i<productsInCartsDB.size();i++)
        {
            productsInCartsRepository.delete(productsInCartsDB.get(i));
        }
    }
}
