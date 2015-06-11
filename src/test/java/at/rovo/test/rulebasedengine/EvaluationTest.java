package at.rovo.test.rulebasedengine;

import at.rovo.test.rulebasedengine.dispatcher.ActionDispatcher;
import at.rovo.test.rulebasedengine.operations.And;
import at.rovo.test.rulebasedengine.operations.Equals;
import at.rovo.test.rulebasedengine.operations.Less;
import at.rovo.test.rulebasedengine.operations.LessEquals;
import at.rovo.test.rulebasedengine.operations.More;
import at.rovo.test.rulebasedengine.operations.MoreEquals;
import at.rovo.test.rulebasedengine.operations.Not;
import at.rovo.test.rulebasedengine.operations.NotEquals;
import at.rovo.test.rulebasedengine.operations.Or;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.Test;

public class EvaluationTest
{
    // create a singleton container for operations
    private final Operations operations = Operations.INSTANCE;
        
    {
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
    }
    
    private String outcome = null;

    // define the possible actions for rules that fire
    private ActionDispatcher inPatient = new ActionDispatcher() {

        @Override
        public void fire()
        {
            outcome = "inPatient";
        }
    };
    private ActionDispatcher outPatient = new ActionDispatcher() {

        @Override
        public void fire()
        {
            outcome = "outPatient";
        }
    };
    
    @Before
    public void init()
    {
        outcome = null;
    }
    
    @Test
    public void simpleRules() throws Exception
    {
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
        
        // add all rules to a single container
        Rules rules = new Rules();
        rules.addRule(rule1);
        rules.addRule(rule2);
        
        // define some variable binding ...
        Map<String, Object> bindings = new HashMap<>();
        bindings.put("PATIENT_TYPE", "'A'");
        bindings.put("ADMISSION_TYPE", "'O'");

        // ... and evaluate the defined rules with the specified bindings
        boolean triggered = rules.eval(bindings);

        // assert that the rules given the bindings evaluated to true
        assertThat(triggered, is(equalTo(true)));
        // assert that the out-patient action dispatcher was actually invoked
        assertThat(outcome, is(equalTo("outPatient")));
        // assert that rule1 triggered the action dispatch
        assertThat(rules.getFiredRule(), is(rule1));
    }
    
    @Test
    public void rulesWithNotAndOr() throws Exception
    {
        // create the rules and link them to the accoridng expression and action
        Rule rule1 = new Rule.Builder()
            .withName("rule1")
            .withExpression("PATIENT_TYPE = 'B'")
            .withDispatcher(inPatient)
            .build();
        Rule rule2 = new Rule.Builder()
            .withName("rule2")
            .withExpression("PATIENT_TYPE = 'A' AND NOT ADMISSION_TYPE = 'O'")
            .withDispatcher(inPatient)
            .build();
        Rule rule3 = new Rule.Builder()
            .withName("rule3")
            .withExpression("PATIENT_TYPE != 'B' AND TEST_VALUE >= 5")
            .withExpression("PATIENT_TYPE != 'A' OR TEST_VALUE < 5")
            .withDispatcher(outPatient)
            .build();
        
        // add all rules to a single container
        Rules rules = new Rules();
        rules.addRule(rule1);
        rules.addRule(rule2);
        rules.addRule(rule3);
        
        // define some variable binding ...
        Map<String, Object> bindings = new HashMap<>();
        bindings.put("PATIENT_TYPE", "'A'");
        bindings.put("ADMISSION_TYPE", "'O'");
        bindings.put("TEST_VALUE", 5);

        // ... and evaluate the defined rules with the specified bindings
        boolean triggered = rules.eval(bindings);

        // assert that the rules given the bindings evaluated to true
        assertThat(triggered, is(equalTo(true)));
        // assert that the out-patient action dispatcher was actually invoked
        assertThat(outcome, is(equalTo("outPatient")));
        // assert that rule1 triggered the action dispatch
        assertThat(rules.getFiredRule(), is(rule3));
    }
    
    @Test
    public void noRuleTriggered() throws Exception
    {
        // create the rules and link them to the accoridng expression and action
        Rule rule1 = new Rule.Builder()
            .withName("rule1")
            .withExpression("PATIENT_TYPE = 'C'")
            .withDispatcher(inPatient)
            .build();
        Rule rule2 = new Rule.Builder()
            .withName("rule2")
            .withExpression("PATIENT_TYPE = 'B' AND NOT ADMISSION_TYPE = 'O'")
            .withDispatcher(inPatient)
            .build();
        Rule rule3 = new Rule.Builder()
            .withName("rule3")
            .withExpression("PATIENT_TYPE != 'B' AND TEST_VALUE >= 5")
            .withExpression("PATIENT_TYPE = 'A' AND NOT ADMISSION_TYPE = 'O' OR TEST_VALUE < 5")
            .withDispatcher(outPatient)
            .build();
        
        // add all rules to a single container
        Rules rules = new Rules();
        rules.addRule(rule1);
        rules.addRule(rule2);
        rules.addRule(rule3);
        
        // define some variable binding ...
        Map<String, Object> bindings = new HashMap<>();
        bindings.put("PATIENT_TYPE", "'B'");
        bindings.put("ADMISSION_TYPE", "'O'");
        bindings.put("TEST_VALUE", 5);

        // ... and evaluate the defined rules with the specified bindings
        boolean triggered = rules.eval(bindings);

        assertThat(triggered, is(equalTo(false)));
        assertThat(outcome, is(equalTo(null)));
        assertThat(rules.getFiredRule(), is(equalTo(null)));
    }
}
