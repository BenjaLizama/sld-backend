package com.promptlabs.usuarios_perfiles.service;

import com.promptlabs.usuarios_perfiles.dto.FamilyMemberDTO;
import com.promptlabs.usuarios_perfiles.dto.LinkFamilyRequest;
import com.promptlabs.usuarios_perfiles.dto.LinkFamilyResponse;
import com.promptlabs.usuarios_perfiles.entity.*;
import com.promptlabs.usuarios_perfiles.exception.RelationshipException;
import com.promptlabs.usuarios_perfiles.repository.FamilyRelationshipRepository;
import com.promptlabs.usuarios_perfiles.repository.ParentProfileRepository;
import com.promptlabs.usuarios_perfiles.repository.ParentTypeRepository;
import com.promptlabs.usuarios_perfiles.repository.StudentProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test unitario para FamilyRelationshipService.
 * 
 * Explicación de anotaciones:
 * @ExtendWith(MockitoExtension.class): Inicializa los mocks de Mockito y permite 
 * el uso de las anotaciones @Mock y @InjectMocks.
 */
@ExtendWith(MockitoExtension.class)
class FamilyRelationshipServiceTest {

    /**
     * @Mock: Crea una versión simulada de la interfaz/clase. 
     * No accede a la base de datos real.
     */
    @Mock
    private FamilyRelationshipRepository familyRelationshipRepository;
    @Mock
    private ParentProfileRepository parentProfileRepository;
    @Mock
    private StudentProfileRepository studentProfileRepository;
    @Mock
    private ParentTypeRepository parentTypeRepository;

    /**
     * @InjectMocks: Crea una instancia real de FamilyRelationshipService e 
     * inyecta automáticamente los @Mocks definidos arriba en su constructor.
     */
    @InjectMocks
    private FamilyRelationshipService familyRelationshipService;

    private LinkFamilyRequest validRequest;
    private ParentProfile mockParent;
    private StudentProfile mockStudent;
    private ParentType mockParentType;

    /**
     * @BeforeEach: Configuración común que se ejecuta antes de CADA test.
     * Garantiza que cada prueba sea independiente.
     */
    @BeforeEach
    void setUp() {
        validRequest = new LinkFamilyRequest("11.111.111-1", "22.222.222-2", 1L);
        
        mockParent = new ParentProfile();
        mockStudent = new StudentProfile();
        mockParentType = new ParentType();
        mockParentType.setRelationship("Padre");
    }

    // --- TESTS PARA linkFamily ---

    @Test
    @DisplayName("Debe vincular exitosamente a un apoderado con un estudiante")
    void linkFamily_Success() {
        // Arrange (Configuración): Definimos qué devolverán los mocks
        when(parentProfileRepository.findByUserRut(validRequest.parentId())).thenReturn(Optional.of(mockParent));
        when(studentProfileRepository.findByUserRut(validRequest.studentId())).thenReturn(Optional.of(mockStudent));
        when(parentTypeRepository.findById(validRequest.parentTypeId())).thenReturn(Optional.of(mockParentType));
        when(familyRelationshipRepository.existsByParentProfileAndStudentProfile(mockParent, mockStudent)).thenReturn(false);

        // Act (Acción): Ejecutamos el método a probar
        LinkFamilyResponse response = familyRelationshipService.linkFamily(validRequest);

        // Assert (Verificación): Comprobamos los resultados
        assertNotNull(response);
        assertTrue(response.mensaje().contains("correctamente"));
        verify(familyRelationshipRepository, times(1)).save(any(FamilyRelationship.class));
    }

    @Test
    @DisplayName("Debe lanzar RelationshipException si la relación ya existe")
    void linkFamily_AlreadyExists_ThrowsException() {
        // Arrange
        when(parentProfileRepository.findByUserRut(validRequest.parentId())).thenReturn(Optional.of(mockParent));
        when(studentProfileRepository.findByUserRut(validRequest.studentId())).thenReturn(Optional.of(mockStudent));
        when(parentTypeRepository.findById(validRequest.parentTypeId())).thenReturn(Optional.of(mockParentType));
        // Simulamos que la relación YA existe
        when(familyRelationshipRepository.existsByParentProfileAndStudentProfile(mockParent, mockStudent)).thenReturn(true);

        // Act & Assert
        assertThrows(RelationshipException.class, () -> familyRelationshipService.linkFamily(validRequest));
        // Verificamos que NUNCA se intente guardar
        verify(familyRelationshipRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar EntityNotFoundException si el apoderado no existe")
    void linkFamily_ParentNotFound_ThrowsException() {
        // Arrange: Simulamos que el repositorio devuelve vacío
        when(parentProfileRepository.findByUserRut(validRequest.parentId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> familyRelationshipService.linkFamily(validRequest));
    }

    // --- TESTS PARA unlinkFamily ---

    @Test
    @DisplayName("Debe desvincular exitosamente una relación existente")
    void unlinkFamily_Success() {
        // Arrange
        FamilyRelationship relationship = new FamilyRelationship();
        when(parentProfileRepository.findByUserRut(validRequest.parentId())).thenReturn(Optional.of(mockParent));
        when(studentProfileRepository.findByUserRut(validRequest.studentId())).thenReturn(Optional.of(mockStudent));
        when(familyRelationshipRepository.findByParentProfileAndStudentProfile(mockParent, mockStudent))
                .thenReturn(Optional.of(relationship));

        // Act
        familyRelationshipService.unlinkFamily(validRequest);

        // Assert
        verify(familyRelationshipRepository).delete(relationship);
    }

    @Test
    @DisplayName("Debe lanzar RelationshipException al desvincular si no existe la relación")
    void unlinkFamily_RelationshipNotFound_ThrowsException() {
        // Arrange
        when(parentProfileRepository.findByUserRut(validRequest.parentId())).thenReturn(Optional.of(mockParent));
        when(studentProfileRepository.findByUserRut(validRequest.studentId())).thenReturn(Optional.of(mockStudent));
        when(familyRelationshipRepository.findByParentProfileAndStudentProfile(mockParent, mockStudent))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RelationshipException.class, () -> familyRelationshipService.unlinkFamily(validRequest));
    }

    // --- TESTS PARA findParents ---

    @Test
    @DisplayName("Debe listar los apoderados de un estudiante correctamente")
    void findParents_Success() {
        // Arrange
        String studentRut = "22.222.222-2";
        User user = new User();
        user.setRut("11.111.111-1");
        user.setFirstName("Juan");
        
        mockParent.setUser(user);
        
        FamilyRelationship rel = new FamilyRelationship();
        rel.setParentProfile(mockParent);
        rel.setParentType(mockParentType);

        when(studentProfileRepository.existsByUserRut(studentRut)).thenReturn(true);
        when(familyRelationshipRepository.findByStudentProfileUserRut(studentRut)).thenReturn(List.of(rel));

        // Act
        List<FamilyMemberDTO> result = familyRelationshipService.findParents(studentRut);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).fullName());
        assertEquals("Padre", result.get(0).relationshipType());
    }

    @Test
    @DisplayName("Debe lanzar EntityNotFoundException al buscar padres si el estudiante no existe")
    void findParents_StudentNotFound_ThrowsException() {
        // Arrange
        String studentRut = "000";
        when(studentProfileRepository.existsByUserRut(studentRut)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> familyRelationshipService.findParents(studentRut));
    }
}
