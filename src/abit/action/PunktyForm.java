package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class PunktyForm extends ActionForm {

    private Integer kodPunkta = null;
    private String  nazvanie  = null;
    private String  action    = null;

    public Integer getKodPunkta() { return kodPunkta; }
    public String  getNazvanie()  { return nazvanie;  }
    public String  getAction()    { return action;    }

    public void setKodPunkta(Integer value) { kodPunkta      = value;        }
    public void setNazvanie(String value)   { nazvanie = value.trim(); }
    public void setAction(String value)     { action         = value.trim(); }

    public void reset(ActionMapping mapping, HttpServletRequest request )
    {
        kodPunkta = null;
        nazvanie  = null;
        action    = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors) throws ServletException {

        if ( bean != null ) {

            kodPunkta = bean.getKodPunkta();

            if ( bean.getNazvanie() != null ) {
                nazvanie = bean.getNazvanie();
            }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setKodPunkta(kodPunkta);

        if ( nazvanie!=null && !nazvanie.equals("") ) {
            bean.setNazvanie(StringUtil.toDB(nazvanie));
        }
        return bean;
    }      
}