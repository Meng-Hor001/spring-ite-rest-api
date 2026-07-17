package co.istad.productapisimpledemo.service.impl;
import co.istad.productapisimpledemo.dto.ProductFilter;
import co.istad.productapisimpledemo.dto.product.ProductRequest;
import co.istad.productapisimpledemo.dto.product.ProductResponse;
import co.istad.productapisimpledemo.dto.product.UpdateProductRequest;
import co.istad.productapisimpledemo.entity.Product;
import co.istad.productapisimpledemo.entity.ProductSpecification;
import co.istad.productapisimpledemo.entity.Tag;
import co.istad.productapisimpledemo.mapper.ProductMapper;
import co.istad.productapisimpledemo.repository.CategoryRepository;
import co.istad.productapisimpledemo.repository.ProductRepository;
import co.istad.productapisimpledemo.repository.TagRepository;
import co.istad.productapisimpledemo.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    // inject the repository here
    //private final ProductRepositoryOld productRepositoryOld;
   private final ProductRepository productRepository;
   private final CategoryRepository categoryRepository;
   private final TagRepository tagRepository;

   private final ProductMapper productMapper;

    @Override
    public List<ProductResponse> findAllProducts() {
        // repository.findAll()
        return productRepository.findAll()
                .stream()
                .map(productMapper::mapToResponse)
                .toList();
    }

    @Override
    public Page<ProductResponse> findAllProducts(Pageable pageable, ProductFilter filter ) {
        Specification<Product> spec = ProductSpecification.filterProduct(filter);
        return productRepository.findAll(spec, pageable)
               .map(productMapper::mapToResponse);
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        // create entity product from the request
        var product = productMapper.mapToProduct(request);

        // setAvailability to True
        product.setIsAvailable(true);

        // check if the category exists
        var category = categoryRepository.findById(request.categoryId()).orElseThrow(
                ()-> new NoSuchElementException("Category with id = "+request.categoryId()+ " not found! ")
        );
        product.setCategory(category);
        // convert Set<Long> to Set<Tag>
        // getReferenceById vs findById
        if(request.tagIds() != null &&  !request.tagIds().isEmpty()) {
            Set<Tag> tags = request.tagIds().stream()
                    .map(tagId -> tagRepository.findById(tagId).orElseThrow(
                            ()-> new NoSuchElementException("Tag with id = "+tagId+ " not found! ")
                    ))
                    .collect(Collectors.toSet());
            product.setTags(tags);
        }
      // insert the data to the table only need to
        // repository.save(entity) = insert
        return productMapper.mapToResponse(productRepository.save(product));

    }


    @Override
    public ProductResponse findProductById(Integer id) {
       var product =  productRepository.findById(id)
               .orElseThrow(()-> new NoSuchElementException("Product with ID = "+id+" not found"));

        return productMapper.mapToResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Integer id , UpdateProductRequest request) {
        // find existing product
        // repository.findById
        var existingProduct = productRepository.findById(id).orElseThrow(()-> new NoSuchElementException("Product with ID = "+id+" not found"));

        if(request.name()!=null)
            existingProduct.setName(request.name());
        if(request.description()!=null)
            existingProduct.setDescription(request.description());
        if(request.price()!=null)
            existingProduct.setPrice(request.price());
        // update product
        productRepository.save(existingProduct);
        return productMapper.mapToResponse(existingProduct);
    }



    // TODO: make it like we delete in the category
    @Override
    public boolean deleteProduct(Integer id) {
        // find if the product exist
        // if it's we delete it and return true
        // else return false

        if(productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }


}
