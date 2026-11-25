package co.edu.ufps.prestamos;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/prestamos")
public class PrestamoController {

    private final PrestamoRepository prestamoRepository;
    private final LibrosClient librosClient;

    public PrestamoController(PrestamoRepository prestamoRepository, LibrosClient librosClient) {
        this.prestamoRepository = prestamoRepository;
        this.librosClient = librosClient;
    }

    @PostMapping
    public ResponseEntity<Prestamo> crearPrestamo(@Valid @RequestBody PrestamoRequest request) {
        LibroInfo libro;
        try {
            libro = librosClient.obtenerLibro(request.libroId());
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El libro " + request.libroId() + " no existe",
                    ex);
        } catch (RestClientException ex) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "El servicio de libros no responde",
                    ex);
        }

        if (libro == null || libro.ejemplaresDisponibles() == null) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "El servicio de libros no responde");
        }

        if (libro.ejemplaresDisponibles() <= 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No hay ejemplares disponibles");
        }

        Prestamo prestamo = new Prestamo();
        prestamo.setLibroId(request.libroId());
        prestamo.setTituloLibro(libro.titulo());
        prestamo.setCodigoEstudiante(request.codigoEstudiante());
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamoRepository.save(prestamo));
    }

    @GetMapping
    public List<Prestamo> listarPrestamos() {
        return prestamoRepository.findAll();
    }
}
