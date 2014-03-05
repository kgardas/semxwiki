/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
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
 */

package com.objectsecurity.xwiki.objects.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.xhtml.input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xpn.xwiki.XWiki;
import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.objects.BaseCollection;
import com.xpn.xwiki.objects.BaseProperty;
import com.xpn.xwiki.objects.ListProperty;
import com.xpn.xwiki.objects.classes.ListClass;
import com.xpn.xwiki.objects.classes.ListItem;
import com.xpn.xwiki.objects.meta.PropertyMetaClass;
//import com.xpn.xwiki.plugin.query.QueryPlugin;

public class SPARQListClass extends ListClass
{
    protected static final String DEFAULT_QUERY = "//DEFAULT SPARQ QUERY";

    private static final Logger LOG = LoggerFactory.getLogger(SPARQListClass.class);

    private List<ListItem> cachedSPARQList;
    
    /** a dummy class to access the list comparators from XWiki core */
    private static class DummyListItem extends ListItem {
    	// do not instanciate
    	private DummyListItem(String id) {
    		super(id);
		}

		static final Comparator<ListItem> ID_COMPARATOR = ListItem.ID_COMPARATOR;
		static final Comparator<ListItem> VALUE_COMPARATOR = ListItem.VALUE_COMPARATOR;
    }

    public SPARQListClass(String name, String prettyname, PropertyMetaClass wclass)
    {
        super(name, prettyname, wclass);
        System.err.println("SPARQListClass::CTOR 1");
    }

    public SPARQListClass(PropertyMetaClass wclass)
    {
        super("sparqlist", "SPARQ List", wclass);
        System.err.println("SPARQListClass::CTOR 1");
    }

    public SPARQListClass()
    {
        this(null);
        System.err.println("SPARQListClass::CTOR 1");
    }

    public List<ListItem> makeList(List<Object> list)
    {
        System.err.println("SPARQListClass::makeList: " + list);
        List<ListItem> result = new ArrayList<ListItem>();
        for (Object item : list) {
            // Oracle databases treat NULL and empty strings similarly. Thus the list passed
            // as parameter can have some elements being NULL (for XWiki string properties which
            // were empty strings). This means we need to check for NULL and ignore NULL entries
            // from the list.
            if (item != null) {
                if (item instanceof String) {
                    result.add(new ListItem((String) item));
                } else {
                    Object[] res = (Object[]) item;
                    if (res.length == 1) {
                        result.add(new ListItem(res[0].toString()));
                    } else if (res.length == 2) {
                        result.add(new ListItem(res[0].toString(), res[1].toString()));
                    } else {
                        result.add(new ListItem(res[0].toString(), res[1].toString(), res[2].toString()));
                    }
                }
            }
        }
        return result;
    }

    public List<ListItem> getSPARQList(XWikiContext context)
    {
        System.err.println("SPARQListClass::getSPARQList: " + context);
        List<ListItem> list = getCachedSPARQList(context);
        String insertedValue = getInsertValue();
        String insertAtPosition = getInsertAtPosition();
        System.out.println("insertedValue: `" + insertedValue + "'");
        System.out.println("insertAtPosition: `" + insertAtPosition + "'");
        if (list == null) {
            XWiki xwiki = context.getWiki();
            String query = getQuery(context);

            if (query == null) {
                list = new ArrayList<ListItem>();
            } else {
                try {
                    // we use reflection to get into O/S Jena's Context class
                    Class c = Class.forName("com.objectsecurity.jena.Context");
                    Method m = c.getMethod("getInstance", null);
                    Object ctx = m.invoke(null, null);
                    m = c.getMethod("query", new Class[] {String.class, String[].class});
                    Object r = m.invoke(ctx, query, new String[0]);
                    System.out.println("result of query: " + r.toString());
                    System.out.println("result of query: " + r.getClass().toString());
                    Vector vec = (Vector)r;
                    list = new ArrayList<ListItem>();
                    for (int i = 0; i < vec.size(); i++) {
                        String name = (String)((Vector)vec.elementAt(i)).elementAt(0);
                        System.out.println("add name: " + name);
                        list.add(new ListItem(name));
                    }
                    // if ((xwiki.getHibernateStore() != null) && (!query.startsWith("/"))) {
                    //     list = makeList(xwiki.search(query, context));
                    // } else {
                    //     list = makeList(((QueryPlugin) xwiki.getPlugin("query", context)).xpath(query).list());
                    // }
                } catch (Exception e) {
                    e.printStackTrace();
                    list = new ArrayList<ListItem>();
                }
            }
            if (insertAtPosition != null &&
                !(insertAtPosition.equals("") || insertAtPosition.equals("----"))) {
                if (insertAtPosition.equals("beginning")) {
                    list.add(0, new ListItem(insertedValue));
                }
                else if (insertAtPosition.equals("end")) {
                    list.add(new ListItem(insertedValue));
                }
                else {
                    throw new RuntimeException("Wrong insert position in SPARQ list: " + insertAtPosition);
                }
            }
            setCachedSPARQList(list, context);
        }
        System.out.print("list: [");
        for (int i = 0; i < list.size(); i++) {
            System.out.print("'" + list.get(i).toString() + "',");
        }
        System.out.println("]");
        return list;
    }

