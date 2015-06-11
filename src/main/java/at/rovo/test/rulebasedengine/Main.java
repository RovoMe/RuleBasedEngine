package at.rovo.test.rulebasedengine;

import at.rovo.test.rulebasedengine.dispatcher.ActionDispatcher;
import at.rovo.test.rulebasedengine.dispatcher.InPatientDispatcher;
import at.rovo.test.rulebasedengine.dispatcher.OutPatientDispatcher;
import at.rovo.test.rulebasedengine.operations.And;
import at.rovo.test.rulebasedengine.operations.Or;
import at.rovo.test.rulebasedengine.operations.Equals;
import at.rovo.test.rulebasedengine.operations.Less;
import at.rovo.test.rulebasedengine.operations.LessEquals;
import at.rovo.test.rulebasedengine.operations.More;
import at.rovo.test.rulebasedengine.operations.MoreEquals;
import at.rovo.test.rulebasedengine.operations.Not;
import at.rovo.test.rulebasedengine.operations.NotEquals;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class Main 
{
    public static void main( String[] args )
    {
        // create a singleton container for operations
        Operations operations = Operations.INSTANCE;
        
        // register new operations with the previously created container
        operations.registerOperation(new And());
        operations.registerOperation(new Or());
        operations.registerOperation(new Equals());
        operations.registerOperation(new Equals(), "EQUALS");
        operations.registerOperation(new Not());
        operations.registerOperation(new Less());
        operations.registerOperation(new More());
        operations.registerOperation(new LessEquals());
        operations.registerOperation(new MoreEquals());
        operations.registerOperation(new NotEquals());

        // define the possible actions for rules that fire
        ActionDispatcher inPatient = new InPatientDispatcher();
        ActionDispatcher outPatient = new OutPatientDispatcher();

        // create the rules and link them to the accoridng expression and action
        Rule rule1 = new Rule.Builder()
            .withName("rule1")
            .withExpression("PATIENT_TYPE = 'A' AND ADMISSION_TYPE = 'O'")
            .withDispatcher(outPatient)
            .build();
        Rule rule2 = new Rule.Builder()
            .withName("rule2")
            .withExpression("PATIENT_TYPE = 'B'")
            .withDispatcher(inPatient)
            .build();
        Rule rule3 = new Rule.Builder()
            .withName("rule3")
            .withExpression("PATIENT_TYPE = 'A' AND NOT ADMISSION_TYPE = 'O'")
            .withDispatcher(inPatient)
            .build();
        Rule rule4 = new Rule.Builder()
            .withName("rule4")
            .withExpression("PATIENT_TYPE != 'B' AND TEST_VALUE >= 5")
            .withExpression("PATIENT_TYPE != 'A' OR TEST_VALUE < 5")
            .withDispatcher(inPatient)
            .build();

        // add all rules to a single container
        Rules rules = new Rules();
        rules.addRule(rule4);
        rules.addRule(rule1);
        rules.addRule(rule2);
        rules.addRule(rule3);
        
        // for test purpose define a variable binding ...
        Map<String, Object> bindings = new HashMap<>();
        bindings.put("PATIENT_TYPE", "'A'");
        bindings.put("ADMISSION_TYPE", "'O'");
        bindings.put("TEST_VALUE", 5);
        // ... and evaluate the defined rules with the specified bindings
        boolean triggered = rules.eval(bindings);
        System.out.println("Action triggered: "+triggered);
    }
}
