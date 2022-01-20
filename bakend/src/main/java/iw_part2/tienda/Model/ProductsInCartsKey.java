package iw_part2.tienda.Model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductsInCartsKey implements Serializable {

    @Column(name = "product_id")
    Long productId;

    @Column(name = "cart_id")
    Long cartId;

    public ProductsInCartsKey(Long productId, Long cartId) {
        this.productId = productId;
        this.cartId = cartId;
    }

    public ProductsInCartsKey() {

    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long orderId) {
        this.cartId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductsInCartsKey that = (ProductsInCartsKey) o;
        return productId.equals(that.productId) && cartId.equals(that.cartId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, cartId);
    }
}
