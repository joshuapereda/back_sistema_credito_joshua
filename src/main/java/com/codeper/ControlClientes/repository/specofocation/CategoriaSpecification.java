
package com.codeper.ControlClientes.repository.specofocation;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.codeper.ControlClientes.dto.CategoriaFilterCriteria;
import com.codeper.ControlClientes.entity.Categoria;

public class CategoriaSpecification {

    public static Specification<Categoria> createSpecification(CategoriaFilterCriteria criterials, String filterValue) {

        return (root, query, cb) -> {

            // Lista donde almacenaremos las condiciones de busqueda
            List<Predicate> predicates = new ArrayList<>();

            // BUSQUEDA GENERAL
            // Buscamos por el nombre de la categoria
            if (filterValue != null && !filterValue.trim().isEmpty()) {

                // Agregamos % para buscar coincidencias parciales
                String likeFilterValue = "%" + filterValue.trim().toLowerCase() + "%";

                predicates.add(cb.like(
                        cb.lower(root.<String>get("nombre")),
                        likeFilterValue
                ));
            }

            // FILTROS ESPECIFICOS
            // Comprobamos que los criterios no sean null
            if (criterials != null) {

                // FILTRO POR NOMBRE
                if (criterials.getNombre() != null
                        && !criterials.getNombre().trim().isEmpty()) {

                    predicates.add(cb.like(
                            cb.lower(root.<String>get("nombre")),
                            "%" + criterials.getNombre().trim().toLowerCase() + "%"
                    ));
                }
            }
            // AND: Aplicamos todas las condiciones de busqueda
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
