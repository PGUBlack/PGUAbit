package abit.ajax;



import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;


public class FillCGsForm extends ActionForm {

    private static final long serialVersionUID = 7403728678369985647L;

 
    private String name = null;
    private String formaOb = null;

    public String getName() {

        return name;

    }

    public void setName(String name) {

        this.name = name;

    }

    public String getFormaOb() {

        return formaOb;

    }

    public void setFormaOb(String formaOb) {

        this.formaOb = formaOb;

    }
    
}


