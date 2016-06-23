package org.androware.flow;


/**
 * Created by jkirkley on 6/9/16.
 */
public class TemplateStepGenerator extends EnumeratedStepGenerator {

    Step templateStep;

    public TemplateStepGenerator(Step templateStep, Integer numSteps) {
        super(numSteps);
        this.templateStep = templateStep;
    }


    @Override
    protected Step getStepInternal() {
        templateStep.setName("" + currIndex);
        return templateStep;
    }

    public Step getTemplateStep() {
        return templateStep;
    }

    public void setTemplateStep(Step templateStep) {
        this.templateStep = templateStep;
    }

}
