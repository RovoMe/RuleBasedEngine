/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.rovo.test.rulebasedengine.operations;

import at.rovo.test.rulebasedengine.Expression;
import java.util.Map;
import java.util.Stack;

public class And extends Operation
{    
    public And()
    {
        super("AND");
    }
    
    @Override
    public And copy()
    {
        return new And();
    }
    
    @Override
    public int parse(String[] tokens, int pos, Stack<Expression> stack)
    {
        Expression left = stack.pop();
        int i = findNextExpression(tokens, pos+1, stack);
        Expression right = stack.pop();
        
        this.leftOperand = left;
        this.rightOperand = right;

        if(left instanceof Or)
        {
            Or or = (Or)left;
            this.leftOperand = or.rightOperand;
            or.rightOperand = this;
            stack.push(or);
        }
        else
        {
            stack.push(this);
        }

        return i;
    }
    
    @Override
    public boolean interpret(Map<String, ?> bindings)
    {
        return leftOperand.interpret(bindings) && rightOperand.interpret(bindings);
    }
}
