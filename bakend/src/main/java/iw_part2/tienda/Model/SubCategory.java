package iw_part2.tienda.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "subcategories")
public class SubCategory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @OneToMany
    Set<Product> products = new HashSet<>();

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "category_id")
    private  Category category;

    public SubCategory() {}

    public SubCategory(Category category, String name) {
    this.category = category;
    this.name = name;
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

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubCategory subcategory = (SubCategory) o;
        return id.equals(subcategory.id) && name.equals(subcategory.name) && category.equals(subcategory.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
