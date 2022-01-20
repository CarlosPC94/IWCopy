package iw_part2.tienda.Service;

import iw_part2.tienda.Model.Category;
import iw_part2.tienda.Model.Product;
import iw_part2.tienda.Model.ProductRepository;
import iw_part2.tienda.Model.SubCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private ProductRepository productRepository;
    @Autowired
    public ProductService( ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Transactional(readOnly = true)
    public List<Product> getProducts() {
        return productRepository.getProducts();
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProductsByCategory(Long num) {

        return productRepository.getAllByCategory(num);
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProductsBySubategory(Long num) {

        return productRepository.getAllBySubcategory(num);
    }

    @Transactional
    public  Product createProduct(Product new_producto)
    {
        Product product = new_producto;
        productRepository.save(product);
        return product;

    }
    @Transactional
    public  Product getProductById(Long id)
    {
       return  productRepository.findById(id).orElse(null);
    }

    @Transactional
    public  boolean deleteProductById(Long id)
    {
        Product product = productRepository.findById(id).orElse(null);
        if(product==null)
            return false;

        productRepository.deleteById(id);
        return  true;

    }

    public Product updateProductId(Product product) {
        productRepository.save(product);
        return product;
    }

    @Transactional
    public void changeImageLink(Long id, String imageLink)
    {
        Product product = productRepository.findById(id).orElse(null);
        if(product==null)
            return;

        product.setLink(imageLink);
        return;
    }

    @Transactional
    public Product update(Long id, String name, Double price, String fileName,
                          String long_desc, String short_desc, Double discount,
                          Category category, SubCategory subcategory, String cantidad)
    {
        Product product = productRepository.findById(id).orElse(null);
        if(product==null)
             return null;

        product.setName(name);
        product.setPrice(price);
        product.setLink(fileName);
        product.setLong_desc(long_desc);
        product.setShort_desc(short_desc);
        product.setDiscount(discount);
        product.setCategory(category);
        product.setSubcategory(subcategory);
        product.setAvailable_quantity(Integer.parseInt(cantidad));

        return product;
    }
}
