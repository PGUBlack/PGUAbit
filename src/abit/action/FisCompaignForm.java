package abit.action;

import org.apache.struts.action.ActionForm;

public class FisCompaignForm extends ActionForm{

	
	private String    action          = null;
	private String nameCompaign;
	

	 public String   getNameCompaign()       { return nameCompaign;         }
	 
	 public void setNameCompaign(String value)          { nameCompaign        = value.trim(); }
	
	 public String   getAction()       { return action;         }
	 
	 public void setAction(String value)          { action        = value.trim(); }
	
}

