package org.androware.flow;

/**
 * Created by jkirkley on 6/8/16.
 */
public interface StepGenerator {

    public Step getStep(int index);
    public Step getStep(String name);
    public Step next();
    public Step prev();

    public Step first();
    public Step last();

    public boolean atStart();
    public boolean atEnd();

}
