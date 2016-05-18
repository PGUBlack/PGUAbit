package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class TselevojPriemForm extends ActionForm {

    private Integer kodTselevogoPriema     = null;
    private String  shifrPriema   = null;
    private String  tselevojPriem = null;
    private String  action        = null;

    public Integer getKodTselevogoPriema()     { return kodTselevogoPriema;     }
    public String  getShifrPriema()   { return shifrPriema;   }
    public String  getTselevojPriem() { return tselevojPriem; }
    public String  getAction()        { return action;        }

    public void setKodTselevogoPriema(Integer value)    { kodTselevogoPriema     = value;        }
    public void setShifrPriema(String value)   { shifrPriema   = value.trim(); }
    public void setTselevojPriem(String value) { tselevojPriem = value.trim(); }
    public void setAction(String value)        { action        = value.trim(); }

    public void reset(ActionMapping mapping, HttpServletRequest request )
    {
        kodTselevogoPriema     = null;
        shifrPriema   = null;
        tselevojPriem = null;
        action        = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors) throws ServletException {

        if ( bean != null ) {

            kodTselevogoPriema = bean.getKodTselevogoPriema();

            if ( bean.getShifrPriema() != null ) {
                shifrPriema = bean.getShifrPriema();
            }
            if ( bean.getTselevojPriem() != null ) {
                tselevojPriem = bean.getTselevojPriem();
            }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setKodTselevogoPriema(kodTselevogoPriema);

        if ( shifrPriema!=null && !shifrPriema.equals("") ) {
            bean.setShifrPriema(StringUtil.toDB(shifrPriema));
        }
        if ( tselevojPriem!=null && !tselevojPriem.equals("") ) {
            bean.setTselevojPriem(StringUtil.toDB(tselevojPriem));
        }
        return bean;
    }      
}