package iw_part2.tienda.Controller;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iw_part2.tienda.Model.Category;
import iw_part2.tienda.Model.Order;
import iw_part2.tienda.Model.Product;
import iw_part2.tienda.Model.User;
import iw_part2.tienda.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/order")
public class OrderController {


    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;

    @Autowired
    ProductsInOrderService productsInOrderService;

    @GetMapping(value = "/all/{id}")
    public List<Order> getAllUserOrders(@PathVariable(value = "id") Long userId){
        List<Order> orders = orderService.getUserOrders(userId);
        return orders;
    }

    @GetMapping(value = "/{id}")
    public Order getPedido(@PathVariable(value = "id") Long id){
        Order order = orderService.findById(id);
        return order;
    }

    @GetMapping(value = "/all")
    public List<Order> getAllOrders(){
        List<Order> orders = orderService.getAllOrders();
        return orders;
    }

    @GetMapping(value = "/state/{state}")
    public List<Order> getAllOrders(@PathVariable(value = "state") String state){
        List<Order> orders = orderService.getOrdersByState(state);
        return orders;
    }

    @PostMapping(value = "/new/{id}")

    public ResponseEntity createOrder(@PathVariable(value = "id") Long idUser,@RequestParam("total_prize") String total_prize, @RequestParam("dirrecion") String direccion, @RequestParam("codigo_postal") String c_postal, @RequestParam("provincia") String provincia, @RequestParam("pedido_estado") String estado)
    {
     User user = userService.findById(idUser);
     Order order = new Order(user);
     order.setUser(user);
     //Fecha actual

     SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
     Calendar calendar = Calendar.getInstance();
     Date fecha = calendar.getTime();
     String formattedDate = dtf.format(fecha);

     order.setDate(fecha);

     order.setState(estado);
     order.setDeliveryAddress(direccion);
     order.setTotalPrice(Float.parseFloat(total_prize));
     order.setDeliveryMethod("Correos");
     order.setProvince(provincia);
     order.setZip_code(Integer.parseInt(c_postal));

     Long orderId= orderService.createOrder(order).getId();
     userService.addOrderUser(idUser,order);
     return ResponseEntity.status(HttpStatus.CREATED).body(order.getId());
    }

    @PutMapping(value = "/{id}/state")
    public ResponseEntity changeState(@PathVariable(value = "id") Long id, @RequestParam("state") String estado){
        try {
            Order order = orderService.changeState(id, estado);
            return ResponseEntity.status(HttpStatus.OK).body(order);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(value = "/add/{id}")
    public ResponseEntity createOrder(@PathVariable(value = "id") Long orderId,@RequestParam("id_product") String id_product, @RequestParam("cantidad") String cantidad)
    {
        Order order = orderService.findById(orderId);
        Product product = productService.getProductById(Long.parseLong(id_product));
        productsInOrderService.addProductToOrder(order,product,Integer.parseInt(cantidad));
        return ResponseEntity.status(HttpStatus.CREATED).body("Order Creado");

    }
}
