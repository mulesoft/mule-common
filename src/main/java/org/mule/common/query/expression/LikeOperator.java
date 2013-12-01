/**
 *
 */
package org.mule.common.query.expression;

public class LikeOperator  extends AbstractBinaryOperator
{

    public String accept(OperatorVisitor operatorVisitor)
    {
        return operatorVisitor.likeOperator();
    }
}
