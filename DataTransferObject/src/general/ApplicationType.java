package general;


public enum ApplicationType {

    UBOAT("uboat"),
    ALLY("ally"),
    AGENT("agent");


    static final String name=null;
    ApplicationType(String type)
    {
        name=type;
    }
//    public String getURLContext()
//    {
//        return "\\"+name;
//    }
    public static String getURLContext()
    {
        return "\\"+name;
    }
    @Override
    public String toString()
    {
        return name;
    }
}
