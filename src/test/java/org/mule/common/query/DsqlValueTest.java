/**
 *
 */
package org.mule.common.query;

import org.mule.common.query.dsql.parser.MuleDsqlParser;
import org.mule.common.query.expression.BooleanValue;
import org.mule.common.query.expression.DateTimeValue;
import org.mule.common.query.expression.DateValue;
import org.mule.common.query.expression.FieldComparation;
import org.mule.common.query.expression.IdentifierValue;
import org.mule.common.query.expression.MuleExpressionValue;
import org.mule.common.query.expression.NullValue;
import org.mule.common.query.expression.NumberValue;
import org.mule.common.query.expression.StringValue;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class DsqlValueTest
{

    @Test
    public void parseStringValue(){
        MuleDsqlParser muleDsqlParser = new MuleDsqlParser();
        DsqlQuery parse = muleDsqlParser.parse("select name from account where lastName = 'de Achaval'");
        Assert.assertThat((FieldComparation) parse.getFilterExpression(), CoreMatchers.isA(FieldComparation.class));
        FieldComparation fieldComparation = (FieldComparation) parse.getFilterExpression();
        Assert.assertThat((StringValue) fieldComparation.getValue(), CoreMatchers.isA(StringValue.class));
        StringValue stringValue = (StringValue) fieldComparation.getValue();

        Assert.assertThat(stringValue.getValue(), CoreMatchers.is("de Achaval"));
    }

    @Test
    public void parseDateTimeWithTimZone1Value(){
        MuleDsqlParser muleDsqlParser = new MuleDsqlParser();
        DsqlQuery parse = muleDsqlParser.parse("select name from account where birdth > 1999-01-01T23:01:01+01:00");
        Assert.assertThat((FieldComparation) parse.getFilterExpression(), CoreMatchers.isA(FieldComparation.class));
        FieldComparation fieldComparation = (FieldComparation) parse.getFilterExpression();
        Assert.assertThat((DateTimeValue) fieldComparation.getValue(), CoreMatchers.isA(DateTimeValue.class));
    }

    @Test
    public void parseDateTimeWithTimZone2Value(){
        MuleDsqlParser muleDsqlParser = new MuleDsqlParser();
        DsqlQuery parse = muleDsqlParser.parse("select name from account where birdth > 1999-01-01T23:01:01-01:00");
        Assert.assertThat((FieldComparation) parse.getFilterExpression(), CoreMatchers.isA(FieldComparation.class));
        FieldComparation fieldComparation = (FieldComparation) parse.getFilterExpression();
        Assert.assertThat((DateTimeValue) fieldComparation.getValue(), CoreMatchers.isA(DateTimeValue.class));
    }

    @Test
    public void parseDateTimeWithTimZone3Value(){
        MuleDsqlParser muleDsqlParser = new MuleDsqlParser();
        DsqlQuery parse = muleDsqlParser.parse("select name from account where birth > 1999-01-01T23:01:01Z");
        Assert.assertThat((FieldComparation) parse.getFilterExpression(), CoreMatchers.isA(FieldComparation.class));
        FieldComparation fieldComparation = (FieldComparation) parse.getFilterExpression();
        Assert.assertThat((DateTimeValue) fieldComparation.getValue(), CoreMatchers.isA(DateTimeValue.class));
    }

    @Test
    public void parseDateTimeWithOutTimZoneValue(){
        MuleDsqlParser muleDsqlParser = new MuleDsqlParser();
        DsqlQuery parse = muleDsqlParser.parse("select name from account where birth > 2013-09-05T16:39:26.621-03:00");
        Assert.assertThat((FieldComparation) parse.getFilterExpression(), CoreMatchers.isA(FieldComparation.class));
        FieldComparation fieldComparation = (FieldComparation) parse.getFilterExpression();
        Assert.assertThat((DateTimeValue) fieldComparation.getValue(), CoreMatchers.isA(DateTimeValue.class));
    }

    @Test
    public void parseDateValue(){
        MuleDsqlParser muleDsqlParser = new MuleDsqlParser();
        DsqlQuery parse = muleDsqlParser.parse("select name from account where birth > 1999-01-01");
        Assert.assertThat((FieldComparation) parse.getFilterExpression(), CoreMatchers.isA(FieldComparation.class));
        FieldComparation fieldComparation = (FieldComparation) parse.getFilterExpression();
        Assert.assertThat((DateValue) fieldComparation.getValue(), CoreMatchers.isA(DateValue.class));
    }

    @Test
    public void parseIntValue(){
        MuleDsqlParser muleDsqlParser = new MuleDsqlParser();
        DsqlQuery parse = muleDsqlParser.parse("select name from account where age = 30");
        Assert.assertThat((FieldComparation) parse.getFilterExpression(), CoreMatchers.isA(FieldComparation.class));
        FieldComparation fieldComparation = (FieldComparation) parse.getFilterExpression();
        Assert.assertThat((NumberValue) fieldComparation.getValue(), CoreMatchers.isA(NumberValue.class));
    }

    @Test
    public void parseBooleanValue(){
        MuleDsqlParser muleDsqlParser = new MuleDsqlParser();
        DsqlQuery parse = muleDsqlParser.parse("select name from account where registered = true");
        Assert.assertThat((FieldComparation) parse.getFilterExpression(), CoreMatchers.isA(FieldComparation.class));
        FieldComparation fieldComparation = (FieldComparation) parse.getFilterExpression();
        Assert.assertThat((BooleanValue) fieldComparation.getValue(), CoreMatchers.isA(BooleanValue.class));
    }

    @Test
    public void parseMuleExpressionValue(){
        MuleDsqlParser muleDsqlParser = new MuleDsqlParser();
        DsqlQuery parse = muleDsqlParser.parse("select name from account where address = #[flowVars['address']]");
        Assert.assertThat((FieldComparation) parse.getFilterExpression(), CoreMatchers.isA(FieldComparation.class));
        FieldComparation fieldComparation = (FieldComparation) parse.getFilterExpression();
        Assert.assertThat((MuleExpressionValue) fieldComparation.getValue(), CoreMatchers.isA(MuleExpressionValue.class));
        MuleExpressionValue stringValue = (MuleExpressionValue) fieldComparation.getValue();

        Assert.assertThat(stringValue.getValue(), CoreMatchers.is("#[flowVars['address']]"));
    }

    @Test
    public void parseIdentifierValue(){
        MuleDsqlParser muleDsqlParser = new MuleDsqlParser();
        DsqlQuery parse = muleDsqlParser.parse("select name from account where birth > NEXT_WEEK");
        Assert.assertThat((FieldComparation) parse.getFilterExpression(), CoreMatchers.isA(FieldComparation.class));
        FieldComparation fieldComparation = (FieldComparation) parse.getFilterExpression();
        Assert.assertThat((IdentifierValue) fieldComparation.getValue(), CoreMatchers.isA(IdentifierValue.class));
        IdentifierValue stringValue = (IdentifierValue) fieldComparation.getValue();
        Assert.assertThat(stringValue.getValue(), CoreMatchers.is("NEXT_WEEK"));
    }

    @Test
    public void parseNullValue(){
        MuleDsqlParser muleDsqlParser = new MuleDsqlParser();
        DsqlQuery parse = muleDsqlParser.parse("select name from account where address = null");
        Assert.assertThat((FieldComparation) parse.getFilterExpression(), CoreMatchers.isA(FieldComparation.class));
        FieldComparation fieldComparation = (FieldComparation) parse.getFilterExpression();
        Assert.assertThat((NullValue) fieldComparation.getValue(), CoreMatchers.isA(NullValue.class));
    }

}
