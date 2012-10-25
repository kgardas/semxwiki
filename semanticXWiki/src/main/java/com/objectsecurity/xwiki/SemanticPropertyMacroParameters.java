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

public class SemanticPropertyMacroParameters {

	public String getValue() {
		return this.value;
	}
	
	@PropertyMandatory
	@PropertyDescription("Semantic property value")
	public void setValue(String value) {
		this.value = value;
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

	public String getDelimiter() {
		return this.delimiter;
	}

	@PropertyDescription("Semantic property delimiter value")
	public void setDelimiter(String value) {
		this.delimiter = value;
	}

	private String value;
	private String autoDelete;
	private String mode;
	private String delimiter;
}
