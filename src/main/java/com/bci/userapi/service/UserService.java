
package com.bci.userapi.service;

import com.bci.userapi.dto.UserRequestDTO;
import com.bci.userapi.dto.UserResponseDTO;

import java.util.Map;
import java.util.UUID;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO dto);
    UserResponseDTO getUser(UUID id);
    UserResponseDTO updateUser(UUID id, UserRequestDTO dto);
    UserResponseDTO patchUser(UUID id, Map<String, Object> updates);
    void deleteUser(UUID id);
}
