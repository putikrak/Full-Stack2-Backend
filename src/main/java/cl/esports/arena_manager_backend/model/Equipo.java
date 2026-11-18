package cl.esports.arena_manager_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String nombre;

    @Column(nullable = false)
    private Boolean activo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "juego_id", nullable = false)
    private Juego juego;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "capitan_id", nullable = false)
    private Usuario capitan;

    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<EquipoIntegrante> integrantes = new ArrayList<>();

    @Transient
    public Long getJuegoId() {
        return juego != null ? juego.getId() : null;
    }

    @Transient
    public Long getCapitanId() {
        return capitan != null ? capitan.getId() : null;
    }
}