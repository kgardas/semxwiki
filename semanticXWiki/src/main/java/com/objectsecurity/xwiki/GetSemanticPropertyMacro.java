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

import javax.inject.Inject;

import org.xwiki.component.annotation.Component;
import org.xwiki.context.Execution;
import org.xwiki.context.ExecutionContext;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.LinkBlock;
import org.xwiki.rendering.block.WordBlock;
import org.xwiki.rendering.listener.reference.ResourceReference;
import org.xwiki.rendering.listener.reference.ResourceType;
import org.xwiki.rendering.macro.AbstractMacro;
import org.xwiki.rendering.macro.MacroExecutionException;
import org.xwiki.rendering.transformation.MacroTransformationContext;

import com.objectsecurity.jena.Context;
import com.objectsecurity.xwiki.util.DocumentUtil;
import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.doc.XWikiDocument;

@Component("getSemProp")
public class GetSemanticPropertyMacro extends AbstractMacro<GetSemanticPropertyMacroParameters>{

	public GetSemanticPropertyMacro() {
		super("getSemProp", DESCRIPTION, GetSemanticPropertyMacroParameters.class);
		System.err.println();
		System.err.println();
		System.err.println("Get semantic property macro is being initialized...");
		System.err.println();
	}

	@Override
	public List<Block> execute(GetSemanticPropertyMacroParameters arg0,
			String arg1, MacroTransformationContext arg2)
			throws MacroExecutionException {

		ExecutionContext ectx = execution.getContext();
		XWikiContext context = (XWikiContext) ectx.getProperty("xwikicontext");
		XWikiDocument doc = context.getDoc();
		String ref = doc.getURL("view", context);
		String name = DocumentUtil.computeFullDocName(doc.getDocumentReference());
		Context ctx = Context.getInstance();
		String p = arg0.getValue();
		String linkAttr = arg0.getLinkAttr();
		String linkRes = arg0.getLinkRes();
		String resLink = null;
		boolean links = arg0.getLinks();
		int pos = p.lastIndexOf("/");
		if (pos == -1)
			return new ArrayList<Block>();
		String property = p;
		int posD = property.lastIndexOf('/');
		int posH = property.lastIndexOf('#');
		pos = (posD < posH) ? posH : posD;
		String property_prefix = property.substring(0, pos + 1);
		String property_name = property.substring(pos + 1);
		System.err.println("prop prefix: `" + property_prefix + "'");
		System.err.println("prop name: `" + property_name + "'");
		String property_value = null;
		try {
			ctx.begin();
			property_value = ctx.getProperty(name, property_prefix, property_name);
			if (linkAttr != null && !linkAttr.equals("")) {
				resLink = ctx.getProperty(ref, property_prefix, linkAttr);
			}
			if (linkRes != null && !linkRes.equals("")) {
				resLink = linkRes;				
			}
			ctx.commit();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			ctx.abort();
			ArrayList<Block> l = new ArrayList<Block>();
			l.add(new WordBlock("<get property operation aborded!>"));
			return l;
		}
		ArrayList<Block> l = new ArrayList<Block>();
		if (!links) {
			l.add(new WordBlock(property_value));
		}
		else {
			ArrayList<Block> tmp = new ArrayList<Block>();
			tmp.add(new WordBlock(property_value));
			ResourceReference rref = null;
			if (resLink == null)
				rref = new ResourceReference(property_value, ResourceType.URL);
			else
				rref = new ResourceReference(resLink, ResourceType.URL);
			l.add(new LinkBlock(tmp, rref, true));
		}
		return l;
	}

	@Override
	public boolean supportsInlineMode() {
		return true;
	}

	private static final String DESCRIPTION = "Get semantic property macro";
	
	@Inject
	private Execution execution;
}
