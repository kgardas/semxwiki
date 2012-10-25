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

public class SymbolMapperTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] test = {
				"https://www.esa.org/bla#bleh",
				"https://www.esa.org/bla/bleh",
				"www.esa.org/bla#bleh",
				"www.esa.org/bla/bleh",
				};
		if (args.length == 0) {
		for (int i = 0; i < test.length; i++) {
			System.out.println(test[i] + " -> " + SymbolMapper.transform(test[i], SymbolMapper.MappingDirection.PHYSICAL_URL_TO_XWIKI_URL, SymbolMapper.MappingStrategy.SYMBOLIC_NAME_TRANSLATION));
			System.out.println(SymbolMapper.transform(SymbolMapper.transform(test[i], SymbolMapper.MappingDirection.PHYSICAL_URL_TO_XWIKI_URL, SymbolMapper.MappingStrategy.SYMBOLIC_NAME_TRANSLATION), SymbolMapper.MappingDirection.XWIKI_URL_TO_PHYSICAL_URL, SymbolMapper.MappingStrategy.SYMBOLIC_NAME_TRANSLATION).equals(test[i]));
		}
		}
		else {
			for (int i = 0; i < args.length; i++) {
				System.out.println(args[i] + " -> " + SymbolMapper.transform(args[i], SymbolMapper.MappingDirection.PHYSICAL_URL_TO_XWIKI_URL, SymbolMapper.MappingStrategy.SYMBOLIC_NAME_TRANSLATION));
				System.out.println(SymbolMapper.transform(SymbolMapper.transform(args[i], SymbolMapper.MappingDirection.PHYSICAL_URL_TO_XWIKI_URL, SymbolMapper.MappingStrategy.SYMBOLIC_NAME_TRANSLATION), SymbolMapper.MappingDirection.XWIKI_URL_TO_PHYSICAL_URL, SymbolMapper.MappingStrategy.SYMBOLIC_NAME_TRANSLATION).equals(args[i]));
			}
		}
		//System.out.println(SymbolMapper.transform("/NextGenRE/XWiki/Test", SymbolMapper.MappingDirection.XWIKI_URL_TO_PHYSICAL_URL, SymbolMapper.MappingStrategy.SYMBOLIC_NAME_TRANSLATION));
	}
}
