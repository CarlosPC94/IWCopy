package iw_part2.tienda.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "product")
public class Product  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private  String name;
    @Column(columnDefinition = "double default 0")
    private double price;
    @Column(columnDefinition = "double default 0")
    private double discount;
    @Column(columnDefinition = "integer default 0")
    private int available_quantity;
    private String link;
    private String short_desc;
    private String long_desc;

    @ManyToOne
    // Nombre de la columna en la BD que guarda físicamente
    // el ID del usuario con el que está asociado una tarea
    @JoinColumn(name = "category")
    private  Category category;

    @ManyToOne
    @JoinColumn(name = "subcategory")
    private  SubCategory subcategory;

    @ManyToMany(mappedBy = "products", fetch = FetchType.EAGER)
    @JsonIgnore
    List <WishList>  wishlists = new ArrayList<WishList>();

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "ProductsInCarts",
//            joinColumns = { @JoinColumn(name = "product_id") },
//            inverseJoinColumns = {@JoinColumn(name = "cart_id")})
//    Set<Cart> carts = new HashSet<>();

    //product_ids && cart_id-s & quantities
    @OneToMany(mappedBy = "product")
    Set<ProductsInCarts> productsInCarts;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "ProductsInOrders",
//            joinColumns = { @JoinColumn(name = "product_id") },
//            inverseJoinColumns = {@JoinColumn(name = "order_id")})
//    Set<Order> orders = new HashSet<>();


    //product_ids && order_id-s & quantities
    @OneToMany(mappedBy = "product")
    Set<ProductsInOrders> productsInOrders;

    @OneToMany(mappedBy = "product")
    private Set<ProductImage> images;


    private Product(){}

    public Product(String name, Category category)
    {
        this.name = name;
        this.category = category;
        category.products.add(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getAvailable_quantity() {
        return available_quantity;
    }

    public void setAvailable_quantity(int available_quantity) {
        this.available_quantity = available_quantity;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

    public String getLong_desc() {
        return long_desc;
    }

    public void setLong_desc(String long_desc) {
        this.long_desc = long_desc;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public SubCategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(SubCategory subcategory) {
        this.subcategory = subcategory;
    }

    public List<WishList> getWishlists() {
        return wishlists;
    }

    public void setWishlists(List <WishList> wishlists) {
        this.wishlists = wishlists;
    }

    public Set<ProductImage> getImages() { return images; }

    public void setImages(Set<ProductImage> images) { this.images = images; }

    public void addImage(ProductImage image) { this.images.add(image); }

    public void removeImage(ProductImage image) { this.images.remove(image); }

//    public Set<Cart> getCarts() {return carts;}
//
//    public void setCarts(Set<Cart> carts) {this.carts = carts;}

//    public Set<Order> getOrders() {return orders;}
//
//    public void setOrders(Set<Order> orders) {this.orders = orders;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        if (id != null && product.id != null)  //if there is ID NotNull fields are compared
        {
            return getId().equals(product.getId()) &&
                    getName().equals(product.getName());
        }   //else constructor fields are compared
        return getName().equals(product.getName()) &&
                getCategory().equals((product.getCategory()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPrice(), getDiscount(), getAvailable_quantity(), getLink(), getShort_desc(), getLong_desc(), getCategory(), getWishlists());
    }
}


