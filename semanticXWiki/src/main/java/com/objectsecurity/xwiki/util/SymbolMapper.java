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
 * Partially funded by the European Space Agengy as part of contract
 * 4000101353 / 10 / NL / SFe
 *
 * Written by Karel Gardas, <kgardas@objectsecurity.com>
 */
package com.objectsecurity.xwiki.util;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class SymbolMapper {
	
    public enum MappingStrategy {
        SYMBOLIC_NAME_TRANSLATION
    }
	
    public enum MappingDirection {
        PHYSICAL_URL_TO_XWIKI_URL,
        XWIKI_URL_TO_PHYSICAL_URL
    }
	
    public static void initMaps() {
        // need to fill the map
        String[] keys = {".", ":", " ", "/", "_", "#"};
        String[] vals = {"dot", "colon", "space", "slash", "underscore", "hash"};
        phys_to_xwiki_map = new HashMap<String, String>();
        xwiki_to_phys_map = new HashMap<String, String>();
        for (int i = 0; i < keys.length; i++) {
            phys_to_xwiki_map.put(keys[i], vals[i]);
            xwiki_to_phys_map.put(vals[i], keys[i]);
        }
    }

    public static String transform(String value, MappingDirection direction, MappingStrategy strategy) {
        if (strategy == MappingStrategy.SYMBOLIC_NAME_TRANSLATION) {
            if (direction == MappingDirection.PHYSICAL_URL_TO_XWIKI_URL) {
                if (phys_to_xwiki_map == null) {
                    initMaps();
                }
                StringBuffer buf = new StringBuffer();
                System.err.println("SymbolMapper: physical to xwiki name translation using symbolic name strategy");
                System.err.println("SymbolMapper: translating: " + value);
                // do we need translation at all?
                if (!(value.startsWith("http")
                      || value.startsWith("www"))) {
                    System.err.println("SymbolMapper: translation not needed, returning: " + value);
                    return value;
                }
                StringTokenizer st = new StringTokenizer(value, ".: /_#", true);
                while (st.hasMoreElements()) {
                    String elem = st.nextToken();
                    if (phys_to_xwiki_map.containsKey(elem)) {
                        buf.append("_");
                        buf.append(phys_to_xwiki_map.get(elem));
                        buf.append("_");
                    }
                    else {
                        buf.append(elem);
                    }
                }
                // now we need to transform last _slash_ or _hash_ into dot.
                // E.g. https://www.esa.org/bla/bleh -- bleh is name of page, while
                // from  https://www.esa.org/bla we form space name
                String tmp = buf.toString();
                System.err.println("SymbolMapper: buf: " + tmp);
                int spos = tmp.lastIndexOf("_slash_");
                int hpos = tmp.lastIndexOf("_hash_");
                int pos = (spos > hpos) ? spos : hpos;
                System.err.println("spos: " + spos);
                System.err.println("hpos: " + hpos);
                System.err.println("pos: " + pos);
                String retval = "";
                if (pos != -1) {
                    String space = tmp.substring(0, pos);
                    String page = tmp.substring(pos + 7);
                    retval = space + "." + page;
                }
                else {
                    retval = tmp;
                }
                System.err.println("SymbolMapper: -> " + retval);
                return retval;
            }
            else {
                System.err.println("SymbolMapper: xwiki to physical name translation using symbolic name strategy");
                System.err.println("SymbolMapper: translating: " + value);
                if (!(value.contains("_slash_")
                      ||value.contains("_hash_"))) {
                    System.err.println("SymbolMapper: translation not needed, returning: " + value);
                    return value;
                }
                // first we need to translate dot dividing name into space and page name
                String nv = value.replace('.', '/');
                StringBuffer buf = new StringBuffer();
                StringTokenizer st = new StringTokenizer(nv, "_", false);
                while (st.hasMoreElements()) {
                    String elem = st.nextToken();
                    if (xwiki_to_phys_map == null) {
                        initMaps();
                    }
                    if (xwiki_to_phys_map.containsKey(elem)) {
                        buf.append(xwiki_to_phys_map.get(elem));
                    }
                    else {
                        buf.append(elem);
                    }
                }
                System.err.println("SymbolMapper: -> " + buf.toString());
                return buf.toString();
            }
        }
        throw new RuntimeException("UNIMPLEMENTED SYMBOL MAPPING STRATEGY!");
    }
		
    static Map<String, String> phys_to_xwiki_map;
    static Map<String, String> xwiki_to_phys_map;
}
