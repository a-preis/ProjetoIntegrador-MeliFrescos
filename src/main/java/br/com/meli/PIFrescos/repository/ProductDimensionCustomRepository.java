package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.ProductDimension;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Cria uma query customizada e dinâmica para parâmetros não obrigatórios e retorna a Lista de ProductDimensions
 * resultante.
 * @author Ana Preis
 */
@Repository
public class ProductDimensionCustomRepository {

    private final EntityManager em;

    public ProductDimensionCustomRepository(EntityManager em){
        this.em = em;
    }

    public List<ProductDimension> find(Float maxHeight, Float maxWidth, Float maxLength, Float maxWeight, String order){

        String query = "select pd from ProductDimension pd ";
        String condicao = "where";

        if(maxHeight != null){
            query += condicao + " pd.height <= :maxHeight ";
            condicao = "and";
        }

        if(maxWidth != null){
            query += condicao + " pd.width <= :maxWidth ";
            condicao = "and";
        }

        if(maxLength != null){
            query += condicao + " pd.length <= :maxLength ";
            condicao = "and";
        }

        if(maxWeight != null){
            query += condicao + " pd.weight <= :maxWeight ";
            condicao = "and ";
        }

        if(order != null){
            if(order.equalsIgnoreCase("asc")){
                query += "order by coalesce(pd.height, pd.width, pd.length, pd.weight)";
            }
            if(order.equalsIgnoreCase("desc")){
                query += "order by coalesce(pd.height, pd.width, pd.length, pd.weight) desc";
            }
        }

        var q = em.createQuery(query, ProductDimension.class);

        if(maxHeight != null){
            q.setParameter("maxHeight", maxHeight);
        }

        if(maxWidth != null){
            q.setParameter("maxWidth", maxWidth);
        }

        if(maxLength != null){
            q.setParameter("maxLength", maxLength);
        }

        if(maxWeight != null){
            q.setParameter("maxWeight", maxWeight);
        }

        return q.getResultList();
    }

}
