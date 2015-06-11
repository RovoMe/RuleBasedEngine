/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.rovo.test.rulebasedengine.operations;

import at.rovo.test.rulebasedengine.Expression;
import at.rovo.test.rulebasedengine.type.BaseType;
import at.rovo.test.rulebasedengine.type.Variable;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author Roman Vottner
 */
public class Equals extends Operation
{      
    public Equals()
    {
        super("=");
    }
    
    @Override
    public Equals copy()
    {
        return new Equals();
    }
    
    @Override
    public int parse(final String[] tokens, int pos, Stack<Expression> stack)
    {
        int i=1;
        // check if the previous token is a NOT symbol - in that case we need to
        // use the token before the NOT
        if (pos-1 >= 0)
        {
            if (tokens[pos-1].equals("NOT"))
                i = 2;
        }
        if (pos-i >= 0 && tokens.length >= pos+1)
        {
            String var = tokens[pos-i];
            
            this.leftOperand = new Variable(var);
            this.rightOperand = BaseType.getBaseType(tokens[pos+1]);
            stack.push(this);
            
            return pos+1;
        }
        throw new IllegalArgumentException("Cannot assign value to variable");
    }
    
    @Override
    public boolean interpret(Map<String, ?> bindings)
    {
        Variable v = (Variable)this.leftOperand;
        Object obj = bindings.get(v.getName());
        if (obj == null)
            return false;

        BaseType<?> type = (BaseType<?>)this.rightOperand;
        if (type.getType().equals(obj.getClass()))
        {
            if (type.getValue().equals(obj))
                return true;
        }
        return false;
    }
}
