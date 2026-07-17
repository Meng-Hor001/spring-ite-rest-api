package co.istad.productapisimpledemo.repository;

import co.istad.productapisimpledemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByKeycloakId(String keycloakId);

    String keycloakId(String keycloakId);
}
