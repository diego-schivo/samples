package com.diegoschivo.samples.jpa.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.diegoschivo.samples.jpa.domain.Product;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("context.xml")
@Transactional
public class ProductDaoTest
{

    @Autowired
    private ProductDao productDao;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Before
    public void insertTestData()
    {
        jdbcTemplate.update("INSERT INTO PRODUCT(CODE, NAME) VALUES('FOO', 'Lorem ipsum')");
        jdbcTemplate.update("INSERT INTO PRODUCT(CODE, NAME) VALUES('BAR', 'Dolor sit')");
    }

    @Test
    public void getProduct()
    {
        Product product = productDao.getProduct("FOOO");
        assertNull(product);
        product = productDao.getProduct("BAR");
        assertNotNull(product);
    }
}
