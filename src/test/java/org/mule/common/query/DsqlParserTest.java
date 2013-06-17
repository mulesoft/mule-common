package org.mule.common.query;

import static org.junit.Assert.*;

import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.junit.Test;
import org.mule.common.query.dsql.grammar.DsqlLexer;
import org.mule.common.query.dsql.grammar.DsqlParser;
import org.mule.common.query.dsql.grammar.DsqlParser.select_return;
import org.mule.common.query.dsql.parser.MuleDsqlParser;
import org.mule.common.query.dsql.parser.exception.DsqlParsingException;

public class DsqlParserTest {
	
	@Test
	public void testEasyParse() {
		try {
			parse("select * from users where name='alejo'");
		} catch (Throwable e) {
			fail();
		}
	}

	@Test
	public void testParse1() {
		try {
			parse("select name, surname from users, addresses where name='alejo' and (apellido='abdala' and address='guatemala 1234') order by name limit 10 offset 200");
		} catch (Throwable e) {
			fail();
		}
	}
	
	@Test
	public void testParse1b() {
		try {
			parse("select name, surname from users, addresses where (name='alejo' and apellido='abdala') and address='guatemala 1234' order by name limit 10 offset 200");
		} catch (Throwable e) {
			fail();
		}
	}
	
	@Test
	public void testParse2() {
		try {
			parse("select * from users, addresses where name='alejo' and apellido='abdala' or apellido='achaval' and name='mariano' and cp='1234'");
		} catch (Throwable e) {
			fail();
		}
	}

	@Test
	public void testParse3() {
		try {
			parse("select * from users, addresses where name='alejo' and not (age > 25)");
		} catch (Throwable e) {
			fail();
		}
	}

	@Test
	public void testParse4() {
		try {
			parse("select * from users, addresses where name='alejo' and age <> 25");
		} catch (Throwable e) {
			fail();
		}
	}

	@Test
	public void testParse5() {
		try {
			parse("select * from users, addresses where name='alejo' and (age >= 25 or age <= 40)");
		} catch (Throwable e) {
			fail();
		}
	}

	@Test
	public void testFail() {
		try {
			parse("select * from users, addresses where name='alejo' and ");
			fail();
		} catch (Throwable t) {
			assertTrue (t instanceof DsqlParsingException);
		}
	}

	// TODO: make this test fail correctly. :)
//	@Test
//	public void testFail2() {
//		try {
//			parse("select * from users, addresses where ");
//			fail();
//		} catch (Throwable t) {
//			t.printStackTrace();
//			assertTrue (t instanceof DsqlParsingException);
//		}
//	}
	
	public void parse(final String string) {
		CharStream antlrStringStream = new ANTLRStringStream(string);
		DsqlLexer dsqlLexer = new DsqlLexer(antlrStringStream);
		CommonTokenStream dsqlTokens = new CommonTokenStream();
		dsqlTokens.setTokenSource(dsqlLexer);

		DsqlParser dsqlParser = new DsqlParser(dsqlTokens);
		try {
			select_return select = dsqlParser.select();
			CommonTree tree = (CommonTree) select.getTree();
			printTree(tree);
			
			MuleDsqlParser parser = new MuleDsqlParser();
	        DsqlQueryVisitor visitor = new DsqlQueryVisitor();
	        parser.parse(string).accept(visitor);
	        System.out.println(visitor.dsqlQuery());

		} catch (RecognitionException e) {
			throw new DsqlParsingException(e);
		}
	}
	
	private void printTree(CommonTree tree) {
		printTree(tree, 0);
	}
	
	@SuppressWarnings("unchecked")
	private void printTree(CommonTree tree, int level) {
		List<CommonTree> children = (List<CommonTree>)tree.getChildren();
		System.out.println(tree.getText() + " - Type=" + tree.getType());
		if (children != null) {
			level+=2;
			for (CommonTree t : children) {
				if (t != null) {
					printLevel(level);
					printTree(t, level);
				}
			}
		}
	}

	private void printLevel(int level) {
		for (int i = 0; i < level; i++) {
			System.out.print("-");
		}
		System.out.print("-> ");
		
	}

}