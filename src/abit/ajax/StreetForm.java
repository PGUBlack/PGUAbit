package abit.ajax;



import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;


public class StreetForm extends ActionForm {

    private static final long serialVersionUID = 7403728678369985647L;

 
    private String name = null;
    private String code = null;
    private String action = null;
   
    public String getName() {

        return name;

    }

    public void setName(String name) {

        this.name = name;

    }

    public String getCode() {

        return code;

    }

    public void setCode(String code) {

        this.code = code;

    }
    public String getAction() {

        return action;

    }

    public void setAction(String action) {

        this.action = action;

    }
}

