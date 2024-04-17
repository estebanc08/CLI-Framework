package oop.project.cli.argparser;

import com.google.common.cache.AbstractCache;

import java.util.HashMap;

public class ReturnValue {

    public HashMap<String, Argument> map;

    ReturnValue(){
        this.map = new HashMap<>();
    }
}