    @Override
    public List<String> getList(XWikiContext context)
    {
        System.err.println("SPARQListClass::getList: " + context);
        List<ListItem> dblist = getSPARQList(context);

        String sort = getSort();

        if ("id".equals(sort)) {
            Collections.sort(dblist, DummyListItem.ID_COMPARATOR);
        } else if ("value".equals(sort)) {
            Collections.sort(dblist, DummyListItem.VALUE_COMPARATOR);
        }

        List<String> result = new ArrayList<String>(dblist.size());
        for (ListItem value : dblist) {
            result.add(value.getId());
        }
        return result;
    }

    @Override
    public Map<String, ListItem> getMap(XWikiContext context)
    {
        System.err.println("SPARQListClass::getMap: " + context);
        List<ListItem> list = getSPARQList(context);
        Map<String, ListItem> result = new HashMap<String, ListItem>();
        if ((list == null) || (list.size() == 0)) {
            return result;
        }
        for (int i = 0; i < list.size(); i++) {
            Object res = list.get(i);
            if (res instanceof String) {
                result.put((String) res, new ListItem((String) res));
            } else {
                ListItem item = (ListItem) res;
                result.put(item.getId(), item);
            }
        }
        return result;
    }

    /**
     * <p>
     * Computes the query corresponding to the current XProperty. The query is either manually specified by the XClass
     * creator in the <tt>sql</tt> field, or, if the query field is blank, constructed using the <tt>classname</tt>,
     * <tt>idField</tt> and <tt>valueField</tt> properties. The query is constructed according to the following rules:
     * </p>
     * <ul>
     * <li>If no classname, id or value fields are selected, return a query that return no rows.</li>
     * <li>If only the classname is provided, select all document names which have an object of that type.</li>
     * <li>If only one of id and value is provided, select just one column.</li>
     * <li>If id = value, select just one column.</li>
     * <li>If no classname is provided, assume the fields are document properties.</li>
     * <li>If the document is not used at all, don't put it in the query.</li>
     * <li>If the object is not used at all, don't put it in the query.</li>
     * </ul>
     * <p>
     * If there are two columns selected, use the first one as the stored value and the second one as the displayed
     * value.
     * </p>
     * 
     * @param context The current {@link XWikiContext context}.
     * @return The HQL query corresponding to this property.
     */
    public String getQuery(XWikiContext context)
    {
        System.err.println("SPARQListClass::getQuery: " + context);
        // First, get the hql query entered by the user.
        String sparq = getSparq();
        System.out.println("SPARQ Query: `" + sparq + "'");
        // Parse the query, so that it can contain velocity scripts, for example to use the
        // current document name, or the current username.
        // try {
        //     sparq = context.getWiki().parseContent(sparq, context);
        // } catch (Exception e) {
        //     LOG.error("Failed to parse SPARQ script [" + sparq + "]. Continuing with non-rendered script.", e);
        // }
        return sparq;
    }

    public String getSparq()
    {
        System.err.println("SPARQListClass::getSparq()");
        return getLargeStringValue("sparq");
    }

    public void setSparq(String sparq)
    {
        System.err.println("SPARQListClass::setSparq: " + sparq);
        setLargeStringValue("sparq", sparq);
    }

    public String getClassname()
    {
        System.err.println("SPARQListClass::getClassname");
        return getStringValue("classname");
    }

    public void setClassname(String classname)
    {
        System.err.println("SPARQListClass::setClassname: " + classname);
        setStringValue("classname", classname);
    }

    public String getIdField()
    {
        System.err.println("SPARQListClass::getIdField()");
        return getStringValue("idField");
    }

