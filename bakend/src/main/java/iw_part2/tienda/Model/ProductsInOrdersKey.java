package iw_part2.tienda.Model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductsInOrdersKey implements Serializable {

    @Column(name = "product_id")
    Long productId;

    @Column(name = "order_id")
    Long orderId;

    public ProductsInOrdersKey(Long productId, Long orderId) {
        this.productId = productId;
        this.orderId = orderId;
    }

    public ProductsInOrdersKey() {

    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductsInOrdersKey that = (ProductsInOrdersKey) o;
        return productId.equals(that.productId) && orderId.equals(that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, orderId);
    }
}
