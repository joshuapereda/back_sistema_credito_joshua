package com.codeper.ControlClientes.repository.specofocation;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import com.codeper.ControlClientes.dto.RolFilterCriteria;
import com.codeper.ControlClientes.entity.Rol;
import jakarta.persistence.criteria.Predicate;

public class RolSpecification {

    public static Specification<Rol> createSpecification(
            RolFilterCriteria criterials, String filterValue) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // BUSQUEDA GENERAL POR NOMBRE
            if (filterValue != null && !filterValue.trim().isEmpty()) {

                // Agregamos % al inicio y al final para buscar coincidencias parciales
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
