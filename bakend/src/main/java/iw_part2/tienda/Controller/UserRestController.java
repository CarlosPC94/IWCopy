package iw_part2.tienda.Controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import iw_part2.tienda.Model.Cart;
import iw_part2.tienda.Model.Product;
import iw_part2.tienda.Model.ProductsInCarts;
import iw_part2.tienda.Model.User;
import iw_part2.tienda.Service.CartService;
import iw_part2.tienda.Service.ProductService;
import iw_part2.tienda.Service.ProductsInCartsService;
import iw_part2.tienda.Service.UserService;
import iw_part2.tienda.authentication.TokenUtils;
import iw_part2.tienda.utils.HttpResponseBuilder;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/users/")
public class UserRestController {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ProductsInCartsService productsInCartsService;

    @GetMapping(value = "/image/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable(value = "name") String name) throws IOException {
        String imageName = "/user-image/" + name;
        InputStream in = getClass().getResourceAsStream("/user-images/" + name);
        return in.readAllBytes();
    }

    @GetMapping(value = "/all")
    public List<User> getAll() {
        List<User> users =userService.getUsers();
        return userService.getUsers();
    }

    @GetMapping(value = "/profile")
    public void getLoggedUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String email = TokenUtils.getEmailFromToken(request);
            User user = userService.findByEmail(email);

            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.putObject("user")
                    .put("id", user.getId())
                    .put("email", user.getEmail())
                    .put("name", user.getNombre())
                    .put("address", user.getDeliveryAddress());

            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), objectNode);
        } catch (Exception ex) {
            // Esta excepción NO es para comprobar si hay token o si es válido
            // eso ya lo comprueba el filtro automáticamente
            // es para otros errores, como que el token sea válido pero el usuario haya
            // sido borrado, o que salte una excepción al escribir la respuesta
            logger.error("Error logging in: {}", ex.getMessage());
            HttpResponseBuilder.writeErrorResponse(response, ex.getMessage(), FORBIDDEN);
        }
    }

    @GetMapping(value = "/perfil/{id}")
    public User getProfileUser(@PathVariable(value = "id") Long id){
        User user = userService.findById(id);
        if (user != null)
            return user;
        else
            throw new RuntimeException("Usuario no encontrado");
    }

    @PostMapping(value="cart/{id}/{id_prod}")
    public Set<ProductsInCarts> addProductToCart(@PathVariable(value = "id") Long idUser,@PathVariable(value = "id_prod") Long id_prod)
    {
        User user =userService.findById(idUser);
        Product product = productService.getProductById(id_prod);
        if (user.getCart() == null){
            cartService.createCart(user);
        }
        Cart cart = user.getCart();
        productsInCartsService.addProductToCart(product, cart);
        User userBD =userService.findById(idUser);
        return  userBD.getCart().getProductsInCarts();
    }

    @DeleteMapping(value="cart/delete/{id}/{id_prod}")
    public Set<ProductsInCarts> deleteProductToCart(@PathVariable(value = "id") Long idUser,@PathVariable(value = "id_prod") Long id_prod)
    {
        User user =userService.findById(idUser);
        Product product = productService.getProductById(id_prod);
        Cart cart = user.getCart();
        productsInCartsService.deleteProductCard(product, cart);
        return  cart.getProductsInCarts();
    }



    @PostMapping(value = "/perfil")
    public User updateProfileUser(@RequestParam("userId") Long userId,@RequestParam("userEmail") String userEmail, @RequestParam("userPassword") String userPassword,
                                  @RequestParam("userAddress") String userAddress, @RequestParam(name = "file", required = false) MultipartFile multipartFile) throws IOException {
        User usuario = userService.findById(userId);
        if (usuario != null){
            usuario.setPassword(userPassword);
            usuario.setEmail(userEmail);
            usuario.setDeliveryAddress(userAddress);
            if(multipartFile == null){
                return userService.modificar(usuario);
            }
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String uploadDir = "./user-images";
            Path uploadPath = Paths.get(uploadDir);


            if(!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            if(!fileName.equals("")) {
                usuario.setImage(fileName);
                try (InputStream inputStream = multipartFile.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                    return userService.modificar(usuario);
                } catch (IOException e) {
                    throw new IOException("Could not save the uploaded file: " + fileName);
                }
            } else {
                return userService.modificar(usuario);
            }

        } else {
            throw new RuntimeException("El usuario no existe");
        }
    }

    @GetMapping(value = "/cart/{id}")
    public Set<ProductsInCarts> UserCart(@PathVariable(value = "id") Long id_user ){
        User user = userService.findById(id_user);

        if(user!=null){
            return user.getCart().getProductsInCarts();
        }
        else
            throw new RuntimeException("El usuario no existe");

    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable(value = "id") Long id){
        userService.deleteUser(id);
    }


}
