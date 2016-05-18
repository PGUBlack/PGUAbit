package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class OblastiForm extends ActionForm {

    private Integer kodOblasti      = null;
    private String  nazvanieOblasti = null;
    private String  action                       = null;

    public Integer getkodOblasti()      { return kodOblasti;      }
    public String  getnazvanieOblasti() { return nazvanieOblasti; }
    public String  getAction()          { return action;          }

    public void setkodOblasti(Integer value)     { kodOblasti      = value;        }
    public void setnazvanieOblasti(String value) { nazvanieOblasti = value.trim(); }
    public void setAction(String value)          { action          = value.trim(); }

    public void reset(ActionMapping mapping, HttpServletRequest request )
    {
        kodOblasti      = null;
        nazvanieOblasti = null;
        action          = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors) throws ServletException {

        if ( bean != null ) {

            kodOblasti = bean.getKodOblasti();

            if ( bean.getNazvanieOblasti() != null ) {
                nazvanieOblasti = bean.getNazvanieOblasti();
            }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setKodOblasti(kodOblasti);

        if ( nazvanieOblasti!=null && !nazvanieOblasti.equals("") ) {
            bean.setNazvanieOblasti(StringUtil.toDB(nazvanieOblasti));
        }
        return bean;
    }      
}