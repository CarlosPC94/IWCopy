package iw_part2.tienda.Controller;

import iw_part2.tienda.Model.*;
import iw_part2.tienda.Service.CategoryService;
import iw_part2.tienda.Service.ProductService;
import iw_part2.tienda.Service.SubCategoryService;
import iw_part2.tienda.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/products/")
public class ProductRestController {
    @Autowired
    private ProductService productService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    SubCategoryService subcategoryService;

    @GetMapping(value = "/all")
    public List<Product> getAll() {
        return productService.getProducts();
    }

    @GetMapping(value = "/all/{id}")
    public List<Product> getAllByCategory(@PathVariable(value = "id") Long idCategory) {
        return productService.getAllProductsByCategory(idCategory);
    }

    @GetMapping(value = "/all/subcategory/{id}")
    public List<Product> getAllBySubategory(@PathVariable(value = "id") Long idSubcategory) {
        return productService.getAllProductsBySubategory(idSubcategory);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable(value = "id") Long idProduct) {
        Product product = productService.getProductById(idProduct);

        if (product == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(product);


    }

    @PostMapping(value = "/create")
    public ResponseEntity save(@RequestParam("name") String name, @RequestParam("price") Double price,@RequestParam("long_desc") String long_desc,@RequestParam("short_desc") String short_desc,@RequestParam("discount") Double discount,@RequestParam("id_category") String idC,@RequestParam("cantidad") String cantidad,@RequestParam("file") MultipartFile multipartFile) {
        try {
           Category category= categoryService.findById(Long.parseLong(idC));
            Product product = new Product(name,category);

            product.setShort_desc(short_desc);
            product.setLong_desc(long_desc);
            product.setPrice(price);

            product.setDiscount(discount);
            product.setAvailable_quantity(Integer.parseInt(cantidad));


            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String uploadDir = "./user-images";
            Path uploadPath = Paths.get(uploadDir);

            if(!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            if(!fileName.equals(""))
            {
                product.setLink(fileName);
               productService.createProduct(product);
                try (InputStream inputStream = multipartFile.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new IOException("Could not save the uploaded file: " + fileName);
                }
            } else {
               product.setLink("img.png");
                productService.createProduct(product);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado correctamente");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(exception);
        }
    }

    @PostMapping(value = "/micreate")
    public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
        try {
            Product newProduct = productService.createProduct(product);
            return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
        }
    }

    @PostMapping(value = "/{id}/image")
    public ResponseEntity saveImage(@PathVariable(value="id") Long idProducto,
                                             @RequestParam("file") MultipartFile multipartFile) {
        Product product = productService.getProductById(idProducto);
        if (product == null) {
            return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
        } else {
            try {
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                String uploadDir = "./user-images";
                Path uploadPath = Paths.get(uploadDir);

                if(!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                if(!fileName.equals(""))
                {
                    productService.changeImageLink(product.getId(), fileName);
                    try (InputStream inputStream = multipartFile.getInputStream()) {
                        Path filePath = uploadPath.resolve(fileName);
                        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        throw new IOException("Could not save the uploaded file: " + fileName);
                    }
                } else {
                    productService.changeImageLink(product.getId(), fileName);
                }
                return ResponseEntity.status(HttpStatus.CREATED).body("Imagen guardada");
            } catch (Exception exception) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Algo fue mal");
            }
        }
    }

    /*@PostMapping(value = "/create")
    public ResponseEntity<Product> create(@RequestBody Product product) {
        Product product1_old = productService.createProduct(product);

        if (product1_old != null)
            return new ResponseEntity<Product>(product1_old, HttpStatus.CREATED);
        else
            return new ResponseEntity<Product>(product1_old, HttpStatus.INTERNAL_SERVER_ERROR);

    }*/

    @PutMapping("/{id}")
    public ResponseEntity actualizar(@RequestParam("name") String name, @RequestParam("price") Double price,
                                     @RequestParam("long_desc") String long_desc,@RequestParam("short_desc") String short_desc,
                                     @RequestParam("discount") Double discount,@RequestParam("id_category") String idC,
                                     @RequestParam("id_subcategory") String idS, @RequestParam("cantidad") String cantidad,
                                     @RequestParam(name = "file", required = false) MultipartFile multipartFile,
                                     @PathVariable Long id) {

        try {
            Category category= categoryService.findById(Long.parseLong(idC));
            SubCategory subcategory= subcategoryService.findById(Long.parseLong(idS));
            Product updated_product = null;


            String fileName = null;

            if (multipartFile != null){
                fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            } else {
                Product producto = productService.getProductById(id);
                fileName = producto.getLink();
                updated_product = productService.update(id, name, price, fileName, long_desc, short_desc, discount, category, subcategory, cantidad);
                return ResponseEntity.ok(updated_product);
            }

            String uploadDir = "./user-images";
            Path uploadPath = Paths.get(uploadDir);

            if(!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            if(!fileName.equals(""))
            {
                updated_product = productService.update(id, name, price, fileName, long_desc, short_desc, discount, category, subcategory, cantidad);
                if(updated_product!=null) {
                    try (InputStream inputStream = multipartFile.getInputStream()) {
                        Path filePath = uploadPath.resolve(fileName);
                        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        throw new IOException("Could not save the uploaded file: " + fileName);
                    }

                }
                else
                    return ResponseEntity.notFound().build();

            } else {
                updated_product = productService.update(id, name, price, "img.png", long_desc, short_desc, discount, category, subcategory, cantidad);
            }
          return ResponseEntity.ok(updated_product);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(exception);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable(value = "id") Long idProduct) {
        Product product = productService.getProductById(idProduct);
        if (product == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        try {
            productService.deleteProductById(idProduct);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
