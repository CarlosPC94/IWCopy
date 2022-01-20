package iw_part2.tienda.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "productsInCarts")
public class ProductsInCarts implements Serializable {

    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ProductsInCartsKey id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne
    @MapsId("cartId")
    @JoinColumn(name = "cart_id")
    Cart cart;

    int quantity;

    public ProductsInCarts(ProductsInCartsKey id) {
        this.id = id;
    }

    public ProductsInCarts() {

    }

    public ProductsInCartsKey getId() {
        return id;
    }

    public void setId(ProductsInCartsKey id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
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
        ProductsInCarts that = (ProductsInCarts) o;
        return quantity == that.quantity && id.equals(that.id) && product.equals(that.product) && cart.equals(that.cart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, cart, quantity);
    }
}
