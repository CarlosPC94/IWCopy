package iw_part2.tienda.Service;

import iw_part2.tienda.Model.Category;
import iw_part2.tienda.Model.SubCategory;
import iw_part2.tienda.Model.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubCategoryService {

    private SubCategoryRepository subCategoryRepository;

    @Autowired
    public SubCategoryService(SubCategoryRepository subCategoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
    }

    @Transactional(readOnly = true)
    public SubCategory findById(Long subCategoryId) {
        return subCategoryRepository.findById(subCategoryId).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<SubCategory> allSubCategories() {
        List<SubCategory> subCategories = new ArrayList<>();
        subCategoryRepository.findAll().forEach(subCategories::add);
        return subCategories;
    }

    @Transactional
    public SubCategory newSubCategory(String subCategoryName, Category category) {
        SubCategory subCategory = new SubCategory(category ,subCategoryName);
        subCategoryRepository.save(subCategory);
        return subCategory;
    }

    @Transactional
    public void deleteSubCategory(Long idSubCategory) {
        SubCategory subCategory = subCategoryRepository.findById(idSubCategory).orElse(null);
        if (subCategory == null) {
            throw new SubCategoryServiceException("No existe SubCategory con id " + idSubCategory);
        }
        subCategoryRepository.delete(subCategory);
    }

    @Transactional
    public SubCategory modifySubCategory(Long idSubCategory, String subCategoryName) {
        SubCategory subCategory = subCategoryRepository.findById(idSubCategory).orElse(null);
        if (subCategory == null) {
            throw new SubCategoryServiceException("No existe category con id " + idSubCategory);
        }
        if (subCategoryName!=null)
        {
            subCategory.setName(subCategoryName);
        }
        subCategoryRepository.save(subCategory);
        return subCategory;
    }


}
