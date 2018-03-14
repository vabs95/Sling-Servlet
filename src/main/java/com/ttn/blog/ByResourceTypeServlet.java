/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.ttn.blog;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES;


@Component(service = {Servlet.class}, property = {SLING_SERVLET_RESOURCE_TYPES + "=blogR"})
@SuppressWarnings("serial")
public class ByResourceTypeServlet extends SlingSafeMethodsServlet {

    private final Logger log = LoggerFactory.getLogger(ByResourceTypeServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response) throws ServletException,
            IOException {
        Resource resource = request.getResource();

        Iterator<Resource> children = resource.listChildren();
        Writer w = response.getWriter();

        List<Resource> childrenList = new ArrayList();
        while (children.hasNext()) {
            childrenList.add(children.next());
        }

        w.write("\n");
        for (Resource resource1 : childrenList) {
            w.write(resource1.getName() + "\t");
        }

        w.write("\n");

        String orderBY = request.getParameter("orderBy");


        if (orderBY.equals("assc")) {
            Collections.sort(childrenList, new Comparator<Resource>() {
                @Override
                public int compare(Resource o1, Resource o2) {
                    ValueMap proValueMap1 = o1.adaptTo(ValueMap.class);
                    ValueMap proValueMap2 = o2.adaptTo(ValueMap.class);
                    String st1 = proValueMap1.get("jcr:created", "default");
                    String st2 = proValueMap2.get("jcr:created", "default");
                    return st1.compareTo(st2);
                }
            });
        } else if (orderBY.equals("desc")) {
            Collections.sort(childrenList, new Comparator<Resource>() {
                @Override
                public int compare(Resource o1, Resource o2) {
                    ValueMap proValueMap1 = o1.adaptTo(ValueMap.class);
                    ValueMap proValueMap2 = o2.adaptTo(ValueMap.class);
                    String st1 = proValueMap1.get("jcr:created", "default");
                    String st2 = proValueMap2.get("jcr:created", "default");
                    return st2.compareTo(st1);
                }
            });
        } else {
            w.write("Wrong parameter");
        }

        for (Resource resource1 : childrenList) {
            w.write(resource1.getName() + "\t");
        }


    }

}

