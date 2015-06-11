/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.rovo.test.rulebasedengine.operations;

import at.rovo.test.rulebasedengine.Expression;
import java.util.Map;
import java.util.Stack;

public class Not extends Operation
{    
    public Not()
    {
        super("NOT");
    }
    
    @Override
    public Not copy()
    {
        return new Not();
    }
    
    @Override
    public int parse(String[] tokens, int pos, Stack<Expression> stack)
    {
        int i = findNextExpression(tokens, pos+1, stack);
        Expression right = stack.pop();
        
        this.rightOperand = right;
        stack.push(this);
 
        return i;
    }

    @Override
    public boolean interpret(final Map<String, ?> bindings)
    {
        return !this.rightOperand.interpret(bindings);
    }    
}
