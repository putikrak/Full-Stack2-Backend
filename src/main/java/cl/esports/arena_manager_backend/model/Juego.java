package cl.esports.arena_manager_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "juego")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "integrantes_por_equipo", nullable = false)
    private Integer integrantesPorEquipo;

    @Column(nullable = false, length = 30)
    private String modalidad;
}