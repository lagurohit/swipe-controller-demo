package pl.fanfatal.swipecontrollerdemo;

import java.io.Serializable;

/**
 * Created by rohit on 1/31/18.
 */

public class Percentage implements Serializable {
    /**
     * Auto-generated serial UID which is used to serialize and de-serialize Customer objects
     */
    private static final long serialVersionUID = 2436034484547418609L;

    //The amount expression as a decimal
    public String value;

    //The amount formatted with a percent sign and a decimal.
    public String formatted;
}