package org.androware.flow;

import java.util.NoSuchElementException;

/**
 * Created by jkirkley on 6/9/16.
 */
public abstract  class EnumeratedStepGenerator implements StepGenerator {


    protected int numSteps;

    protected int currIndex = -1;

    public EnumeratedStepGenerator(int numSteps) {
        this.numSteps = numSteps;
    }

    protected abstract Step getStepInternal();

    @Override
    public Step getStep(int index) {
        if(index >= 0 && index < numSteps){
            currIndex = index;
            return getStepInternal();
        }
        throw new IndexOutOfBoundsException("Index : " + index + ", out of bounds:  [0, " + (numSteps-1) + "].");
    }

    @Override
    public Step getStep(String name) {
        // NOTE this method is not clearly defined since steps
        // are not named in this case and currIndex cannot be set properly
        throw new NoSuchElementException( "No named steps in EnumeratedStepGenerator: " + name );
    }

    @Override
    public Step next() {
        return getStep(currIndex+1);
    }

    @Override
    public Step prev() {
        return getStep(currIndex-1);
    }

    @Override
    public Step first() {
        return getStep(0);
    }

    @Override
    public Step last() {
        return getStep(numSteps-1);
    }

    @Override
    public boolean atStart() {
        return currIndex <= 0;
    }

    @Override
    public boolean atEnd() {
        return currIndex == numSteps - 1;
    }

    public int getNumSteps() {
        return numSteps;
    }

    public void setNumSteps(int numSteps) {
        this.numSteps = numSteps;
    }

    public int getCurrIndex() {
        return currIndex;
    }

    public void setCurrIndex(int currIndex) {
        this.currIndex = currIndex;
    }


}
