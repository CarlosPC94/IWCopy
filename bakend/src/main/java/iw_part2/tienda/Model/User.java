package iw_part2.tienda.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String email;
    private String name;
    private String password;
    private String deliveryAddress;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Collection<Group> groups;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wishlist_id", referencedColumnName = "id")
    @JsonIgnore
    private WishList wishlist;

    @Temporal(TemporalType.DATE)
    private Date birthdate;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Cart cart;

    @Column(nullable = true, length = 64)
    private String image;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> ordersList;


    @Transient
    private String token;

    public User() {}

    public User(String email) {
        this.email = email;
    }

    public WishList getWishList() {return wishlist;}

    public void setWishList(WishList wishList) {this.wishlist = wishList;}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Order> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<Order> ordersList) {
        this.ordersList = ordersList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return name;
    }

    public void setNombre(String nombre) {
        this.name = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Collection<Group> getGroups() {
        return groups;
    }

    public void setGroups(Collection<Group> group) {
        this.groups = group;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public Cart getCart() {return cart;}

    public void setCart(Cart cart) {this.cart = cart;}

    public String getImage() {return image;}

    public void setImage(String image) {this.image = image;}

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        if (id != null && user.id != null)  //if there is ID NotNull fields are compared
        {
            return getId().equals(user.getId()) &&
                    getDeliveryAddress().equals(user.getDeliveryAddress()) &&
                    getGroups().equals(user.getGroups()) &&
                    getEmail().equals(user.getEmail());
        }   //else constructor fields are compared
        return getEmail().equals(user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getName(), getPassword(), getDeliveryAddress(), getGroups(), getWishList(), getBirthdate(), getCart(), getImage());
    }
}
