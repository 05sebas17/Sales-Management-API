package com.dev.senior.service;

import com.dev.senior.dto.UsuarioRequest;
import com.dev.senior.dto.UsuarioResponse;
import com.dev.senior.exception.DuplicateResourceException;
import com.dev.senior.exception.ResourceNotFoundException;
import com.dev.senior.model.Rol;
import com.dev.senior.model.Usuario;
import com.dev.senior.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("Pedro Gomez", "pedro@test.com", "encodedPassword", Rol.USER);
        usuario.setId(1L);
    }

    @Test
    void crearUsuario_Exito() {
        UsuarioRequest request = new UsuarioRequest("Pedro Gomez", "pedro@test.com", "Password123!", Rol.USER);

        when(usuarioRepository.existsByCorreo(request.getCorreo())).thenReturn(false);
        when(passwordEncoder.encode(request.getContrasena())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponse response = usuarioService.crear(request);

        assertNotNull(response);
        assertEquals("Pedro Gomez", response.getNombre());
        assertEquals("pedro@test.com", response.getCorreo());
        assertEquals("USER", response.getRol());

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_CorreoDuplicado_LanzaExcepcion() {
        UsuarioRequest request = new UsuarioRequest("Pedro Gomez", "pedro@test.com", "Password123!", Rol.USER);

        when(usuarioRepository.existsByCorreo(request.getCorreo())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> usuarioService.crear(request));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void buscarPorId_Exito() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioResponse response = usuarioService.buscarPorIdResponse(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("pedro@test.com", response.getCorreo());
    }

    @Test
    void buscarPorId_NoExiste_LanzaExcepcion() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.buscarPorIdResponse(99L));
    }

    @Test
    void listarTodos_RetornaLista() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioResponse> lista = usuarioService.listarTodos();

        assertNotNull(lista);
        assertEquals(1, lista.size());
    }

    @Test
    void eliminarUsuario_Exito() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        assertDoesNotThrow(() -> usuarioService.eliminar(1L));
        verify(usuarioRepository, times(1)).deleteById(1L);
    }
}
