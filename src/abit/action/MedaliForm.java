package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class MedaliForm extends ActionForm {

    private Integer kodMedali   = null;
    private String  shifrMedali = null;
    private String  medal       = null;
    private String  action      = null;

    public Integer  getKodMedali()           { return kodMedali;           }
    public String   getShifrMedali()         { return shifrMedali;         }
    public String   getMedal()               { return medal; }
    public String   getAction()              { return action;              }

    public void setKodMedali(Integer value)          { kodMedali           = value;        }
    public void setShifrMedali(String value)         { shifrMedali         = value.trim(); }
    public void setMedal(String value)               { medal = value.trim(); }
    public void setAction(String value)              { action              = value.trim(); }

    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
        kodMedali           = null;
        shifrMedali         = null;
        medal               = null;
        action              = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors ) throws ServletException {

        if ( bean != null) {

             kodMedali = bean.getKodMedali();

             if ( bean.getShifrMedali() != null) {
                  shifrMedali = bean.getShifrMedali();
             }
             if ( bean.getMedal() != null) {
                  medal = bean.getMedal();
             }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setKodMedali(kodMedali);

        if (shifrMedali!=null && !shifrMedali.equals("")) {
            bean.setShifrMedali(StringUtil.toDB(shifrMedali));
        }
        if (medal!=null && !medal.equals("")) {
            bean.setMedal(StringUtil.toDB(medal));
        }
        return bean;
    }      
}