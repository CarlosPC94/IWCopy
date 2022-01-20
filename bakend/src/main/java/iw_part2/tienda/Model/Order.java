package iw_part2.tienda.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private Date date;
    private float totalPrice;
    private String deliveryAddress;
    private String deliveryMethod;
    private  String province;
    private  int zip_code;
    private String state;



//    @ManyToMany(mappedBy = "orders", fetch = FetchType.EAGER)
//    Set<Product> products = new HashSet<>();

    //products in orders with quantities
    @JsonManagedReference
    @OneToMany(mappedBy = "order")
    List<ProductsInOrders> productsInOrders;


    public Order() {}

    public Order(User user) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public List<ProductsInOrders> getProductsInOrders() {
        return productsInOrders;
    }

    public void setProductsInOrders(List<ProductsInOrders> productsInOrders) {
        this.productsInOrders = productsInOrders;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getZip_code() {
        return zip_code;
    }

    public void setZip_code(int zip_code) {
        this.zip_code = zip_code;
    }
//    public Set<Product> getProducts() {return products;}
//
//    public void setProducts(Set<Product> products) {this.products = products;}


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Float.compare(order.getTotalPrice(), getTotalPrice()) == 0 && getId().equals(order.getId()) && getUser().equals(order.getUser()) && getDate().equals(order.getDate()) && getDeliveryAddress().equals(order.getDeliveryAddress()) && getDeliveryMethod().equals(order.getDeliveryMethod());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser(), getDate(), getTotalPrice(), getDeliveryAddress(), getDeliveryMethod());
    }
}
