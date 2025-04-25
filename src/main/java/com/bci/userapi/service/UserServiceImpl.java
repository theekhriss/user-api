
package com.bci.userapi.service;

import com.bci.userapi.dto.UserRequestDTO;
import com.bci.userapi.dto.UserResponseDTO;
import com.bci.userapi.model.Phone;
import com.bci.userapi.model.User;
import com.bci.userapi.repository.UserRepository;
import com.bci.userapi.security.JwtUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	@Autowired
	private final UserRepository userRepository;
	@Autowired
	private final JwtUtil jwtUtil;

	@Value("${app.regex.password}")
	private String passwordRegex;

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
		if (!dto.getContraseña().matches(passwordRegex)) {
		    throw new RuntimeException("La contraseña no cumple con el formato requerido");
		}
		List<Phone> phones = dto.getTelefonos().stream()
				.map(p -> new Phone(null, p.getNumero(), p.getCodigoCiudad(), p.getCodigoPais()))
				.collect(Collectors.toList());

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
		User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
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
    @Transactional
    public UserResponseDTO updateUser(UUID id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        user.setNombre(dto.getNombre());
        user.setCorreo(dto.getCorreo());
        user.setContraseña(dto.getContraseña());
        user.setModificado(LocalDateTime.now());
        user.setTelefonos(dto.getTelefonos().stream()
                .map(p -> new Phone(null, p.getNumero(), p.getCodigoCiudad(), p.getCodigoPais()))
                .collect(Collectors.toList()));

        return mapToResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDTO patchUser(UUID id, Map<String, Object> updates) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        updates.forEach((campo, valor) -> {
            switch (campo) {
                case "nombre":
                    user.setNombre((String) valor); break;
                case "correo":
                    user.setCorreo((String) valor); break;
                case "contraseña":
                    user.setContraseña((String) valor); break;
            }
        });

        user.setModificado(LocalDateTime.now());
        return mapToResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        userRepository.delete(user);
    }

    private UserResponseDTO mapToResponse(User user) {
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
