package com.nitindhar.kampr.util;

import com.nitindhar.forrst.ForrstAPI;
import com.nitindhar.forrst.ForrstAPIClient;
import com.nitindhar.forrst.http.HttpProvider;

public class ForrstUtil {

    private static final ForrstAPI forrst = new ForrstAPIClient(HttpProvider.JAVA_NET);

    public static ForrstAPI client() {
        return forrst;
    }

}