package com.diegoschivo.samples.apache.cxf.sum;

import javax.jws.WebService;


@WebService(endpointInterface = "com.diegoschivo.samples.apache.cxf.sum.Sum", serviceName = "Sum")
public class SumImpl implements Sum
{

    public int sum(int a, int b)
    {
        return a + b;
    }
}
