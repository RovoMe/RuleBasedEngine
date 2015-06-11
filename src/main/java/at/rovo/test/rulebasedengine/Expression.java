/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.rovo.test.rulebasedengine;

import java.util.Map;

/**
 *
 * @author Roman Vottner
 */
public interface Expression
{
    public boolean interpret(final Map<String, ?> bindings);
}
