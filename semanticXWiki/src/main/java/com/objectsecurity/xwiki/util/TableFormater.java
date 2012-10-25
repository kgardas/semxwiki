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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.LinkBlock;
import org.xwiki.rendering.block.TableBlock;
import org.xwiki.rendering.block.TableCellBlock;
import org.xwiki.rendering.block.TableHeadCellBlock;
import org.xwiki.rendering.block.TableRowBlock;
import org.xwiki.rendering.block.WordBlock;
import org.xwiki.rendering.listener.reference.ResourceReference;
import org.xwiki.rendering.listener.reference.ResourceType;

import com.objectsecurity.jena.PairNameLink;
import com.objectsecurity.xwiki.SemanticQueryMacro;

public class TableFormater {

	static public List<Block> format_table(Vector<Vector<PairNameLink>> values, String[] header, boolean links) {
		ArrayList<Block> block_table = new ArrayList<Block>();
		ArrayList<Block> block_head = new ArrayList<Block>();
		for (int i = 0; i < header.length; i++) {
			ArrayList<Block> head_cell = new ArrayList<Block>();
			head_cell.add(new WordBlock(header[i]));
			block_head.add(new TableHeadCellBlock(head_cell, new HashMap<String, String>()));
		}
		block_table.add(new TableRowBlock(block_head, new HashMap<String, String>()));
		for (int i = 0; i < values.size(); i++) {
			Vector<PairNameLink> row = values.elementAt(i);
			ArrayList<Block> block_row = new ArrayList<Block>(); 
			for (int j = 0; j < row.size(); j++) {
				ArrayList<Block> cell = new ArrayList<Block>();
				if (links) {
					ArrayList<Block> tmp = new ArrayList<Block>();
					tmp.add(new WordBlock(row.elementAt(j).name));
					ResourceReference rref = null;
					//if (j == 0)
					if (row.elementAt(j).link == null || row.elementAt(j).link.equals("")) {
						//rref = new ResourceReference(row.elementAt(j).name.replace("#", "%23"), ResourceType.URL);
						String remapped_link = SymbolMapper.transform(row.elementAt(j).name, SymbolMapper.MappingDirection.PHYSICAL_URL_TO_XWIKI_URL, SymbolMapper.MappingStrategy.SYMBOLIC_NAME_TRANSLATION);
						ResourceType type;
						if (remapped_link.startsWith("/"))
							type = ResourceType.URL;
						else
							type = ResourceType.DOCUMENT;
						rref = new ResourceReference(remapped_link, type);
					}
					else {
						//rref = new ResourceReference(row.elementAt(j).link.replace("#", "%23"), ResourceType.URL);
						String remapped_link = SymbolMapper.transform(row.elementAt(j).link, SymbolMapper.MappingDirection.PHYSICAL_URL_TO_XWIKI_URL, SymbolMapper.MappingStrategy.SYMBOLIC_NAME_TRANSLATION);
						ResourceType type;
						if (remapped_link.startsWith("/"))
							type = ResourceType.URL;
						else
							type = ResourceType.DOCUMENT;
						rref = new ResourceReference(remapped_link, type);
					}
					//else
					//rref = new ResourceReference(row.elementAt(j), ResourceType.PATH);
					System.err.println("rref : " + rref.toString());
					cell.add(new LinkBlock(tmp, rref, true));
				}
				else {
					cell.add(new WordBlock(row.elementAt(j).name));
				}
				block_row.add(new TableCellBlock(cell, new HashMap<String, String>()));
			}
			block_table.add(new TableRowBlock(block_row, new HashMap<String, String>()));
		}
		TableBlock table = new TableBlock(block_table, new HashMap<String, String>());
		ArrayList<Block> retval = new ArrayList<Block>();
		retval.add(table);
		return retval;
	}

	static public List<Block> format_line(Vector<Vector<PairNameLink>> values, String[] header, boolean links) {
		System.err.println("TableFormater: format_line");
		ArrayList<Block> line = new ArrayList<Block>();
		for (int i = 0; i < values.size(); i++) {
			Vector<PairNameLink> row = values.elementAt(i);
			//ArrayList<Block> block_row = new ArrayList<Block>(); 
			for (int j = 0; j < row.size(); j++) {
				//ArrayList<Block> cell = new ArrayList<Block>();
				if (links) {
					ArrayList<Block> tmp = new ArrayList<Block>();
					tmp.add(new WordBlock(row.elementAt(j).name));
					ResourceReference rref = null;
					//if (j == 0)
					if (row.elementAt(j).link == null || row.elementAt(j).link.equals("")) {
						//rref = new ResourceReference(row.elementAt(j).name.replace("#", "%23"), ResourceType.URL);
						String remapped_link = SymbolMapper.transform(row.elementAt(j).name, SymbolMapper.MappingDirection.PHYSICAL_URL_TO_XWIKI_URL, SymbolMapper.MappingStrategy.SYMBOLIC_NAME_TRANSLATION);
						ResourceType type;
						if (remapped_link.startsWith("/"))
							type = ResourceType.URL;
						else
							type = ResourceType.DOCUMENT;
						rref = new ResourceReference(remapped_link, type);
					}
					else {
						//rref = new ResourceReference(row.elementAt(j).link.replace("#", "%23"), ResourceType.URL);
						String remapped_link = SymbolMapper.transform(row.elementAt(j).link, SymbolMapper.MappingDirection.PHYSICAL_URL_TO_XWIKI_URL, SymbolMapper.MappingStrategy.SYMBOLIC_NAME_TRANSLATION);
						ResourceType type;
						if (remapped_link.startsWith("/"))
							type = ResourceType.URL;
						else
							type = ResourceType.DOCUMENT;
						rref = new ResourceReference(remapped_link, type);
					}
					//else
					//rref = new ResourceReference(row.elementAt(j), ResourceType.PATH);
					System.err.println("rref : " + rref.toString());
					line.add(new LinkBlock(tmp, rref, true));
				}
				else {
					line.add(new WordBlock(row.elementAt(j).name));
				}
				if (row.size() > j + 1 || values.size() > i + 1)
					line.add(new WordBlock(", "));
			}
		}
		System.err.println("formated line: " + line.toString());
		return line;
	}

	static public List<Block> format(Vector<Vector<PairNameLink>> values, String[] header, boolean links, SemanticQueryMacro.Mode mode) {
		if (mode == SemanticQueryMacro.Mode.LINE)
			return format_line(values, header, links);
		return format_table(values, header, links);
	}
	
	static public List<Block> format2(Vector<Vector<String>> values, String[] header, boolean links) {
		Vector<Vector<PairNameLink>> arg = new Vector<Vector<PairNameLink>>();
		Iterator<Vector<String>> i = values.iterator();
		while (i.hasNext()) {
			Vector<String> x = i.next();
			Iterator<String> j = x.iterator();
			Vector<PairNameLink> row = new Vector<PairNameLink>();
			while (j.hasNext()) {
				row.add(new PairNameLink(j.next(), null));
			}
			arg.add(row);
		}
		return format(arg, header, links, SemanticQueryMacro.Mode.TABLE);
	}

	static public String[] parseHeader(String header) {
		if (header == null || header.equals(""))
			return new String[0];
		System.err.println("header `" + header + "'");
		StringTokenizer st = new StringTokenizer(header, ",");
		Vector<String> vheader = new Vector<String>();
		while (st.hasMoreTokens()) {
			vheader.add(st.nextToken());
		}
		for (int i = 0; i < vheader.size(); i++) {
			System.err.println("header element: `" + vheader.elementAt(i) + "'");
		}
		return vheader.toArray(new String[] {});
	}
}
