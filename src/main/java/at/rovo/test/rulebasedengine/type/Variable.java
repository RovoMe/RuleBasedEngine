/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.rovo.test.rulebasedengine.type;

import at.rovo.test.rulebasedengine.Expression;
import java.util.Map;

/**
 *
 * @author Roman Vottner
 */
public class Variable implements Expression
{
    private String name;
    
    public Variable(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return this.name;
    }

    @Override
    public boolean interpret(Map<String, ?> bindings)
    {
        return true;
    }
}
