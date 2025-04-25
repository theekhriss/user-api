package com.bci.userapi.test;

import com.bci.userapi.dto.UserRequestDTO;
import com.bci.userapi.dto.UserResponseDTO;
import com.bci.userapi.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserServiceFake implements UserService {

    private final Map<UUID, UserResponseDTO> db = new HashMap<>();

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        UserResponseDTO response = new UserResponseDTO();
        response.setId(UUID.randomUUID());
        response.setNombre(dto.getNombre());
        response.setCorreo(dto.getCorreo());
        db.put(response.getId(), response);
        return response;
    }

    @Override
    public UserResponseDTO getUser(UUID id) {
        return db.get(id);
    }

    @Override
    public UserResponseDTO updateUser(UUID id, UserRequestDTO dto) {
        UserResponseDTO user = db.get(id);
        if (user == null) throw new RuntimeException("No encontrado");
        user.setNombre(dto.getNombre());
        return user;
    }

    @Override
    public UserResponseDTO patchUser(UUID id, Map<String, Object> updates) {
        UserResponseDTO user = db.get(id);
        if (user == null) throw new RuntimeException("No encontrado");
        if (updates.containsKey("nombre")) {
            user.setNombre((String) updates.get("nombre"));
        }
        return user;
    }

    @Override
    public void deleteUser(UUID id) {
        db.remove(id);
    }
}
