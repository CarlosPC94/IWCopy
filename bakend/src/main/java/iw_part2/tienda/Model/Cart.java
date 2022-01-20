package iw_part2.tienda.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "FK_User", updatable = false, nullable = false)
    private User user;

//    @ManyToMany(mappedBy = "carts", fetch = FetchType.EAGER)
//    Set<Product> products = new HashSet<>();

    //products in carts with quantities
    @OneToMany(mappedBy = "cart")
    @JsonIgnore
    Set<ProductsInCarts> productsInCarts;

    public Cart() {};
    public Cart(User user) {this.user=user;}
    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}

    public Set<ProductsInCarts> getProductsInCarts() {
        return productsInCarts;
    }

    public void setProductsInCarts(Set<ProductsInCarts> productsInCarts) {
        this.productsInCarts = productsInCarts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cart)) return false;
        Cart cart = (Cart) o;
        return getId().equals(cart.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
