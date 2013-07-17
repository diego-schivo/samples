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

package com.diegoschivo.samples.apache.cxf.sum;

import static org.junit.Assert.assertEquals;

import org.apache.cxf.test.TestUtilities;
import org.apache.cxf.testutil.common.TestUtil;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;


@ContextConfiguration(locations = {"classpath:com/diegoschivo/samples/apache/cxf/sum/SumTest.xml" })
public class SumTest extends AbstractJUnit4SpringContextTests
{

    static final String PORT = TestUtil.getPortNumber(SumTest.class);

    private TestUtilities testUtilities;

    public SumTest()
    {
        testUtilities = new TestUtilities(getClass());
    }

    @Test
    public void testSum() throws Exception
    {
        int a = 3;
        int b = 2;
        int c = applicationContext.getBean("sumClient", Sum.class).sum(a, b);
        assertEquals(5, c);
    }
}
