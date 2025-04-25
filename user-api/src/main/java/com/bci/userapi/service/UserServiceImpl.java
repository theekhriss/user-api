
package com.bci.userapi.service;

import com.bci.userapi.dto.UserRequestDTO;
import com.bci.userapi.dto.UserResponseDTO;
import com.bci.userapi.model.Phone;
import com.bci.userapi.model.User;
import com.bci.userapi.repository.UserRepository;
import com.bci.userapi.security.JwtUtil;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        userRepository.findByCorreo(dto.getCorreo()).ifPresent(u -> {
            throw new RuntimeException("El correo ya está registrado");
        });

        List<Phone> phones = dto.getTelefonos().stream().map(p -> {
            Phone phone = new Phone();
            phone.setNumero(p.getNumero());
            phone.setCodigoCiudad(p.getCodigoCiudad());
            phone.setCodigoPais(p.getCodigoPais());
            return phone;
        }).collect(Collectors.toList());

        User user = new User();
        user.setNombre(dto.getNombre());
        user.setCorreo(dto.getCorreo());
        user.setContraseña(dto.getContraseña());
        user.setCreado(LocalDateTime.now());
        user.setModificado(LocalDateTime.now());
        user.setUltimoLogin(LocalDateTime.now());
        user.setActivo(true);
        user.setTelefonos(phones);
        user.setToken(jwtUtil.generateToken(dto.getCorreo()));

        user = userRepository.save(user);

        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setCreado(user.getCreado());
        response.setModificado(user.getModificado());
        response.setUltimoLogin(user.getUltimoLogin());
        response.setToken(user.getToken());
        response.setActivo(user.isActivo());

        return response;
    }

    @Override
    public UserResponseDTO getUser(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setCreado(user.getCreado());
        response.setModificado(user.getModificado());
        response.setUltimoLogin(user.getUltimoLogin());
        response.setToken(user.getToken());
        response.setActivo(user.isActivo());
        return response;
    }
}
