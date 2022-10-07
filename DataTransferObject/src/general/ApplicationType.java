package general;


public enum ApplicationType {

    UBOAT("uboat"),
    ALLY("ally"),
    AGENT("agent");


    final String name;
    ApplicationType(String type)
    {
        this.name=type;
    }
    public String getURLContext()
    {
        return "\\"+name;
    }
    @Override
    public String toString()
    {
        return name;
    }
}
