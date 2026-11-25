package co.edu.ufps.prestamos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class LibrosClient {

    private final RestClient restClient;

    public LibrosClient(RestClient.Builder builder, @Value("${libros.url}") String librosUrl) {
        this.restClient = builder.baseUrl(librosUrl).build();
    }

    public LibroInfo obtenerLibro(Long libroId) {
        return restClient.get()
                .uri("/libros/{id}", libroId)
                .retrieve()
                .body(LibroInfo.class);
    }
}
