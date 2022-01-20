package iw_part2.tienda.Service;

import iw_part2.tienda.Model.Cart;
import iw_part2.tienda.Model.CartRepository;
import iw_part2.tienda.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Transactional
    public Cart findbyId(Long id){
      return cartRepository.findById(id).orElse(null);
    }
    @Transactional
    public Cart createCart(User user){
       Cart cart = new Cart();
       cart.setUser(user);
       user.setCart(cart);
       cartRepository.save(cart);
       return cart;
    }

    @Transactional
    public Cart updateCart(Cart cart){
        cartRepository.save(cart);
        return cart;
    }


}
