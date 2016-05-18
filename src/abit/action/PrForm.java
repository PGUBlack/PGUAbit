package abit.action;

import org.apache.struts.action.ActionForm;

public class PrForm extends ActionForm{

	
	private String    action          = null;
	private String[] selectedItems = {}; 
	private Integer[] selectedKods;
//	private String[] items = {"UPS","FedEx","Airborne"}; 
	public String[] getSelectedItems() { 
	  return this.selectedItems; 
	} 
	
	public Integer[] getSelectedKods(){
		return this.selectedKods;
	}
	
	public void setSelectedKods(Integer[] selectedKods){
		this.selectedKods = selectedKods;
	}
	public void setSelectedItems(String[] selectedHobbies) {
        this.selectedItems = selectedHobbies;
    }
	
	 public String   getAction()       { return action;         }
	 
	 public void setAction(String value)          { action        = value.trim(); }
	
}