package co.istad.productapisimpledemo.entity;

import co.istad.productapisimpledemo.dto.ProductFilter;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

// static method -> define condition , criteria for filter
public class ProductSpecification {
    public static Specification<Product> filterProduct(ProductFilter filter) {
        return  (root, query, criteriaBuilder) -> {
            // list condition
            List<Predicate> predicates = new ArrayList<>();

            // for specific filter , we add one record of the predicate
            // 1. filter by product that contains the name
            // % % -> containing
            if (filter.getName() != null && !filter.getName().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + filter.getName().toLowerCase() + "%"
                )
                );
            }
            // 2. minPrice minPrice = 1000
            if (filter.getMinPrice() != null) {
                // min -> greaterThanOrEqualTo
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
            }

            // 3. maxPirce
            if (filter.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
            }

            //  4. category
            // Many-to-One
            if (filter.getCategoryId() != null) {
                // product -> category
                // find product by categoryId
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), filter.getCategoryId()));
            }

            // ManyToMay using join
            if (filter.getTagNames() != null && !filter.getTagNames().isEmpty()) {

                // inner join
                Join<Product, Tag> tagsJoin = root.join("tags");
                // tagsJoin.get("name")
                predicates.add(tagsJoin.get("name").in(filter.getTagNames()));
                // Ensure distinct results if a product has multiple matching tags
              query.distinct(true);
            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

    }
}
