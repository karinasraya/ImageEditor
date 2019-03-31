/**  
  * Filter is an abstract superclass for all image filters in this  
  * application. Filters can be applied to OFImages by invoking the apply   
  * method.  
  *   
  * @author Karina Soraya P  
  * @version 1.4/20181126  
  */  
 public abstract class Filter
{
    private String name;
    public Filter(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return name;
    }
    public abstract void apply(OFImage image);
}
