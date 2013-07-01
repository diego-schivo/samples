package com.diegoschivo.samples.apache.cxf.sum;

import javax.jws.WebService;


@WebService
public interface Sum
{

    int sum(int a, int b);
}
