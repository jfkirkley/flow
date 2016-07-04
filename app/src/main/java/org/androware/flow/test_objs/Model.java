package org.androware.flow.test_objs;

import android.databinding.ObservableArrayMap;

/**
 * Created by jkirkley on 7/3/16.
 */

public class Model {
    public ObservableArrayMap<Integer,String> MyMap=new ObservableArrayMap<>();
    public Model(){
        MyMap.put(1,"easy");
    }
}
