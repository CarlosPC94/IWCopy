package iw_part2.tienda.Service;

import iw_part2.tienda.Model.Category;
import iw_part2.tienda.Model.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Category> allCategories() {
        List<Category> categories = new ArrayList<Category>();
        categoryRepository.findAll().forEach(categoria -> {
            categories.add(categoria);
        });
        return categories;
    }

    @Transactional
    public Category newCategory(String categoryName) {
        Category categoria = new Category(categoryName);
        categoryRepository.save(categoria);
        return categoria;
    }

    @Transactional
    public void deleteCategory(Long idCategory) {
        Category category = categoryRepository.findById(idCategory).orElse(null);
        if (category == null) {
            throw new CategoryServiceException("No existe category con id " + idCategory);
        }
        categoryRepository.delete(category);
    }

    @Transactional
    public Category modifyCategory(Long idCategory, String categoryName) {
        Category category = categoryRepository.findById(idCategory).orElse(null);
        if (category == null) {
            throw new CategoryServiceException("No existe category con id " + idCategory);
        }
        if (categoryName!=null)
        {
            category.setName(categoryName);
        }
        categoryRepository.save(category);
        return category;
    }


}
