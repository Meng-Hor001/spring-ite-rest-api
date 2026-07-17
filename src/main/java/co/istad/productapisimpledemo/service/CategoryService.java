package co.istad.productapisimpledemo.service;

import co.istad.productapisimpledemo.dto.CategoryRequest;
import co.istad.productapisimpledemo.dto.CategoryResponse;
import co.istad.productapisimpledemo.entity.Category;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(CategoryRequest request);
    void deleteCategory(Integer id);
    // get all with Pagination ( follow products sample )
    // soft delete category ( changing the value of isDeleted )

    List<CategoryResponse> findAll();
    CategoryResponse findById(Integer id);
    List<CategoryResponse> findByName(String name);


    // asc , desc
    List<CategoryResponse> findParentCategories(String sortDirection );

}
