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

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.inject.Inject;

import org.xwiki.component.annotation.Component;
import org.xwiki.context.Execution;
import org.xwiki.context.ExecutionContext;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.WordBlock;
import org.xwiki.rendering.macro.AbstractMacro;
import org.xwiki.rendering.macro.MacroExecutionException;
import org.xwiki.rendering.transformation.MacroTransformationContext;

import com.objectsecurity.jena.Context;
import com.objectsecurity.xwiki.util.DocumentUtil;
import com.objectsecurity.xwiki.util.TableFormater;
import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.doc.XWikiDocument;

@Component("semPropTab")
public class SemanticPropertyTableMacro extends AbstractMacro<SemanticPropertyTableMacroParameters>{

	public SemanticPropertyTableMacro() {
		super("semPropTab", DESCRIPTION, SemanticPropertyTableMacroParameters.class);
		System.err.println();
		System.err.println();
		System.err.println("Semantic property table macro is being initialized...");
		System.err.println();
	}

	@Override
	public List<Block> execute(SemanticPropertyTableMacroParameters arg0,
			String arg1, MacroTransformationContext arg2)
			throws MacroExecutionException {

		ExecutionContext ectx = execution.getContext();
		XWikiContext context = (XWikiContext) ectx.getProperty("xwikicontext");
		XWikiDocument doc = context.getDoc();
		String name = DocumentUtil.computeFullDocName(doc.getDocumentReference());
		Context ctx = Context.getInstance();
		Vector<Vector<String>> vtab = null;
		try {
			ctx.begin();
			vtab = ctx.getPropertyTableForResource(name);
			ctx.commit();
		}
		catch (Exception ex) {
			ctx.abort();
			ArrayList<Block> l = new ArrayList<Block>();
			l.add(new WordBlock("<property table operation aborded!>"));
			return l;
		}
		return TableFormater.format2(vtab, TableFormater.parseHeader("Property,Value"), arg0.getLinks());
	}

	@Override
	public boolean supportsInlineMode() {
		return true;
	}

	private static final String DESCRIPTION = "Semantic property table macro";
	
	@Inject
	private Execution execution;
}
