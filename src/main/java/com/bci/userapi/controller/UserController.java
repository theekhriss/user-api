
package com.bci.userapi.controller;

import com.bci.userapi.dto.UserRequestDTO;
import com.bci.userapi.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UserController {

	@Autowired
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody UserRequestDTO dto) {
		try {
			return ResponseEntity.ok(userService.createUser(dto));
		} catch (RuntimeException ex) {
			return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", ex.getMessage()));
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable UUID id) {
		try {
			System.out.println(id);
			return ResponseEntity.ok(userService.getUser(id));
		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap("mensaje", "Usuario no encontrado"));
		}
	}
	
	@PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody UserRequestDTO dto) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, dto));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", ex.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patch(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        try {
            return ResponseEntity.ok(userService.patchUser(id, updates));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", ex.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(Collections.singletonMap("mensaje", "Usuario eliminado con éxito"));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", ex.getMessage()));
        }
    }
}
