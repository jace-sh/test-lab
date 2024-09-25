package jace.shim.testlab.utils;

public class NumberIdGenerator {

    private static Snowflake snowflake = new Snowflake();

    public static Long generate() {
        return snowflake.nextId();
    }
}
