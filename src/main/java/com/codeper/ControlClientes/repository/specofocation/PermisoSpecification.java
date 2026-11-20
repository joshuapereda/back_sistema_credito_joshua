package com.codeper.ControlClientes.repository.specofocation;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import com.codeper.ControlClientes.dto.PermisoFilterCriteria;
import com.codeper.ControlClientes.entity.Permiso;
import jakarta.persistence.criteria.Predicate;

public class PermisoSpecification {

    public static Specification<Permiso> createSpecification(
            PermisoFilterCriteria criterials, String filterValue) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // BUSQUEDA GENERAL POR NOMBRE
            if (filterValue != null && !filterValue.trim().isEmpty()) {

                // Buscamos coincidencias parciales, sin distinguir mayusculas y minusculas
                String likeFilterValue = "%" + filterValue.trim().toLowerCase() + "%";

                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nombre")),
                        likeFilterValue
                    )
                );
            }

            // FILTROS ESPECIFICOS
            if (criterials != null) {

                // FILTRAR POR NOMBRE
                if (criterials.getNombre() != null &&
                    !criterials.getNombre().trim().isEmpty()) {

                    String nombre = "%" + criterials.getNombre().trim().toLowerCase() + "%";

                    predicates.add(
                        criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("nombre")),
                            nombre
                        )
                    );
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
