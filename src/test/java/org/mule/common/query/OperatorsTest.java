package org.mule.common.query;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mule.common.query.expression.EqualsOperator;
import org.mule.common.query.expression.GreaterOperator;
import org.mule.common.query.expression.GreaterOrEqualsOperator;
import org.mule.common.query.expression.LessOperator;
import org.mule.common.query.expression.LikeOperator;
import org.mule.common.query.expression.Operator;

public class OperatorsTest {

    @Test
    public void testEquals() {
        assertThat(new EqualsOperator(), equalTo(new EqualsOperator()));
        assertThat(new LessOperator(), equalTo(new LessOperator()));
        assertThat(new GreaterOperator(), equalTo(new GreaterOperator()));
        assertThat(new LikeOperator(), equalTo(new LikeOperator()));
    }
    @Test
    public void testNotEquals() {
        assertThat((Operator) new EqualsOperator(), is(not(equalTo((Operator) new LessOperator()))));
        assertThat((Operator) new LessOperator(), is(not(equalTo((Operator) new GreaterOperator()))));
        assertThat((Operator) new LikeOperator(), is(not(equalTo((Operator) new GreaterOrEqualsOperator()))));
        assertThat((Operator) new EqualsOperator(), is(not(equalTo((Operator) new LikeOperator()))));
    }

}
