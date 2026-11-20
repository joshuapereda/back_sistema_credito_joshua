package com.codeper.ControlClientes.repository.specofocation;


import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import com.codeper.ControlClientes.dto.ProductoFilterCriteria;
import com.codeper.ControlClientes.entity.Producto;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
public class ProductoSpecification {
    public static Specification<Producto> createSpecification(ProductoFilterCriteria criterials, String filterValue){
        //consulta un QUERY
        return (root,query,cb)->{
            List<Predicate> predicates=new ArrayList<>();

            // JOIN: Accedemos a Categoria desde Producto
            Join<Object, Object> categoria = root.join("categoria", JoinType.LEFT);
            
            // Obtenemos los atributos que utilizaremos para buscar
            var nombre = cb.lower(root.<String>get("nombre"));
            var nombreCategoria = cb.lower(categoria.<String>get("nombre"));

            // BUSQUEDA GENERAL
            if(filterValue !=null && !filterValue.trim().isEmpty()){
                //Utilizamos el like a nivel sql
                String likeFilterValue = "%" + filterValue.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(nombre, likeFilterValue),
                        cb.like(nombreCategoria, likeFilterValue)
                ));
            }
            // FILTROS ESPECIFICOS
            if (criterials != null) {

                if (criterials.getNombre() != null
                        && !criterials.getNombre().trim().isEmpty()) {

                    predicates.add(cb.like(
                            nombre,
                            "%" + criterials.getNombre().trim().toLowerCase() + "%"
                    ));
                }

                if (criterials.getNombreCategoria() != null
                        && !criterials.getNombreCategoria().trim().isEmpty()) {

                    predicates.add(cb.like(
                            nombreCategoria,
                            "%" + criterials.getNombreCategoria().trim().toLowerCase() + "%"
                    ));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