    public void setIdField(String idField)
    {
        System.err.println("SPARQListClass::setIdField: " + idField);
        setStringValue("idField", idField);
    }

    public String getValueField()
    {
        System.err.println("SPARQListClass::getValueField");
        return getStringValue("valueField");
    }

    public void setValueField(String valueField)
    {
        System.err.println("SPARQListClass::setValueField: " + valueField);
        setStringValue("valueField", valueField);
    }

    public String getInsertAtPosition()
    {
        return getStringValue("insertAtPosition");
    }

    public void setInsertAtPosition(String value)
    {
        setStringValue("insertAtPosition", value);
    }

    public String getInsertValue()
    {
        return getStringValue("insertValue");
    }

    public void setInsertValue(String val)
    {
        setStringValue("insertValue", val);
    }

    public List<ListItem> getCachedSPARQList(XWikiContext context)
    {
        System.err.println("SPARQListClass::getCachedSPARQList");
        if (isCache()) {
            return this.cachedSPARQList;
        } else {
            return (List<ListItem>) context.get(context.getDatabase() + ":" + getFieldFullName());
        }
    }

    public void setCachedSPARQList(List<ListItem> cachedSPARQList, XWikiContext context)
    {
        System.err.println("SPARQListClass::setCachedSPARQList");
        if (isCache()) {
            this.cachedSPARQList = cachedSPARQList;
        } else {
            context.put(context.getDatabase() + ":" + getFieldFullName(), cachedSPARQList);
        }
    }

    @Override
    public void flushCache()
    {
        System.err.println("SPARQListClass::flushCache");
        this.cachedSPARQList = null;
    }

    // return first or second column from user query
    public String returnCol(String hibquery, boolean first)
    {
        System.err.println("SPARQListClass::returnCol: " + hibquery);
        String firstCol = "-", secondCol = "-";

        int fromIndx = hibquery.indexOf("from");

        if (fromIndx > 0) {
            String firstPart = hibquery.substring(0, fromIndx);
            firstPart.replaceAll("\\s+", " ");
            int comIndx = hibquery.indexOf(",");

            // there are more than one columns to select- take the second one (the value)
            if (comIndx > 0 && comIndx < fromIndx) {
                StringTokenizer st = new StringTokenizer(firstPart, " ,()", true);
                ArrayList<String> words = new ArrayList<String>();

                while (st.hasMoreTokens()) {
                    words.add(st.nextToken().toLowerCase());
                }

                int comma = words.indexOf(",") - 1;
                while (words.get(comma).toString().compareTo(" ") == 0) {
                    comma--;
                }
                firstCol = words.get(comma).toString().trim();

                comma = words.indexOf(",") + 1;
                while (words.get(comma).toString().compareTo(" ") == 0) {
                    comma++;
                }

                if (words.get(comma).toString().compareTo("(") == 0) {
                    int i = comma + 1;
                    while (words.get(i).toString().compareTo(")") != 0) {
                        secondCol += words.get(i).toString();
                        i++;
                    }
                    secondCol += ")";
                } else {
                    secondCol = words.get(comma).toString().trim();
                }
            }
            // has only one column
            else {
                int i = fromIndx - 1;
                while (firstPart.charAt(i) == ' ') {
                    --i;
                }
                String col = " ";
                while (firstPart.charAt(i) != ' ') {
                    col += firstPart.charAt(i);
                    --i;
                }
                String reverse = " ";
                for (i = (col.length() - 1); i >= 0; --i) {
                    reverse += col.charAt(i);
                }
                firstCol = reverse.trim();
            }
        }
        if (first == true) {
            return firstCol;
        } else {
            return secondCol;
        }
    }

