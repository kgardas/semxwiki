/**
 * Semantic XWiki Extension
 * Copyright (c) 2010, 2011, 2012, 2014 ObjectSecurity Ltd.
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
 * The research leading to these results has received funding
 * from the European Union Seventh Framework Programme (FP7/2007-2013)
 * under grant agreement No FP7-608142.
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component("semProp")
public class SemanticPropertyMacro extends AbstractMacro<SemanticPropertyMacroParameters> {

    public SemanticPropertyMacro() {
        super("semProp", DESCRIPTION, SemanticPropertyMacroParameters.class);
        logger.debug("Semantic property macro is being initialized...");
    }

    @Override
    public List<Block> execute(SemanticPropertyMacroParameters arg0,
                                   String arg1, MacroTransformationContext arg2)
        throws MacroExecutionException {

        ExecutionContext ectx = execution.getContext();
        XWikiContext context = (XWikiContext) ectx.getProperty("xwikicontext");
        XWikiDocument doc = context.getDoc();
        String ref = doc.getURL("view", context);
        String name = DocumentUtil.computeFullDocName(doc.getDocumentReference());
        Context ctx = Context.getInstance();
        String p = arg0.getValue();
        String m = arg0.getMode();
        Mode mode = Mode.MODIFY;
        if (m != null && m.equals("ADD")) {
            mode = Mode.ADD;
        }
        int pos = p.lastIndexOf("::");
        if (pos == -1)
            return new ArrayList<Block>();
        String property = p.substring(0, pos );
        logger.debug("property `" + property + "'");
        String property_value = p.substring(pos + 2);
        logger.debug("property_value `" + property_value + "'");
        int posD = property.lastIndexOf('/');
        int posH = property.lastIndexOf('#');
        pos = (posD < posH) ? posH : posD;
        String property_prefix = property.substring(0, pos + 1);
        String property_name = property.substring(pos + 1);
        logger.debug("prop prefix: `" + property_prefix + "'");
        logger.debug("prop name: `" + property_name + "'");
        try {
            ctx.begin();
            if (property_value.equals("<this>"))
                property_value = ref;
            ctx.setProperty(name, property_prefix, property_name, property_value, mode);
            ctx.commit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
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

    private static final String DESCRIPTION = "Semantic property macro";
	
    @Inject
    private Execution execution;
    private static final Logger logger = LoggerFactory.getLogger(SemanticPropertyMacro.class);
}
