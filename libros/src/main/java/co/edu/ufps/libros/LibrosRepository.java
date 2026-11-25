package co.edu.ufps.libros;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LibrosRepository extends JpaRepository<Libro, Long> {
    
}
