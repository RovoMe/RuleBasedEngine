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
public class BaseType<T> implements Expression
{
    public T value;
    public Class<T> type;
    
    public BaseType(T value, Class<T> type)
    {
        this.value = value;
        this.type = type;
    }
    
    public T getValue()
    {
        return this.value;
    }
    
    public Class<T> getType()
    {
        return this.type;
    }
    
    @Override
    public boolean interpret(Map<String, ?> bindings)
    {
        return true;
    }
    
    public static BaseType<?> getBaseType(String string)
    {
        if (string == null)
            throw new IllegalArgumentException("The provided string must not be null");
        
        if ("true".equals(string) || "false".equals(string))
            return new BaseType<>(Boolean.getBoolean(string), Boolean.class);
        else if (string.startsWith("'"))
            return new BaseType<>(string, String.class);
        else if (string.contains("."))
            return new BaseType<>(Float.parseFloat(string), Float.class);
        else
            return new BaseType<>(Integer.parseInt(string), Integer.class);
    }
}
