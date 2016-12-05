/***************************************************************************

Copyright (c) 2016, EPAM SYSTEMS INC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

****************************************************************************/

package com.epam.dlab.auth.aws;

import com.amazonaws.services.identitymanagement.model.User;
import com.epam.dlab.auth.UserInfo;
import com.epam.dlab.auth.conveyor.AwsUserCache;
import com.epam.dlab.auth.conveyor.LdapFilterCache;
import com.epam.dlab.auth.conveyor.LoginCache;
import org.junit.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AwsTest {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAwsCache() throws InterruptedException {
        AwsUserCache c = AwsUserCache.getInstance();
        c.setDefaultBuilderTimeout(1, TimeUnit.SECONDS);
        c.setIdleHeartBeat(100,TimeUnit.MILLISECONDS);
        c.save(new User().withUserId("test"));
        User u = c.getAwsUserInfo("test");
        assertNotNull(u);
        System.out.println(u);
        Thread.sleep(1500);
        u = c.getAwsUserInfo("test");
        assertNull(u);
    }

    @Test
    public void testLoginCache() throws InterruptedException {
        LoginCache c = LoginCache.getInstance();
        c.setDefaultBuilderTimeout(1, TimeUnit.SECONDS);
        c.setIdleHeartBeat(100,TimeUnit.MILLISECONDS);
        c.save(new UserInfo("test","a"));
        UserInfo u = c.getUserInfo("a");
        assertNotNull(u);
        System.out.println(u);
    }

    @Test
    public void testLdapCache() throws InterruptedException {
        LdapFilterCache c = LdapFilterCache.getInstance();
        c.setIdleHeartBeat(100,TimeUnit.MILLISECONDS);
        Map<String,Object> m = new HashMap<>();
        m.put("name","a");
        c.save("a",m,100);
        Map<String,Object> m2 = c.getLdapFilterInfo("a");
        assertNotNull(m2);
        assertTrue(m==m2);
        m2.put("test","me");
        System.out.println(m);
        Thread.sleep(1000);
    }


}