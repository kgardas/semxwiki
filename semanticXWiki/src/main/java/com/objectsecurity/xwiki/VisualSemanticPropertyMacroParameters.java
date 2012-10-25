/**
 * Semantic XWiki Extension
 * Copyright (c) 2010, 2011, 2012 ObjectSecurity Ltd.
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
 *
 * The research leading to these results has received funding
 * from the European Union Seventh Framework Programme (FP7/2007-2013)
 * under grant agreement No FP7-242474.
 *
 * Written by Karel Gardas, <kgardas@objectsecurity.com>
 */
package com.objectsecurity.xwiki;

import org.xwiki.properties.annotation.PropertyMandatory;
import org.xwiki.properties.annotation.PropertyDescription;

public class VisualSemanticPropertyMacroParameters {

	public String getValue() {
		return this.value;
	}
	
	@PropertyMandatory
	@PropertyDescription("Visual semantic property value")
	public void setValue(String value) {
		this.value = value;
	}
	
	public boolean getLinks() {
		return this.links;
	}
	
	@PropertyDescription("Visual semantic property links")
	public void setLinks(boolean links) {
		this.links = links;
	}

	public String getLinkAttr() {
		return this.linkAttr;
	}
	
	@PropertyDescription("Set semantic property link attribute")
	public void setLinkAttr(String l) {
		this.linkAttr = l;
	}
	
	public String getLinkRes() {
		return this.linkRes;
	}
	
	@PropertyDescription("Set semantic property link resource")
	public void setLinkRes(String r) {
		this.linkRes = r;
	}
	
	public String getAutoDelete() {
		return this.autoDelete;
	}
	
	@PropertyDescription("Semantic property auto delete value")
	public void setAutoDelete(String value) {
		this.autoDelete = value;
	}

	public String getMode() {
		return this.mode;
	}
	
	@PropertyDescription("Semantic property mode value")
	public void setMode(String value) {
		this.mode = value;
	}

	private String value;
	private boolean links = false;
	private String linkAttr;
	private String linkRes;
	private String autoDelete;
	private String mode;
}
