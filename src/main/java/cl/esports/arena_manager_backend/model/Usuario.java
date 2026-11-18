package cl.esports.arena_manager_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 30)
    private String apodo;

    @Column(nullable = false, unique = true, length = 120)
    private String correo;

    @Column(nullable = false)
    private Integer edad;

    @JsonIgnore
    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 30)
    private String rol;

    @Column(nullable = false)
    private Integer victorias;

    @Column(nullable = false)
    private Integer derrotas;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    @Builder.Default
    private List<Sancion> sanciones = new ArrayList<>();

    @Transient
    public Map<String, Integer> getEstadisticas() {
        return Map.of(
                "victorias", victorias != null ? victorias : 0,
                "derrotas", derrotas != null ? derrotas : 0
        );
    }

    @Transient
    public List<Object> getHistorial() {
        return List.of();
    }
}