/*
 * Copyright (c) 2010-2012 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package org.asynchttpclient.proxy;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.proxy.ProxyServer;
import org.asynchttpclient.util.ProxyUtils;
import org.testng.annotations.Test;

public class ProxyUtilsTest {
    @Test(groups = "fast")
    public void testBasics() {
        // should avoid, there is no proxy (is null)
        Request req = new RequestBuilder("GET").setUrl("http://somewhere.com/foo").build();
        assertTrue(ProxyUtils.ignoreProxy(null, req));

        // should avoid, it's in non-proxy hosts
        req = new RequestBuilder("GET").setUrl("http://somewhere.com/foo").build();
        ProxyServer proxyServer = ProxyServer.newProxyServer("foo", 1234).nonProxyHost("somewhere.com").build();
        assertTrue(ProxyUtils.ignoreProxy(proxyServer, req));

        // should avoid, it's in non-proxy hosts (with "*")
        req = new RequestBuilder("GET").setUrl("http://sub.somewhere.com/foo").build();
        proxyServer = ProxyServer.newProxyServer("foo", 1234).nonProxyHost("*.somewhere.com").build();
        assertTrue(ProxyUtils.ignoreProxy(proxyServer, req));

        // should use it
        req = new RequestBuilder("GET").setUrl("http://sub.somewhere.com/foo").build();
        proxyServer = ProxyServer.newProxyServer("foo", 1234).nonProxyHost("*.somewhere.org").build();
        assertFalse(ProxyUtils.ignoreProxy(proxyServer, req));
    }
}
