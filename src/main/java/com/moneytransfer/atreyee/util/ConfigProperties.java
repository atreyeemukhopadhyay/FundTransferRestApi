package com.moneytransfer.atreyee.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {

    private static Properties props;

    private static Properties loadProperties()
    {

        InputStream ipStream =  ConfigProperties.class.getClassLoader().getResourceAsStream("application.properties");

        if(ipStream!=null){
            try {
                props =  new Properties();
                props.load(ipStream);
            }catch (IOException ex){
                // handle exception
            }
        }
        return props;
    }
    public static Properties getProps(){

        if (props == null) {
            return loadProperties();

        }
        return props;
    }
}
