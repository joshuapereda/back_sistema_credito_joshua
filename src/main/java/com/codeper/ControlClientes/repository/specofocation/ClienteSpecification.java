
package com.codeper.ControlClientes.repository.specofocation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.codeper.ControlClientes.dto.ClienteFilterCriteria;
import com.codeper.ControlClientes.entity.Cliente;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class ClienteSpecification {

    public static Specification<Cliente> createSpecification(ClienteFilterCriteria criterials, String filterValue) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // JOIN: Accedemos a Persona desde Cliente
            Join<Object, Object> persona = root.join("persona", JoinType.LEFT);
            
            // Obtenemos los atributos que utilizaremos para buscar
            var docIdentidad = cb.lower(root.<String>get("docIdentidad"));
            var nombrePersona = cb.lower(persona.<String>get("nombre"));

            // BUSQUEDA GENERAL
            if (filterValue != null && !filterValue.trim().isEmpty()) {

                String likeFilterValue = "%" + filterValue.trim().toLowerCase() + "%";

                predicates.add(cb.or(
                        cb.like(docIdentidad, likeFilterValue),
                        cb.like(nombrePersona, likeFilterValue)
                ));
            }

            // FILTROS ESPECIFICOS
            if (criterials != null) {

                if (criterials.getDocIdentidad() != null
                        && !criterials.getDocIdentidad().trim().isEmpty()) {

                    predicates.add(cb.like(
                            docIdentidad,
                            "%" + criterials.getDocIdentidad().trim().toLowerCase() + "%"
                    ));
                }

                if (criterials.getNombrePersona() != null
                        && !criterials.getNombrePersona().trim().isEmpty()) {

                    predicates.add(cb.like(
                            nombrePersona,
                            "%" + criterials.getNombrePersona().trim().toLowerCase() + "%"
                    ));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
