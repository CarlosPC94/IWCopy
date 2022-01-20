package iw_part2.tienda.Service;

import iw_part2.tienda.Model.User;
import iw_part2.tienda.Model.UserRepository;
import iw_part2.tienda.Model.WishList;
import iw_part2.tienda.Model.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service @RequiredArgsConstructor
public class UserService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final WishListService wishListService;
    private final GroupRepository groupRepository;
    @Autowired
    private  CartService cartService;

    @Autowired
    private  ProductsInCartsService productsInCartsService;
    private final PasswordEncoder passwordEncoder;

    /*
    @Autowired
    public UserService(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.passwordEncoder = passwordEncoder;
    }*/

    public enum LoginStatus {LOGIN_OK, USER_NOT_FOUND, ERROR_PASSWORD}

    @Transactional(readOnly = true)
    public LoginStatus login(String email, String password) {
        Optional<User> usuario = userRepository.findByEmail(email);
        if (!usuario.isPresent()) {
            return LoginStatus.USER_NOT_FOUND;
        } else if (!usuario.get().getPassword().equals(password)) {
            return LoginStatus.ERROR_PASSWORD;
        } else {
            return LoginStatus.LOGIN_OK;
        }
    }

    @Transactional
    public User registrar(User usuario) {
        Optional<User> usuarioBD = userRepository.findByEmail(usuario.getEmail());
        if (usuarioBD.isPresent())
            throw new UserServiceException("El usuario " + usuario.getEmail() + " ya está registrado");
        else if (usuario.getEmail() == null)
            throw new UserServiceException("El usuario no tiene email");
        else if (usuario.getPassword() == null)
            throw new UserServiceException("El usuario no tiene password");
        else {
            WishList wishList = new WishList(usuario);
            wishListService.registrar(wishList);

            Cart cart =  new Cart(usuario);
            usuario.setCart(cart);

            usuario.setWishList(wishList);
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            return userRepository.save(usuario);




        }
    }

    @Transactional
    public User modificar(User user){
        userRepository.save(user);
        return user;
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Transactional(readOnly = true)
    public User findById(Long usuarioId) {
        return userRepository.findById(usuarioId).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    @Transactional
    public void addGroup(String email, String group) {
        Optional<User> optUser = userRepository.findByEmail(email);
        if (optUser.isPresent()) {
            Optional<Group> optGroup = groupRepository.findByName(group);
            if (optGroup.isPresent()) {
                optUser.get().getGroups().add(optGroup.get());
            }
        }
    }

    /*
    Método para que la seguridad de Spring reconozca nuestros usuarios (username es el email) y su rol (user, admin)
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).get();
        if (user == null) {
            logger.error("User " + username + " not found in the database");
            throw new UsernameNotFoundException("User " + username + " not found in the database");
        } else {
            logger.info("User " + username + " found in the database");
        }
        // Pasamos nuestra lista de roles como autoridades
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getGroups().forEach(role -> { // role es el grupo que no se porque aqui los llamamos grupos
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    @Transactional
    public User addOrderUser(Long idUser,Order order)
    {
        User user = findById(idUser);
        List<Order> orderList = user.getOrdersList();
        orderList.add(order);
        user.setOrdersList(orderList);


        Cart cart= cartService.findbyId(user.getCart().getId());
        Set<ProductsInCarts> productsInCartsSet = new HashSet<ProductsInCarts>();

       // Set <ProductsInCarts> productsInCartsSetBefore = <ProductsInCarts> user.getCart().getProductsInCarts();
        cart.setProductsInCarts(productsInCartsSet);
        user.setCart(cart);
        productsInCartsService.deleteAllProductCard(cart);
       /* for(int i=0;i<user.getCart().getProductsInCarts().size();i++)
        {
            productsInCartsService.deleteProductCard(productsInCartsSetBefore.  .getProduct(),user.getCart());
        }*/


        cartService.updateCart(cart);
        userRepository.save(user);

        return user;
    }

    @Transactional
    public boolean deleteUser(Long id){
        if (userRepository.findById(id).isPresent()){
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
