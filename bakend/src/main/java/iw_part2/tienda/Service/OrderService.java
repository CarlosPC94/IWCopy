package iw_part2.tienda.Service;


import iw_part2.tienda.Model.Category;
import iw_part2.tienda.Model.Order;
import iw_part2.tienda.Model.OrderRepository;
import iw_part2.tienda.Model.ProductsInOrders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Transactional
    public Order createOrder(Order new_order){
        Order order = new_order;
        List<ProductsInOrders> productsInOrders = new ArrayList<ProductsInOrders>();
        order.setProductsInOrders(productsInOrders);
        orderRepository.save(order);
        return order;
    }
    @Transactional(readOnly = true)
    public  Order findById(Long idOrder)
    {
        return  orderRepository.findById(idOrder).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Order> getUserOrders(Long id){
        List<Order> orders = orderRepository.findAllByuser_id(id);
        return orders;
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders(){
        List<Order> orders = new ArrayList<Order>();
        orderRepository.findAll().forEach(order -> {
            orders.add(order);
        });
        return orders;
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByState(String state){
        List<Order> orders = new ArrayList<Order>();
        orderRepository.findAll().forEach(order -> {
            if(order.getState().equals(state)){
                orders.add(order);
            }
        });
        return orders;
    }

    @Transactional
    public Order changeState(Long id, String state){
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            throw new RuntimeException("No hay pedido con id " + id);
        }
        order.setState(state);
        return order;
    }
}
