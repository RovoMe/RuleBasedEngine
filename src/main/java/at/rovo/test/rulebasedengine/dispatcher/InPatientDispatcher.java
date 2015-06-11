/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.rovo.test.rulebasedengine.dispatcher;

/**
 *
 * @author Roman Vottner
 */
public class InPatientDispatcher implements ActionDispatcher
{
    @Override
    public void fire()
    {
        // send patient to in_patient
        System.out.println("Send patient to IN");
    }
}
