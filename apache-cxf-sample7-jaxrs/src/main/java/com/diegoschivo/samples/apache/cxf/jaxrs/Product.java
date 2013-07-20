package com.diegoschivo.samples.apache.cxf.jaxrs;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "Product")
public class Product
{

    private Long id;

    private String code;

    private String name;

    public Product()
    {
    }

    public Product(String code, String name)
    {
        this.code = code;
        this.name = name;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
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
}
