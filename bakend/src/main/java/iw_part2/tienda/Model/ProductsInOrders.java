package iw_part2.tienda.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "productsInOrders")
public class ProductsInOrders implements Serializable {

    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ProductsInOrdersKey id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    Product product;

    @JsonBackReference
    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    Order order;

    int quantity;

    public ProductsInOrders(ProductsInOrdersKey id) {
        this.id = id;
    }

    public ProductsInOrders() {

    }

    public ProductsInOrdersKey getId() {
        return id;
    }

    public void setId(ProductsInOrdersKey id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductsInOrders that = (ProductsInOrders) o;
        return quantity == that.quantity && id.equals(that.id) && product.equals(that.product) && order.equals(that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, order, quantity);
    }
}
