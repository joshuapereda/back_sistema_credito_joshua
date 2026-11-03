package com.codeper.ControlClientes.repository.specofocation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import com.codeper.ControlClientes.dto.UsuarioFilterCriteria;
import com.codeper.ControlClientes.entity.Usuario;

import jakarta.persistence.criteria.Predicate;

public class UsuarioSpecification {
     public static Specification<Usuario> createSpecification(UsuarioFilterCriteria criterials, String filterValue){
        //consulta un QUERY
        return (root,query,criteriaBuilder)->{
            List<Predicate> predicates=new ArrayList<>();
            //si no es nulo y no esta vacio significa que tenemos un valor para aplicar el filtro
            if(filterValue !=null && !filterValue.trim().isEmpty()){
                //Utilizamos el like a nivel sql
                String likeFilterValue="%"+filterValue.toLowerCase()+"%";
                predicates.add(criteriaBuilder.or(
                    //where nombre like "%{filterValue}" or descripcion ...(deacuerdo a los criterios de bsuqueda)
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("username")),filterValue),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("persona").get("nombre")),filterValue)
                ));
            }
            //FILTROS ESPECIFICOS
            if(criterials.getUsername() !=null && !criterials.getUsername().trim().isEmpty()){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("username")),
                 "%"+criterials.getUsername().toLowerCase()+"%"));
            }
           
            //Busqueda para una relacion con JOIN (valor que se optiene de otra tabla )
            if(criterials.getNombrePersona() !=null && !criterials.getNombrePersona().trim().isEmpty()){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("persona").get("nombre")),
                 "%"+criterials.getNombrePersona().toLowerCase()+"%"));
            }
            return criteriaBuilder.and(predicates);
        };
    }
}

