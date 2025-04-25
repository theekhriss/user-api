package com.bci.userapi.test;

import com.bci.userapi.controller.UserController;
import com.bci.userapi.dto.UserRequestDTO;
import com.bci.userapi.dto.UserResponseDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void setup() {
        userController = new UserController(new UserServiceFake());
    }

    @Test
    void testCreate() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setNombre("Pedro");
        dto.setCorreo("pedro@mail.com");

        ResponseEntity<?> response = userController.create(dto);

        assertEquals(200, response.getStatusCodeValue());
        UserResponseDTO user = (UserResponseDTO) response.getBody();
        assertEquals("Pedro", user.getNombre());
    }

    @Test
    void testGetById() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setNombre("Ana");
        dto.setCorreo("ana@mail.com");

        UserResponseDTO created = (UserResponseDTO) userController.create(dto).getBody();
        ResponseEntity<?> response = userController.getById(created.getId());

        assertEquals(200, response.getStatusCodeValue());
        UserResponseDTO found = (UserResponseDTO) response.getBody();
        assertEquals("Ana", found.getNombre());
    }

    @Test
    void testUpdate() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setNombre("Mario");
        dto.setCorreo("mario@mail.com");

        UserResponseDTO created = (UserResponseDTO) userController.create(dto).getBody();

        UserRequestDTO update = new UserRequestDTO();
        update.setNombre("Mario Editado");

        ResponseEntity<?> response = userController.update(created.getId(), update);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Mario Editado", ((UserResponseDTO) response.getBody()).getNombre());
    }

    @Test
    void testDelete() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setNombre("Lucía");
        dto.setCorreo("lucia@mail.com");

        UserResponseDTO created = (UserResponseDTO) userController.create(dto).getBody();
        ResponseEntity<?> response = userController.delete(created.getId());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Usuario eliminado con éxito", ((Map<?, ?>) response.getBody()).get("mensaje"));
    }
}
