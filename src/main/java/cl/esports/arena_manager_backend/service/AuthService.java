package cl.esports.arena_manager_backend.service;

import cl.esports.arena_manager_backend.config.ApiException;
import cl.esports.arena_manager_backend.dto.AuthResponse;
import cl.esports.arena_manager_backend.dto.LoginRequest;
import cl.esports.arena_manager_backend.dto.RegistroRequest;
import cl.esports.arena_manager_backend.model.Usuario;
import cl.esports.arena_manager_backend.repository.UsuarioRepository;
import cl.esports.arena_manager_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(request.getCorreo().trim())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos."));

        String guardada = usuario.getPassword();
        boolean coincide = (guardada != null && guardada.startsWith("$2"))
                ? passwordEncoder.matches(request.getPassword(), guardada)
                : request.getPassword().equals(guardada);

        if (!coincide) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos.");
        }

        String token = jwtService.generarToken(usuario.getId(), usuario.getCorreo(), usuario.getApodo(), usuario.getRol());
        return AuthResponse.builder()
                .token(token)
                .tipo("Bearer")
                .usuario(usuario)
                .build();
    }

    @Transactional
    public AuthResponse registro(RegistroRequest request) {
        if (usuarioRepository.existsByCorreoIgnoreCase(request.getCorreo().trim())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Ya existe una cuenta registrada con ese correo.");
        }
        if (usuarioRepository.existsByApodoIgnoreCase(request.getApodo().trim())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El apodo ya está en uso por otro jugador.");
        }

        Usuario nuevo = Usuario.builder()
                .nombre(request.getNombre().trim())
                .apodo(request.getApodo().trim())
                .correo(request.getCorreo().trim())
                .edad(request.getEdad())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol("JUGADOR")
                .victorias(0)
                .derrotas(0)
                .build();

        Usuario guardado = usuarioRepository.save(nuevo);
        String token = jwtService.generarToken(guardado.getId(), guardado.getCorreo(), guardado.getApodo(), guardado.getRol());

        return AuthResponse.builder()
                .token(token)
                .tipo("Bearer")
                .usuario(guardado)
                .build();
    }
}