// Player.java
package pl.fanfatal.swipecontrollerdemo;

import java.io.Serializable;

public class AccountBeneficaryList implements Serializable {
    public String name;
    public Percentage percentage;
    private String id;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Percentage getPercentage() {
        return percentage;
    }

}
