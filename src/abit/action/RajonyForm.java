package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class RajonyForm extends ActionForm {

    private Integer kodRajona      = null;
    private String  nazvanieRajona = null;
    private String  action         = null;

    public Integer getKodRajona()      { return kodRajona;      }
    public String  getNazvanieRajona() { return nazvanieRajona; }
    public String  getAction()         { return action;         }

    public void setKodRajona(Integer value)     { kodRajona      = value;        }
    public void setNazvanieRajona(String value) { nazvanieRajona = value.trim(); }
    public void setAction(String value)         { action         = value.trim(); }

    public void reset(ActionMapping mapping, HttpServletRequest request )
    {
        kodRajona      = null;
        nazvanieRajona = null;
        action         = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors) throws ServletException {

        if ( bean != null ) {

            kodRajona = bean.getKodRajona();

            if ( bean.getNazvanieRajona() != null ) {
                nazvanieRajona = bean.getNazvanieRajona();
            }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setKodRajona(kodRajona);

        if ( nazvanieRajona!=null && !nazvanieRajona.equals("") ) {
            bean.setNazvanieRajona(StringUtil.toDB(nazvanieRajona));
        }
        return bean;
    }      
}