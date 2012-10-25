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

import com.hp.hpl.jena.query.QueryParseException;
import com.objectsecurity.jena.Context;
import com.objectsecurity.jena.PairNameLink;
import com.objectsecurity.xwiki.util.DocumentUtil;
import com.objectsecurity.xwiki.util.SymbolMapper;
import com.objectsecurity.xwiki.util.TableFormater;
import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.doc.XWikiDocument;

@Component("semQuery")
public class SemanticQueryMacro extends AbstractMacro<SemanticQueryMacroParameters>{

	public enum Mode {
		TABLE,
		LINE
	}
	
	public SemanticQueryMacro() {
		super("semQuery", DESCRIPTION, SemanticQueryMacroParameters.class);
		System.err.println();
		System.err.println();
		System.err.println("Semantic query macro is being initialized...");
		System.err.println();
	}

	@Override
	public List<Block> execute(SemanticQueryMacroParameters arg0,
			String arg1, MacroTransformationContext arg2)
			throws MacroExecutionException {

		ExecutionContext ectx = execution.getContext();
		XWikiContext context = (XWikiContext) ectx.getProperty("xwikicontext");
		XWikiDocument doc = context.getDoc();
		String name = DocumentUtil.computeFullDocName(doc.getDocumentReference());
		String res = SymbolMapper.transform(name, SymbolMapper.MappingDirection.XWIKI_URL_TO_PHYSICAL_URL, SymbolMapper.MappingStrategy.SYMBOLIC_NAME_TRANSLATION);
		Context ctx = Context.getInstance();
		String queryToProcess = arg0.getQuery();
		String query = "";
		if (queryToProcess.indexOf("<this>") != -1) {
			query = queryToProcess.replace("<this>", /*"<" +*/ res /*+ ">"*/);
		}
		else {
			query = queryToProcess;
		}
		String[] header = TableFormater.parseHeader(arg0.getHeader());
		String[] linksAttrs = TableFormater.parseHeader(arg0.getLinksAttrs());
		String[] linksValuesRemapping = TableFormater.parseHeader(arg0.getLinksValuesRemapping());
		Vector<Vector<PairNameLink>> vtab = null;
		try {
			ctx.begin();
			System.err.println("query `" + query + "'");
			vtab = ctx.query(query, header, linksAttrs, linksValuesRemapping);
			ctx.commit();
		}
		catch (QueryParseException ex) {
			System.err.println("query parse exception: " + ex.getMessage());
			ctx.abort();
			ArrayList<Block> l = new ArrayList<Block>();
			l.add(new WordBlock("query aborted due to parse error: " + ex.getMessage()));
			return l;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			ctx.abort();
			ArrayList<Block> l = new ArrayList<Block>();
			l.add(new WordBlock("<query operation aborded!>"));
			return l;
		}
		if (arg0.getUndefined() != null) {
			String undefinedLink = "";
			if (arg0.getUndefinedLink() != null)
				undefinedLink = arg0.getUndefinedLink();
			Vector<PairNameLink> t = new Vector<PairNameLink>();
			t.add(new PairNameLink(arg0.getUndefined(), undefinedLink));
			vtab.insertElementAt(t, 0);
		}
		return TableFormater.format(vtab, header, arg0.getLinks(), arg0.getMode());
	}

	@Override
	public boolean supportsInlineMode() {
		return true;
	}

	private static final String DESCRIPTION = "Semantic query macro";
	
	@Inject
	private Execution execution;
}