    // the result of the second query, to retrieve the value
    public String getValue(String val, String sql, XWikiContext context)
    {
        System.err.println("SPARQListClass::getValue");

        // Make sure the query does not contain ORDER BY, as it will fail in certain databases.
        int orderByPos = sql.toLowerCase().lastIndexOf("order by");
        if (orderByPos >= 0) {
            sql = sql.substring(0, orderByPos);
        }
        String firstCol = returnCol(sql, true);
        String secondCol = returnCol(sql, false);

        String newsql = sql.substring(0, sql.indexOf(firstCol));
        newsql += secondCol + " ";
        newsql += sql.substring(sql.indexOf("from"));
        newsql += "and " + firstCol + "='" + val + "'";

        Object[] list = null;
        XWiki xwiki = context.getWiki();
        String res = "";
        try {
            list = xwiki.search(newsql, context).toArray();
            if (list.length > 0) {
                res = list[0].toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    // override the method from parent ListClass
    @Override
    public void displayEdit(StringBuffer buffer, String name, String prefix, BaseCollection object, XWikiContext context)
    {
        System.err.println("SPARQListClass::displayEdit");
        // input display
        if (getDisplayType().equals("input")) {
            input input = new input();
            input.setType("text");
            input.setSize(getSize());
            boolean changeInputName = false;
            boolean setInpVal = true;

            BaseProperty prop = (BaseProperty) object.safeget(name);
            String val = "";
            if (prop != null) {
                val = prop.toFormString();
            }

            if (isPicker()) {
                input.setClass("suggested");
                String path = "";
                XWiki xwiki = context.getWiki();
                path = xwiki.getURL("Main.WebHome", "view", context);
                String classname = this.getObject().getName();
                String fieldname = this.getName();
                String hibquery = this.getSparq();
                String secondCol = "-", firstCol = "-";

                if (hibquery != null && !hibquery.equals("")) {
                    firstCol = returnCol(hibquery, true);
                    secondCol = returnCol(hibquery, false);

                    if (secondCol.compareTo("-") != 0) {
                        changeInputName = true;
                        input hidden = new input();
                        hidden.setID(prefix + name);
                        hidden.setName(prefix + name);
                        hidden.setType("hidden");
                        hidden.setDisabled(isDisabled());
                        if (val != null && !val.equals("")) {
                            hidden.setValue(val);
                        }
                        buffer.append(hidden.toString());

                        input.setValue(getValue(val, hibquery, context));
                        setInpVal = false;
                    }
                }

                String script =
                    "\"" + path + "?xpage=suggest&amp;classname=" + classname + "&amp;fieldname=" + fieldname
                    + "&amp;firCol=" + firstCol + "&amp;secCol=" + secondCol + "&amp;\"";
                String varname = "\"input\"";
                String seps = "\"" + this.getSeparators() + "\"";
                if (isMultiSelect()) {
                    input.setOnFocus("new ajaxSuggest(this, {script:" + script + ", varname:" + varname + ", seps:"
                        + seps + "} )");
                } else {
                    input.setOnFocus("new ajaxSuggest(this, {script:" + script + ", varname:" + varname + "} )");
                }
            }

            if (changeInputName == true) {
                input.setName(prefix + name + "_suggest");
                input.setID(prefix + name + "_suggest");
            } else {
                input.setName(prefix + name);
                input.setID(prefix + name);
            }
            if (setInpVal == true) {
                input.setValue(val);
            }

            input.setDisabled(isDisabled());
            buffer.append(input.toString());
        } else if (getDisplayType().equals("radio") || getDisplayType().equals("checkbox")) {
            displayRadioEdit(buffer, name, prefix, object, context);
        } else {
            displaySelectEdit(buffer, name, prefix, object, context);
        }

        if (!getDisplayType().equals("input")) {
            org.apache.ecs.xhtml.input hidden = new input(input.hidden, prefix + name, "");
            buffer.append(hidden);
        }
    }

    @Override
    public void displayView(StringBuffer buffer, String name, String prefix, BaseCollection object, XWikiContext context)
    {
        System.err.println("SPARQListClass::displayView");
        if (isPicker() && getSparq().compareTo("") != 0) {
            BaseProperty prop = (BaseProperty) object.safeget(name);
            String val = "";
            if (prop != null) {
                val = prop.toFormString();
            }
            Map map = getMap(context);

            String secondCol = returnCol(getSparq(), false);
            if (secondCol.compareTo("-") != 0) {
                String res = getValue(val, getSparq(), context);
                buffer.append(getDisplayValue(res, name, map, context));
            } else {
                buffer.append(getDisplayValue(val, name, map, context));
            }
        } else {
            List<String> selectlist;
            String separator = getSeparator();
            BaseProperty prop = (BaseProperty) object.safeget(name);
            Map<String, ListItem> map = getMap(context);
            if (prop instanceof ListProperty) {
                selectlist = ((ListProperty) prop).getList();
                List<String> newlist = new ArrayList<String>();
                for (String entry : selectlist) {
                    newlist.add(getDisplayValue(entry, name, map, context));
                }
                buffer.append(StringUtils.join(newlist, separator));
            } else {
                buffer.append(getDisplayValue(prop.getValue(), name, map, context));
            }
        }
    }
}
