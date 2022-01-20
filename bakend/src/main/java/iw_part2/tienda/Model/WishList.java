package iw_part2.tienda.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="wishlist")
public class WishList implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonBackReference
    @OneToOne(mappedBy = "wishlist")
    private User user;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "wishlist_product",
            joinColumns = { @JoinColumn(name = "wishlist_id") },
            inverseJoinColumns = {@JoinColumn(name = "product_id")})
    Set<Product> products = new HashSet<>();

    public WishList() {}

    public WishList(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WishList)) return false;
        WishList wishList = (WishList) o;
        return getId().equals(wishList.getId()) && getUser().equals(wishList.getUser()) && Objects.equals(getProducts(), wishList.getProducts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser(), getProducts());
    }
}
