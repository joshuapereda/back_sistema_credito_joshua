package com.codeper.ControlClientes.repository.specofocation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.codeper.ControlClientes.dto.CompraFilterCriteria;
import com.codeper.ControlClientes.entity.Compra;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class CompraSpecification {

    public static Specification<Compra> createSpecification(CompraFilterCriteria criterials, String filterValue) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Cada join se crea una sola vez
            Join<Object, Object> tipo = root.join("tipoCompra", JoinType.LEFT);
            Join<Object, Object> cliente = root.join("cliente", JoinType.LEFT);
            Join<Object, Object> persona = cliente.join("persona", JoinType.LEFT);

            Expression<String> tipoNombre = cb.lower(tipo.<String>get("nombre"));
            Expression<String> clienteNombre = cb.lower(persona.<String>get("nombre"));
            Expression<String> estadoTexto = cb.lower(root.get("estado").as(String.class));

            // Búsqueda general (una sola caja de texto, busca en varias columnas)
            if (filterValue != null && !filterValue.trim().isEmpty()) {
                String like = "%" + filterValue.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(tipoNombre, like),
                    cb.like(clienteNombre, like),
                    cb.like(estadoTexto, like)
                ));
            }

            // Filtros específicos (solo si vienen criterios)
            if (criterials != null) {
                if (criterials.getTipoCompra() != null && !criterials.getTipoCompra().trim().isEmpty()) {
                    predicates.add(cb.like(tipoNombre,
                        "%" + criterials.getTipoCompra().trim().toLowerCase() + "%"));
                }
                if (criterials.getNombreCliente() != null && !criterials.getNombreCliente().trim().isEmpty()) {
                    predicates.add(cb.like(clienteNombre,
                        "%" + criterials.getNombreCliente().trim().toLowerCase() + "%"));
                }
                if (criterials.getEstadoCompra() != null && !criterials.getEstadoCompra().trim().isEmpty()) {
                    predicates.add(cb.like(estadoTexto,
                        "%" + criterials.getEstadoCompra().trim().toLowerCase() + "%"));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}