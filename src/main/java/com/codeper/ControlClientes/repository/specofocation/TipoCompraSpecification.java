package com.codeper.ControlClientes.repository.specofocation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.codeper.ControlClientes.dto.TipoCompraFilterCriteria;
import com.codeper.ControlClientes.entity.TipoCompra;

import jakarta.persistence.criteria.Predicate;

public class TipoCompraSpecification {

    public static Specification<TipoCompra> createSpecification(TipoCompraFilterCriteria criterials, String filterValue){
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
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("descripcion")),filterValue)
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
            return criteriaBuilder.and(predicates);
        };
    }
}
