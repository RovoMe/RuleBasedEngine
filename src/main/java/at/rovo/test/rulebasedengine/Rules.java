/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.rovo.test.rulebasedengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Roman Vottner
 */
public class Rules
{
    private List<Rule> rules = new ArrayList<>();
    
    public Rules()
    {
        
    }
    
    public void addRule(Rule rule)
    {
        if (!rules.contains(rule))
            rules.add(rule);
    }
    
    public boolean eval(Map<String, ?> bindings)
    {
        boolean result = false;
        for (Rule rule : rules)
        {
            result = rule.eval(bindings);
            System.out.println("DEBUG: "+rule+" evaluate "+result);
            if (result)
                return true;
        }
        return false;
    }
}
