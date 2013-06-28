/**
 *    Copyright 2013 Diego Schivo
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.diegoschivo.samples.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.diegoschivo.samples.jpa.domain.Product;


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
