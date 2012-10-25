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
package com.objectsecurity.jena;

import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.VCARD;
import com.objectsecurity.jena.Context.Mode;

public class BookStoreTest {

	static String pUri = "http://www.boookstore.net/authors/JRRTolkien";
	static String p2Uri = "http://www.boookstore.net/authors/JaneAusten";
	static String bUri="http://www.bookstore.net/books/";
	static String fullName = "J.R.R.Tolkien";
	static String fullName2 = "Jane Austen";
	
	public static void main(String[] args) {
		Context ctx = Context.getInstance();
		ctx.getModel();
		/*
		Resource tolkien = model.createResource(pUri);
		tolkien.addProperty(VCARD.FN, fullName);
		tolkien.addProperty(BookStore.AUTHOR, "true");
		
		Resource hobbit = model.createResource(bUri + "TheHobbit");
		hobbit.addProperty(DC.title, "The Hobbit");
		hobbit.addProperty(BookStore.BOOK, "true");
		hobbit.addProperty(DC.creator, tolkien);
		hobbit.addProperty(BookStore.WRITTEN_BY, tolkien);
		tolkien.addProperty(BookStore.WROTE, hobbit);

		Resource lotr = model.createResource(bUri + "TheLordOftheRings");
		lotr.addProperty(DC.title, "The Lord of the Rings");
		lotr.addProperty(BookStore.BOOK, "true");
		lotr.addProperty(DC.creator, tolkien);
		lotr.addProperty(BookStore.WRITTEN_BY, tolkien);
		tolkien.addProperty(BookStore.WROTE, lotr);

		Resource austen = model.createResource(p2Uri);
		austen.addProperty(VCARD.FN, fullName2);
		austen.addProperty(BookStore.AUTHOR, "true");
		
		Resource pap = model.createResource(bUri + "PrideAndPrejudice");
		pap.addProperty(DC.title, "Pride and Prejudice");
		pap.addProperty(BookStore.BOOK, "true");
		pap.addProperty(DC.creator, austen);
		pap.addProperty(BookStore.WRITTEN_BY, austen);
		austen.addProperty(BookStore.WROTE, pap);
		*/
		ctx.setProperty(pUri, VCARD.FN, fullName, Mode.MODIFY);
		ctx.setProperty(pUri, "http://www.objectsecurity.com/bookStore#", "AUTHOR", "true", Mode.MODIFY);
		
		ctx.setProperty(bUri+"TheHobbit", DC.title, "The Hobbit", Mode.MODIFY);
		ctx.setProperty(bUri+"TheHobbit", "http://www.objectsecurity.com/bookStore#", "BOOK", "true", Mode.MODIFY);
		ctx.setProperty(bUri+"TheHobbit", DC.creator, "J.R.R.Tolkien", Mode.MODIFY);
		ctx.setProperty(bUri+"TheHobbit", "http://www.objectsecurity.com/bookStore#", "WRITTEN_BY", "J.R.R.Tolkien", Mode.MODIFY);
		
		ctx.setProperty(pUri, "http://www.objectsecurity.com/bookStore#", "WROTE", "The Hobbit", Mode.MODIFY);

		//ctx.flush();
		
		/*
		 * How to find all books written by tolkien?

SELECT ?title 
WHERE
 { ?book <http://purl.org/dc/elements/1.1/title> ?title .
   ?book <http://purl.org/dc/elements/1.1/creator> ?author .
   ?book <http://www.objectsecurity.com/bookStore#BOOK> ?isBook .
   ?book <http://purl.org/dc/elements/1.1/creator> <http://www.boookstore.net/authors/JRRTolkien> .
   FILTER regex(?isBook, "true", "i") }

		 */

		String queryString = "SELECT ?title ?author ?isBook ";
		queryString += "WHERE ";
		queryString += "{ ?book <http://purl.org/dc/elements/1.1/title> ?title .";
		queryString += "?book <http://purl.org/dc/elements/1.1/creator> ?author .";
		queryString += "?book <http://www.objectsecurity.com/bookStore#BOOK> ?isBook .";
		//queryString += "?book <http://purl.org/dc/elements/1.1/creator> <http://www.boookstore.net/authors/JRRTolkien> .";
		queryString += "?book <http://purl.org/dc/elements/1.1/creator> \"J.R.R.Tolkien\" .";
		queryString += "FILTER regex(?isBook, \"true\", \"i\") }";
		
		String res = ctx.query(queryString, "author,title,isBook", "true");
		System.out.println(res);
		
		//model.listStatements(model.createResource();
//		StmtIterator iter = model.listStatements(new SimpleSelector(model.createResource(bUri + "TheHobbit"), null, (RDFNode)null) {
//			public boolean selects(Statement s) {
//				return true;
//			}
//		});
//		while (iter.hasNext()) {
//			Statement stmt = iter.next();
//			System.out.print(stmt.getPredicate().toString());
//			System.out.print(" -> ");
//			System.out.println(stmt.getLiteral().toString());
//		}
		String props = ctx.getPropertyTableForResource(bUri+"TheHobbit", "true");
		System.out.println(props);
	}
}
