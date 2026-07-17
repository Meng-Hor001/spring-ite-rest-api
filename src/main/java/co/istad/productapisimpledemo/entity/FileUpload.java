package co.istad.productapisimpledemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "files_tbl")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String caption;
    @Column(nullable = false)
    private Long size; // bytes
    @Column(nullable = false, length = 15)
    private String extension;
    @Column(nullable = false)// png
    private String mediaType; // file, images, documents
}
