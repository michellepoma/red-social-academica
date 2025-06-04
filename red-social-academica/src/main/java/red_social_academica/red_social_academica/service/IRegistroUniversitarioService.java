package red_social_academica.red_social_academica.service;

public interface IRegistroUniversitarioService {

    /**
     * Verifica si un Registro Universitario (RU) existe en el sistema y está habilitado.
     *
     * @param ru número de matrícula ingresado por el usuario
     * @return true si el RU es válido y activo, false en caso contrario
     */
    boolean esRuValido(String ru);
}
