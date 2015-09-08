package config;


import java.util.Enumeration;
import java.util.ResourceBundle;


public class Config {


    private volatile static Config instance;
    private volatile ResourceBundle resource;
    private static final String FILENAME = "config.cf";
    public static final String REDIRECT = "REDIRECT";
    public static final String MISSING = "MISSING";
    public static final String DEFAULT = "DEFAULT";


    private Config() {

    }

    public static Config getInstance() {

        if (instance == null) {

            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config();
                    instance.resource = ResourceBundle.getBundle(FILENAME);
                }
            }
        }
        return instance;
    }

    public synchronized String getProperty(String page) {

        Enumeration<String> keys = resource.getKeys();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();

            if (resource.getObject(key).toString().equals(page)
                    && !resource.getObject(DEFAULT).toString().equals(page)) {
                return key;

            } else if (page.contains(resource.getObject(REDIRECT).toString())) {
                return REDIRECT;

            }
        }

        return MISSING;

    }
}