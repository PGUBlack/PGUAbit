package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;
import abit.Constants;

public class LogoutForm extends ActionForm {

    private String  pass, uid;
    private Integer kodVuza          = null;
    private String  abbreviaturaVuza = null;


    public Integer  getKodVuza()          { return kodVuza;          }
    public String   getAbbreviaturaVuza() { return abbreviaturaVuza; }

    public void setKodVuza(Integer value)         { kodVuza          = value;        }
    public void setAbbreviaturaVuza(String value) { abbreviaturaVuza = value.trim(); }

    public LogoutForm() {
        reset(null,null);
    }

    public String getPass() {
     return pass;
    }

    public void setPass(String value) {
     pass = value;
    }

    public String getUid() {
     return uid;
    }

    public void setUid(String value) {
     uid = value;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
     pass = Constants.EMPTY;
     uid = Constants.EMPTY;
     kodVuza          = null;
     abbreviaturaVuza = null;

    }
        

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors ) throws ServletException {

        if ( bean != null) {

             kodVuza = bean.getKodVuza();

             if ( bean.getAbbreviaturaVuza() != null) {
                  abbreviaturaVuza = bean.getAbbreviaturaVuza();
             }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setKodVuza(kodVuza);

        if (abbreviaturaVuza!=null && !abbreviaturaVuza.equals("")) {
            bean.setAbbreviaturaVuza(StringUtil.toDB(abbreviaturaVuza));
        }

        return bean;
    }      

}

