package co.istad.productapisimpledemo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.nio.channels.FileLock;
import java.util.HashSet;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

@Entity(name = "product_tbl")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    // for currency
    private BigDecimal price;// unitPrice
    // validate default value here
    private Boolean isAvailable=true;
    private Boolean isDeleted=false; // soft Delete
    // will create the utilities class in order to generate this
    private String slug; // for seo purpose
    private String thumbnail; // for product image
    private Integer qty;
    @ManyToMany
    @JoinTable(
            name = "product_tags",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    // determined foreign key
    @JoinColumn(name = "category_id")
    private Category category;

}
