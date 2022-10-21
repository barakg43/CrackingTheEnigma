package general;


public enum ApplicationType {

    UBOAT("uboat"),
    ALLY("ally"),
    AGENT("agent");


    final private String name;
    ApplicationType(String type)
    {
        name=type;
    }
//    public String getURLContext()
//    {
//        return "\\"+name;
//    }
    public String getURLContext()
    {
        return "/"+name;
    }
    @Override
    public String toString()
    {
        return name;
    }
}
