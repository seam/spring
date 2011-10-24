/*
 * JBoss, Home of Professional Open Source
 * Copyright [2011], Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.seam.spring.test.reflections;

import org.jboss.seam.spring.bootstrap.SpringContext;
import org.jboss.seam.spring.reflections.Annotations;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Marius Bogoevici
 */
public class TestAnnotationInstantiation {

    @Test
    public void testAnnotationType() {
        SpringContext springContext = Annotations.instanceOf(SpringContext.class);
        Assert.assertNotNull(springContext);
        Assert.assertNotNull(springContext.annotationType());
        Assert.assertNotNull(springContext.name());
        Assert.assertEquals("default", springContext.name());
    }

    @Test
    public void testAnnotationWithAllParametersSet() {
        Map<String, Object> teamAttributes = new HashMap<String, Object>();
        teamAttributes.put("name", "Ireland");
        teamAttributes.put("size", 7);
        Team team = Annotations.instanceOf(Team.class, teamAttributes);
        Assert.assertEquals(7, team.size());
        Assert.assertEquals("Ireland", team.name());
    }

    @Test
    public void testAnnotationWithDefaults() {
        Map<String, Object> teamAttributes = new HashMap<String, Object>();
        teamAttributes.put("name", "Ireland");
        Team team = Annotations.instanceOf(Team.class, teamAttributes);
        Assert.assertEquals(15, team.size());
        Assert.assertEquals("Ireland", team.name());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnnotationWithMissingAttributes() {
        Map<String, Object> teamAttributes = new HashMap<String, Object>();
        Team team = Annotations.instanceOf(Team.class, teamAttributes);
    }
}
