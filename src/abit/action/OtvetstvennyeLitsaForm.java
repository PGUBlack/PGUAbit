package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class OtvetstvennyeLitsaForm extends ActionForm {

    private Integer kodZapisi = null;
    private String  doljnost  = null;
    private String  fio       = null;
    private String  action      = null;

    public Integer getKodZapisi() { return kodZapisi; }
    public String  getDoljnost()  { return doljnost;  }
    public String  getFio()       { return fio;       }
    public String  getAction()    { return action;    }

    public void setKodZapisi(Integer value) { kodZapisi = value;        }
    public void setDoljnost(String value)   { doljnost  = value.trim(); }
    public void setFio(String value)        { fio       = value.trim(); }
    public void setAction(String value)     { action    = value.trim(); }

    public void reset(ActionMapping mapping, HttpServletRequest request )
    {
        kodZapisi = null;
        doljnost  = null;
        fio       = null;
        action      = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors) throws ServletException {

        if ( bean != null ) {

            kodZapisi = bean.getKodZapisi();

            if ( bean.getDoljnost() != null ) {
                doljnost = bean.getDoljnost();
            }
            if ( bean.getFio() != null ) {
                fio = bean.getFio();
            }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setKodZapisi(kodZapisi);

        if ( doljnost!=null && !doljnost.equals("") ) {
            bean.setDoljnost(StringUtil.toDB(doljnost));
        }
        if ( fio!=null && !fio.equals("") ) {
            bean.setFio(StringUtil.toDB(fio));
        }
        return bean;
    }      
}