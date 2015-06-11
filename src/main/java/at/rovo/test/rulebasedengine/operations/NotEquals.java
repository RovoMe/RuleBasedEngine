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
public class NotEquals extends Operation
{      
    public NotEquals()
    {
        super("!=");
    }
    
    @Override
    public NotEquals copy()
    {
        return new NotEquals();
    }
    
    @Override
    public int parse(final String[] tokens, int pos, Stack<Expression> stack)
    {
        if (pos-1 >= 0 && tokens.length >= pos+1)
        {
            String var = tokens[pos-1];
            
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
        if ( !type.getType().equals(obj.getClass()) || 
             !type.getValue().equals(obj) )
        {
                return true;
        }
        return false;
    }
}
