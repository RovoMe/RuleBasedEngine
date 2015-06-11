/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.rovo.test.rulebasedengine;

import at.rovo.test.rulebasedengine.operations.Operation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum Operations
{
    /** Application of the Singleton pattern using enum **/
    INSTANCE;
    
    private final Map<String, Operation> operations = new HashMap<>();
    
    public void registerOperation(Operation op, String symbol)
    {
        if (!operations.containsKey(symbol))
            operations.put(symbol, op);
    }
    
    public void registerOperation(Operation op)
    {
        if (!operations.containsKey(op.getSymbol()))
            operations.put(op.getSymbol(), op);
    }
    
    public Operation getOperation(String symbol)
    {
        return this.operations.get(symbol);
    }
    
    public Set<String> getDefinedSymbols()
    {
        return this.operations.keySet();
    }
}
