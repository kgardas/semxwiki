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
package com.objectsecurity.xwiki.util;

import java.util.Iterator;
import java.util.List;

import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.SpaceReference;

public class DocumentUtil {
	
	public static String computeFullDocName(DocumentReference ref) {
		List<SpaceReference> spaces = ref.getSpaceReferences();
		String retval = "";
		for (Iterator<SpaceReference> it = spaces.iterator(); it.hasNext(); ) {
			SpaceReference sr = it.next();
			retval = retval + sr.getName() + ".";
		}
		retval = retval + ref.getName();
		return retval;
	}

}
