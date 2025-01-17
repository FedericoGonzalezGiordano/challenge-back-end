package ar.edu.utn.frc.tup.lc.iv.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class User {
        /**
         * Nombre real del usuario.
         */
        private String name;
        /**
         * Apellido del usuario.
         */
        private String lastname;
        /**
         * Contraseña del usuario utilizada en login.
         */
        private String password;
        /**
         * Contraseña del email utilizada en login.
         */
        private String email;

        private String avatar_url;
}