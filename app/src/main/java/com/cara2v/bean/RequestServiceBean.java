package com.cara2v.bean;

import java.util.ArrayList;

/**
 * Created by chiranjib on 6/1/18.
 */

public class RequestServiceBean {

    public String id;
    public String name;
    public ArrayList<Subservice>subService;

    public static class Subservice {
        public String id;
        public String name;

        @Override
        public String toString() {
            return name;
        }
    }
}
