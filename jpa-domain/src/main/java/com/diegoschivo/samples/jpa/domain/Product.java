package com.diegoschivo.samples.jpa.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "PRODUCT")
public class Product implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    public Product()
    {
    }

    public Product(String code, String name)
    {
        this.code = code;
        this.name = name;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }
        if (!(other instanceof Product))
        {
            return false;
        }
        final Product that = (Product) other;
        return code.equals(that.code);
    }

    @Override
    public int hashCode()
    {
        return code.hashCode();
    }
}
