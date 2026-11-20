
package com.codeper.ControlClientes.repository.specofocation;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import com.codeper.ControlClientes.dto.PagoFilterCriteria;
import com.codeper.ControlClientes.entity.Pago;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class PagoSpecification {

    public static Specification<Pago> createSpecification(PagoFilterCriteria criterials, String filterValue) {

        // Construimos una consulta dinámica sobre la entidad Pago
        return (root, query, cb) -> {

            // Lista donde almacenaremos las condiciones de búsqueda (WHERE)
            List<Predicate> predicates = new ArrayList<>();

            // JOIN 1: Desde Pago accedemos a Compra
            // "compra" es el atributo de relación que existe en la entidad Pago
            Join<Object, Object> compra = root.join("compra", JoinType.LEFT);

            // JOIN 2: Desde Compra accedemos a Cliente
            // "cliente" es el atributo de relación que existe en la entidad Compra
            Join<Object, Object> cliente = compra.join("cliente", JoinType.LEFT);

            // JOIN 3: Desde Cliente accedemos a Persona
            // "persona" es el atributo de relación que existe en la entidad Cliente
            // Necesitamos este JOIN porque el nombre está almacenado en Persona
            Join<Object, Object> persona = cliente.join("persona", JoinType.LEFT);

            // Obtenemos el atributo nombre de Persona
            // LOWER convierte el texto a minúsculas para buscar sin distinguir mayúsculas
            var clienteNombre = cb.lower(persona.<String>get("nombre"));

            // BUSQUEDA GENERAL
            // Comprueba si filterValue tiene algún valor
            if (filterValue != null && !filterValue.trim().isEmpty()) {

                // Agregamos % al inicio y al final para buscar coincidencias parciales
                // Ejemplo: "%joshua%" encontrará "Joshua Pereda"
                String like = "%" + filterValue.trim().toLowerCase() + "%";

                // Agregamos la condición de búsqueda por nombre del cliente
                predicates.add(cb.like(clienteNombre, like));
            }

            // FILTROS ESPECIFICOS
            // Comprobamos que el objeto de criterios no sea null
            if (criterials != null) {

                // Verificamos que el filtro por nombre del cliente tenga un valor
                if (criterials.getNombreCliente() != null&& !criterials.getNombreCliente().trim().isEmpty()) {
                    // Buscamos coincidencias parciales por nombre del cliente
                    predicates.add(cb.like(clienteNombre,
                            "%" + criterials.getNombreCliente().trim().toLowerCase() + "%"
                    ));
                }
            }

            // AND: Todas las condiciones agregadas deben cumplirse
            // Convertimos la lista de Predicate en un arreglo para ejecutar la consulta
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
