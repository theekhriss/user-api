
package com.bci.userapi.dto;

import java.util.List;

import javax.persistence.Transient;
import javax.validation.constraints.Pattern;

public class UserRequestDTO {

    private String nombre;
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Correo inválido")
    private String correo;
    private String contraseña;
    private List<PhoneDTO> telefonos;

    @Transient
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";

    public boolean isPasswordValid() {
        return this.contraseña != null && this.contraseña.matches(PASSWORD_REGEX);
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public List<PhoneDTO> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<PhoneDTO> telefonos) {
        this.telefonos = telefonos;
    }

    public static class PhoneDTO {
        private String numero;
        private String codigoCiudad;
        private String codigoPais;

        public String getNumero() {
            return numero;
        }

        public void setNumero(String numero) {
            this.numero = numero;
        }

        public String getCodigoCiudad() {
            return codigoCiudad;
        }

        public void setCodigoCiudad(String codigoCiudad) {
            this.codigoCiudad = codigoCiudad;
        }

        public String getCodigoPais() {
            return codigoPais;
        }

        public void setCodigoPais(String codigoPais) {
            this.codigoPais = codigoPais;
        }
    }
}
