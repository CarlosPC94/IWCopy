package iw_part2.tienda.Service;

import iw_part2.tienda.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductsInOrderService {

    private ProductInOrdersRepository productsInOrdersRepository;
    private OrderRepository orderRepository;

    @Autowired
    public ProductsInOrderService(ProductInOrdersRepository productsInOrdersRepository,   OrderRepository orderRepository){
        this.productsInOrdersRepository = productsInOrdersRepository;
        this.orderRepository=orderRepository;
    }

    @Transactional
    public void addProductToOrder( Order order,Product product,Integer catidad){

            ProductsInOrdersKey productsInOrdersKey = new ProductsInOrdersKey();
            productsInOrdersKey.setProductId(product.getId());
            productsInOrdersKey.setOrderId((order.getId()));
            ProductsInOrders productsInOrders = new ProductsInOrders();
            productsInOrders.setProduct(product);
            productsInOrders.setOrder(order);
            productsInOrders.setId(productsInOrdersKey);
            productsInOrders.setQuantity(catidad);

            List<ProductsInOrders> productsInOrdersList = order.getProductsInOrders();
            productsInOrdersList.add(productsInOrders);
            order.setProductsInOrders(productsInOrdersList);
            productsInOrdersRepository.save(productsInOrders);
            orderRepository.save(order);

    }

    /*@Transactional
    public void deleteProductCard(Product product, Cart cart) {
        ProductsInCarts productsInCartsDB = productsInCartsRepository.getAllWhere(product.getId(), cart.getId());
        productsInCartsRepository.delete(productsInCartsDB);
    }*/
}
