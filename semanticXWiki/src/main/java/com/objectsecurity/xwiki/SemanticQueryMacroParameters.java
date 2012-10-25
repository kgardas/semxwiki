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

public class SemanticQueryMacroParameters {

	public String getQuery() {
		return this.query;
	}
	
	@PropertyMandatory
	@PropertyDescription("semantic query query")
	public void setQuery(String query) {
		this.query = query;
	}
	
	public boolean getLinks() {
		return this.links;
	}
	
	@PropertyDescription("semantic query table links")
	public void setLinks(boolean links) {
		this.links = links;
	}

	public String getHeader() {
		return this.header;
	}
	
	@PropertyMandatory
	@PropertyDescription("semantic query table header")
	public void setHeader(String header) {
		this.header = header;
	}

	public String getLinksAttrs() {
		return this.linksAttrs;
	}
	
	@PropertyDescription("semantic query table links attribute")
	public void setLinksAttrs(String linksAttr) {
		this.linksAttrs = linksAttr;
	}

	public String getLinksValuesRemapping() {
		return this.linksValuesRemapping;
	}
	
	@PropertyDescription("semantic query table links values remapping")
	public void setLinksValuesRemapping(String values) {
		this.linksValuesRemapping = values;
	}

	public SemanticQueryMacro.Mode getMode() {
		return this.mode;
	}
	
	@PropertyDescription("semantic query mode")
	public void setMode(SemanticQueryMacro.Mode mode) {
		this.mode = mode;
	}

	public String getUndefined() {
		return this.undefined;
	}
	
	@PropertyDescription("semantic query undefined string")
	public void setUndefined(String undefined) {
		this.undefined = undefined;
	}

	public String getUndefinedLink() {
		return this.undefinedLink;
	}
	
	@PropertyDescription("semantic query undefined link")
	public void setUndefinedLink(String undefinedLink) {
		this.undefinedLink = undefinedLink;
	}

	private String query;
	private boolean links = false;
	private String header;
	private String linksAttrs;
	private String linksValuesRemapping;
	private SemanticQueryMacro.Mode mode;
	private String undefined;
	private String undefinedLink;
}
