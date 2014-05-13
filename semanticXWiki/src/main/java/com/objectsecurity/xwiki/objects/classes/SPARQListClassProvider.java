/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.objectsecurity.xwiki.objects.classes;

import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;

import com.xpn.xwiki.internal.objects.classes.PropertyClassProvider;
import com.xpn.xwiki.internal.objects.meta.PropertyMetaClassInterface;
import com.xpn.xwiki.objects.classes.PropertyClassInterface;
import com.xpn.xwiki.objects.classes.StaticListClass;
import com.xpn.xwiki.objects.classes.StringClass;
import com.xpn.xwiki.objects.classes.TextAreaClass;
import com.xpn.xwiki.objects.meta.ListMetaClass;
import com.xpn.xwiki.objects.meta.PropertyMetaClass;

@Component
@Named("SPARQList")
@Singleton
public class SPARQListClassProvider implements PropertyClassProvider
{
    @Override
    public PropertyMetaClassInterface getDefinition() 
    {
        PropertyMetaClass definition = new ListMetaClass();
        definition.setPrettyName("SPARQ Database List");
        definition.setName(getClass().getAnnotation(Named.class).value());

        TextAreaClass sparq_class = new TextAreaClass();
        sparq_class.setName("sparq");
        sparq_class.setPrettyName("SPARQ Query");
        sparq_class.setSize(80);
        sparq_class.setRows(5);
        definition.safeput("sparq", sparq_class);
        //removeField("multiSelect");
        //removeField("relationalStorage");
        //removeField("picker");
        //removeField("size");
        definition.removeField("sort");
        definition.removeField("cache");

        StringClass insert_value_class = new StringClass();
        insert_value_class.setName("insertValue");
        insert_value_class.setPrettyName("Insert Value");
        insert_value_class.setSize(10);
        definition.safeput("insertValue", insert_value_class);

        StaticListClass insert_position_class = new StaticListClass();
        insert_position_class.setName("insertAtPosition");
        insert_position_class.setPrettyName("Insert Into List At");
        insert_position_class.setValues("----|beginning|end");
        definition.safeput("insertAtPosition", insert_position_class);
        
        return definition;
    }

    @Override
    public PropertyClassInterface getInstance() 
    {
    	return new SPARQListClass();
    }
}
