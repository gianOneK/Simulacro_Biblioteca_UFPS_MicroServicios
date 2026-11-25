package co.edu.ufps.libros;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/libros")
public class LibrosController {
    
    private final LibrosRepository libroRepository;

    public LibrosController(LibrosRepository libroRepository) {
        this.libroRepository = libroRepository;
    }


    // Listar todos los libros
    @GetMapping
    public List<Libro> listarLibros() {
        return libroRepository.findAll();
    }

    //Listar por ID
    @GetMapping("/{id}")
    public Libro obtenerLibro(@PathVariable Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));
    }

    
    // Crear un nuevo libro
    @PostMapping
    public Libro crearLibro(@Valid @RequestBody Libro libro) {
        return libroRepository.save(libro);
    }

     
    // Actualizar un libro existente
    @PutMapping("/{id}")
    public Libro actualizarLibro(@PathVariable Long id, @Valid @RequestBody Libro libroActualizado) {
        return libroRepository.findById(id)
                .map(libro -> {
                    libro.setTitulo(libroActualizado.getTitulo());
                    libro.setAutor(libroActualizado.getAutor());
                    libro.setEjemplaresDisponibles(libroActualizado.getEjemplaresDisponibles());
                    return libroRepository.save(libro);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));
    }

    // Eliminar un libro
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarLibro(@PathVariable Long id) {
        if (!libroRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado");
        }
        libroRepository.deleteById(id);
    }
}
