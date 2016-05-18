package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class LgotyForm extends ActionForm {

    private Integer kodLgot   = null;
    private String  lgoty     = null;
    private String  shifrLgot = null;
    private String  action    = null;

    public Integer  getKodLgot()   { return kodLgot;    }
    public String   getLgoty()     { return lgoty;      }
    public String   getShifrLgot() { return shifrLgot;  }
    public String   getAction()    { return action;     }

    public void setKodLgot(Integer value)          { kodLgot   = value;        }
    public void setLgoty(String value)             { lgoty     = value.trim(); }
    public void setShifrLgot(String value)         { shifrLgot = value.trim(); }
    public void setAction(String value)            { action    = value.trim(); }

    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
        kodLgot   = null;
        shifrLgot = null;
        lgoty     = null;
        action    = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors ) throws ServletException {

        if ( bean != null) {

             kodLgot = bean.getKodLgot();

             if ( bean.getShifrLgot() != null) {
                  shifrLgot = bean.getShifrLgot();
             }
             if ( bean.getLgoty() != null) {
                  lgoty = bean.getLgoty();
             }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setKodLgot(kodLgot);

        if (shifrLgot!=null && !shifrLgot.equals("")) {
            bean.setShifrLgot(StringUtil.toDB(shifrLgot));
        }
        if (lgoty!=null && !lgoty.equals("")) {
            bean.setLgoty(StringUtil.toDB(lgoty));
        }
        return bean;
    }      
}