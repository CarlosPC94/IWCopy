package iw_part2.tienda.Controller;

import iw_part2.tienda.Model.Category;
import iw_part2.tienda.Model.SubCategory;
import iw_part2.tienda.Model.User;
import iw_part2.tienda.Service.CategoryService;
import iw_part2.tienda.Service.SubCategoryService;
import iw_part2.tienda.Service.SubCategoryServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    SubCategoryService subCategoryService;

    @GetMapping(value = "/categories")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.allCategories();
        if (categories != null)
            return new ResponseEntity<>(categories, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @PostMapping(value = "/categories")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category newCategory = categoryService.newCategory(category.getName());
        if (newCategory != null)
            return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
        else
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping(value = "/categories/{id}")
    public ResponseEntity<List<Category>> deleteCategories(@PathVariable(value="id") Long idCategoria) {
        try {
            categoryService.deleteCategory(idCategoria);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/subcategories")
    public ResponseEntity<List<SubCategory>> getSubcategories() {
        List<SubCategory> subcategories = subCategoryService.allSubCategories();
        if (subcategories != null)
            return new ResponseEntity<>(subcategories, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @PostMapping(value = "/categories/{id}/subcategories")
    public ResponseEntity<SubCategory> createSubategory(@PathVariable(value="id") Long idCategoria,
                                                     @RequestBody SubCategory subcategory) {
        Category categoria = categoryService.findById(idCategoria);
        if (categoria == null) {
            throw new SubCategoryServiceException("No existe la categoria indicada con id" + idCategoria);
        }
        SubCategory newSubcategory = subCategoryService.newSubCategory(subcategory.getName(), categoria);
        if (newSubcategory != null)
            return new ResponseEntity<>(newSubcategory, HttpStatus.CREATED);
        else
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping(value = "/subcategories/{id}")
    public ResponseEntity<List<SubCategory>> deleteSubcategories(@PathVariable(value="id") Long idSubcategoria) {
        try {
            subCategoryService.deleteSubCategory(idSubcategoria);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
