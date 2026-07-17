package co.istad.productapisimpledemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_tbl")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String password;
    private String username;
    // must include this
    //private String keycloakUserId;

    @Column(unique = true, nullable = false)
    private String keycloakId;
    @OneToOne(mappedBy ="user", cascade = CascadeType.ALL)
    private Profile profile;
}
