package cl.esports.arena_manager_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "equipo_integrante")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipoIntegrante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "rol_equipo", nullable = false, length = 30)
    private String rolEquipo;

    @Transient
    public Long getJugadorId() {
        return usuario != null ? usuario.getId() : null;
    }
}