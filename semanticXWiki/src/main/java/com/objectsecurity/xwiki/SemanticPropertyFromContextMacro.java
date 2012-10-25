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
import org.xwiki.rendering.block.WordBlock;
import org.xwiki.rendering.macro.AbstractMacro;
import org.xwiki.rendering.macro.MacroExecutionException;
import org.xwiki.rendering.transformation.MacroTransformationContext;

import com.objectsecurity.jena.Context;
import com.objectsecurity.jena.Context.Mode;
import com.objectsecurity.xwiki.util.DocumentUtil;
import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.doc.XWikiDocument;

@Component("semPropFromCtx")
public class SemanticPropertyFromContextMacro extends AbstractMacro<SemanticPropertyMacroParameters>{

	public SemanticPropertyFromContextMacro() {
		super("semProp", DESCRIPTION, SemanticPropertyMacroParameters.class);
		System.err.println();
		System.err.println();
		System.err.println("Semantic property macro from xwiki context is being initialized...");
		System.err.println();
	}

	@Override
	public List<Block> execute(SemanticPropertyMacroParameters arg0,
			String arg1, MacroTransformationContext arg2)
			throws MacroExecutionException {
		System.err.println("is new code really executed?");
		ExecutionContext ectx = execution.getContext();
		XWikiContext context = (XWikiContext) ectx.getProperty("xwikicontext");
		XWikiDocument doc = context.getDoc();
		String name = DocumentUtil.computeFullDocName(doc.getDocumentReference());
		Context ctx = Context.getInstance();
		String p = arg0.getValue();
		String m = arg0.getMode();
	    Mode mode = Mode.MODIFY;
	    if (m != null && m.equals("ADD")) {
	    	mode = Mode.ADD;
	    }
		System.err.println("semantic_property_value: `" + p + "'");
		if (p == null)
			return new ArrayList<Block>();
		int pos = p.lastIndexOf("::");
		if (pos == -1)
			return new ArrayList<Block>();					
		String property = p.substring(0, pos );
		System.err.println("property `" + property + "'");
		String ctx_property_value = p.substring(pos + 2);
		System.err.println("ctx_property_value `" + ctx_property_value + "'");
		Object o = context.get(ctx_property_value);
		System.err.println("property_value `" + o + "'");
		int posD = property.lastIndexOf('/');
		int posH = property.lastIndexOf('#');
		pos = (posD < posH) ? posH : posD;
		String property_prefix = property.substring(0, pos + 1);
		String property_name = property.substring(pos + 1);
		System.err.println("prop prefix: `" + property_prefix + "'");
		System.err.println("prop name: `" + property_name + "'");
		try {
			ctx.begin();
			if (o instanceof String) {
				String property_value = (String)o;
				ctx.setProperty(name, property_prefix, property_name, property_value, mode);
			}
			else if (o instanceof ArrayList) {
				@SuppressWarnings("unchecked")
				ArrayList<String> arr = (ArrayList<String>)o;
				String[] property_values = arr.toArray(new String[0]);
				for (int i = 0; i < property_values.length; i++) {
					System.err.println("adding property value (array!) : " + property_values[i]);
					ctx.setProperty(name, property_prefix, property_name, property_values[i], mode);
					if (i == 0) {
						// after first value is set we switch to ADD mode to be able to add another values
						// and still preserve previous values
						mode = Mode.ADD;
					}
				}
			}
			else
				throw new RuntimeException("Unsupported property value type!");
			ctx.commit();
		}
		catch (Exception ex) {
			ctx.abort();
			ArrayList<Block> l = new ArrayList<Block>();
			l.add(new WordBlock("<set property operation aborded!>"));
			return l;
		}
		return new ArrayList<Block>();
	}

	@Override
	public boolean supportsInlineMode() {
		return true;
	}

	private static final String DESCRIPTION = "Semantic property from context macro";
	
	@Inject
	private Execution execution;
}
