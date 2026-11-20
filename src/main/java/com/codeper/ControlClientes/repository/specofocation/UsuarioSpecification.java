
package com.codeper.ControlClientes.repository.specofocation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.codeper.ControlClientes.dto.UsuarioFilterCriteria;
import com.codeper.ControlClientes.entity.Usuario;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class UsuarioSpecification {

    public static Specification<Usuario> createSpecification(
            UsuarioFilterCriteria criterials, String filterValue) {

        return (root, query, cb) -> {

            // Lista donde almacenaremos las condiciones de busqueda
            List<Predicate> predicates = new ArrayList<>();

            // JOIN: Desde Usuario accedemos a Persona
            // Necesitamos este JOIN porque el nombre esta en Persona
            Join<Object, Object> persona = root.join("persona", JoinType.LEFT);

            // Obtenemos los atributos que utilizaremos para buscar
            // LOWER convierte los valores a minusculas
            var username = cb.lower(root.<String>get("username"));
            var nombrePersona = cb.lower(persona.<String>get("nombre"));

            // BUSQUEDA GENERAL
            // Busca por username o nombre de la persona
            if (filterValue != null && !filterValue.trim().isEmpty()) {

                // Agregamos % para buscar coincidencias parciales
                String likeFilterValue = "%" + filterValue.trim().toLowerCase() + "%";

                predicates.add(cb.or(
                        cb.like(username, likeFilterValue),
                        cb.like(nombrePersona, likeFilterValue)
                ));
            }

            // FILTROS ESPECIFICOS
            // Comprobamos que los criterios no sean null
            if (criterials != null) {

                // FILTRO POR USERNAME
                if (criterials.getUsername() != null
                        && !criterials.getUsername().trim().isEmpty()) {

                    predicates.add(cb.like(
                            username,
                            "%" + criterials.getUsername().trim().toLowerCase() + "%"
                    ));
                }

                // FILTRO POR NOMBRE DE PERSONA
                if (criterials.getNombrePersona() != null
                        && !criterials.getNombrePersona().trim().isEmpty()) {

                    predicates.add(cb.like(
                            nombrePersona,
                            "%" + criterials.getNombrePersona().trim().toLowerCase() + "%"
                    ));
                }
            }
            // AND: Todas las condiciones de busqueda deben cumplirse
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
