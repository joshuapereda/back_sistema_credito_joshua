package com.codeper.ControlClientes.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.codeper.ControlClientes.exception.ConflictException;
import com.codeper.ControlClientes.repository.PermisoRepository;
import com.codeper.ControlClientes.repository.RolRepository;

@ExtendWith(MockitoExtension.class)
class RolServiceTest {

    @Mock private RolRepository rolRepository;
    @Mock private PermisoRepository permisoRepository;
    @InjectMocks private RolService rolService;

    @Test
    void eliminarBorraElRolYNingunPermiso() {
        when(rolRepository.existsById(3)).thenReturn(true);

        rolService.eliminarRol(3);

        verify(rolRepository).deleteById(3);
        verify(permisoRepository, never()).deleteById(any());
    }

    @Test
    void noEliminaUnRolConPermisos() {
        when(rolRepository.existsById(3)).thenReturn(true);
        when(permisoRepository.existsByRolesId(3)).thenReturn(true);

        assertThrows(ConflictException.class, () -> rolService.eliminarRol(3));
        verify(rolRepository, never()).deleteById(any());
    }
}
