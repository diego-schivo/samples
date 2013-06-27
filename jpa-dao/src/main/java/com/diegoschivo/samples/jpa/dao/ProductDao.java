package com.diegoschivo.samples.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.diegoschivo.samples.jpa.domain.Product;


/**
 * @author diego
 * @version $Id: $
 */
@Repository
public class ProductDao
{

        @PersistenceContext
        private EntityManager entityManager;

        public Product getProduct(String code)
        {
            Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.code = :code");
            query.setParameter("code", code);
            try
            {
                return (Product) query.getSingleResult();
            }
            catch (NoResultException e)
            {
                return null;
            }
        }
}
