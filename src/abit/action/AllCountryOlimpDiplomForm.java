package abit.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import abit.bean.AllCountryOlimpDiplomBean;
import abit.util.StringUtil;

public class AllCountryOlimpDiplomForm extends ActionForm{
	private String  name                     = null;
	private String  family                   = null;
	private String  action                       = null;
	
	public String  getName()                     { return name; }
	public String  getFamily()                   { return family; }
	public String  getAction()                   { return action; }
	
	public void setName(String value)            { name = value.trim(); }
	public void setFamily(String value)          { family = value.trim(); }
	public void setAction(String value)          { action = value.trim(); }
	
    public void reset(ActionMapping mapping, HttpServletRequest request )
    {
    	name                     = null;
    	family                     = null;
    }
    public void setBean( AllCountryOlimpDiplomBean bean, HttpServletRequest request, ActionErrors errors) throws ServletException {

        if ( bean != null ) {
        	   if ( bean.getName() != null ) {
                   name = bean.getName();
               }
        	   if ( bean.getFamily() != null ) {
                   family = bean.getFamily();
               }
        }
    }
    public AllCountryOlimpDiplomBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

    	AllCountryOlimpDiplomBean bean = new AllCountryOlimpDiplomBean();
        if ( family!=null && !family.equals("") ) {
            bean.setFamily(StringUtil.toDB(family));
        }
        if ( name!=null && !name.equals("") ) {
            bean.setName(StringUtil.toDB(name));
        }
        return bean;
    }

    
}
