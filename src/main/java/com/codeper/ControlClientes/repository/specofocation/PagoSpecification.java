package com.codeper.ControlClientes.repository.specofocation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.codeper.ControlClientes.dto.PagoFilterCriteria;
import com.codeper.ControlClientes.entity.Pago;

import jakarta.persistence.criteria.Predicate;

public class PagoSpecification {
public static Specification<Pago> createSpecification(PagoFilterCriteria criterials, String filterValue){
        //consulta un QUERY
        return (root,query,criteriaBuilder)->{
            List<Predicate> predicates=new ArrayList<>();
            //si no es nulo y no esta vacio significa que tenemos un valor para aplicar el filtro
            if(filterValue !=null && !filterValue.trim().isEmpty()){
                //Utilizamos el like a nivel sql
                String likeFilterValue="%"+filterValue.toLowerCase()+"%";
                predicates.add(criteriaBuilder.or(
                    //where nombre like "%{filterValue}" or descripcion ...(deacuerdo a los criterios de bsuqueda)
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("compra").get("estado")),filterValue),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("compra").get("cliente").get("nombre")),filterValue)
                ));
            }
            //FILTROS ESPECIFICOS
            //Busqueda para una relacion con JOIN (valor que se optiene de otra tabla )
            if(criterials.getNombreCliente() !=null && !criterials.getNombreCliente().trim().isEmpty()){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("compra").get("cliente").get("nombre")),
                 "%"+criterials.getNombreCliente().toLowerCase()+"%"));
            }
            if(criterials.getEstadoCompra() !=null && !criterials.getEstadoCompra().trim().isEmpty()){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("compra").get("estado")),
                 "%"+criterials.getEstadoCompra().toLowerCase()+"%"));
            }
            return criteriaBuilder.and(predicates);
        };
    }
}

