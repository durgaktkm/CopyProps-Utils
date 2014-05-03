import java.util.Date;

/**
 * Created by Ramprasad on 3/13/14.
 */
public class T1 {
    @MyAnnotation(name = "s1")
    private String sw;
    public String s2;
    public int i;
    public Date today;
    public Abc abc;

    public String getSw() {
        return sw;
    }

    public void setSw(String sw) {
        this.sw = sw;
    }
}
