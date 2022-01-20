package iw_part2.tienda.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ProductImage")
public class ProductImage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "product")
    private Product product;

    @Column(nullable = true, length = 64)
    private String image;

    public ProductImage() {}

    public ProductImage(String image, Product product)
    {
        this.image=image;
        this.product=product;
        product.addImage(this);
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public Product getProduct() {return product;}

    public void setProduct(Product product) {this.product = product;}

    public String getImage() {return image;}

    public void setImage(String image) {this.image = image;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductImage)) return false;
        ProductImage that = (ProductImage) o;
        return getId().equals(that.getId()) && getProduct().equals(that.getProduct()) && getImage().equals(that.getImage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getProduct(), getImage());
    }
}
