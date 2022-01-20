package iw_part2.tienda.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @OneToMany
    @JsonIgnore
    Set<Product> products = new HashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    Set<SubCategory> subcategories = new HashSet<>();

    public Category() {}

    public Category(String name) {

        this.name = name;
        this.subcategories = new HashSet<>();
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

    public Set<SubCategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(Set<SubCategory> subcategories) {
        this.subcategories = subcategories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        if (id != null && category.id != null)
        {
            return id.equals(category.id) && name.equals(category.name);

        }
        return name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
