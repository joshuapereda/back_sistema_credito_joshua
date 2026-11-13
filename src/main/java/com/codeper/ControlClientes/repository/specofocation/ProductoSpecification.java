package com.codeper.ControlClientes.repository.specofocation;


import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import com.codeper.ControlClientes.dto.ProductoFilterCriteria;
import com.codeper.ControlClientes.entity.Producto;

public class ProductoSpecification {
    public static Specification<Producto> createSpecification(ProductoFilterCriteria criterials, String filterValue){
        //consulta un QUERY
        return (root,query,criteriaBuilder)->{
            List<Predicate> predicates=new ArrayList<>();
            //si no es nulo y no esta vacio significa que tenemos un valor para aplicar el filtro
            if(filterValue !=null && !filterValue.trim().isEmpty()){
                //Utilizamos el like a nivel sql
                String likeFilterValue="%"+filterValue.toLowerCase()+"%";
                predicates.add(criteriaBuilder.or(
                    //where nombre like "%{filterValue}" or descripcion ...(deacuerdo a los criterios de bsuqueda)
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")),filterValue),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("descripcion")),filterValue),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("categoria").get("nombre")),filterValue)
                ));
            }
            //FILTROS ESPECIFICOS
            if(criterials.getNombre() !=null && !criterials.getNombre().trim().isEmpty()){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")),
                 "%"+criterials.getNombre().toLowerCase()+"%"));
            }
            if(criterials.getDescripcion() !=null && !criterials.getDescripcion().trim().isEmpty()){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("descripcion")),
                 "%"+criterials.getDescripcion().toLowerCase()+"%"));
            }
            //Busqueda para una relacion con JOIN (valor que se optiene de otra tabla )
            if(criterials.getNombreCategoria() !=null && !criterials.getNombreCategoria().trim().isEmpty()){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("categoria").get("nombre")),
                 "%"+criterials.getNombreCategoria().toLowerCase()+"%"));
            }
            return criteriaBuilder.and(predicates);
        };
    }
}
