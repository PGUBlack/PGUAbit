package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class NazvanieVuzaForm extends ActionForm {

    private Integer kodVuza          = null;
    private String  nazvanieVuza     = null;
    private String  nazvanieRodit    = null;
    private String  abbreviaturaVuza = null;
    private String  postAdresVuza    = null;
    private String  action           = null;

    public Integer  getKodVuza()          { return kodVuza;          }
    public String   getNazvanieVuza()     { return nazvanieVuza;     }
    public String   getNazvanieRodit()    { return nazvanieRodit;    }
    public String   getAbbreviaturaVuza() { return abbreviaturaVuza; }
    public String   getPostAdresVuza()    { return postAdresVuza;    }
    public String   getAction()           { return action;           }

    public void setKodVuza(Integer value)         { kodVuza          = value;        }
    public void setNazvanieVuza(String value)     { nazvanieVuza     = value.trim(); }
    public void setNazvanieRodit(String value)    { nazvanieRodit    = value.trim(); }
    public void setAbbreviaturaVuza(String value) { abbreviaturaVuza = value.trim(); }
    public void setPostAdresVuza(String value)    { postAdresVuza    = value.trim(); }
    public void setAction(String value)           { action           = value.trim(); }

    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
        kodVuza          = null;
        nazvanieVuza     = null;
        nazvanieRodit    = null;
        abbreviaturaVuza = null;
        postAdresVuza    = null;
        action           = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors ) throws ServletException {

        if ( bean != null) {

             kodVuza = bean.getKodVuza();

             if ( bean.getNazvanieVuza() != null) {
                  nazvanieVuza = bean.getNazvanieVuza();
             }
             if ( bean.getNazvanieRodit() != null) {
                  nazvanieRodit = bean.getNazvanieRodit();
             }
             if ( bean.getAbbreviaturaVuza() != null) {
                  abbreviaturaVuza = bean.getAbbreviaturaVuza();
             }
             if ( bean.getPostAdresVuza() != null) {
                  postAdresVuza = bean.getPostAdresVuza();
             }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setKodVuza(kodVuza);

        if (nazvanieVuza!=null && !nazvanieVuza.equals("")) {
            bean.setNazvanieVuza(StringUtil.toDB(nazvanieVuza));
        }
        if (nazvanieRodit!=null && !nazvanieRodit.equals("")) {
            bean.setNazvanieRodit(StringUtil.toDB(nazvanieRodit));
        }
        if (abbreviaturaVuza!=null && !abbreviaturaVuza.equals("")) {
            bean.setAbbreviaturaVuza(StringUtil.toDB(abbreviaturaVuza));
        }
        if (postAdresVuza!=null && !postAdresVuza.equals("")) {
            bean.setPostAdresVuza(StringUtil.toDB(postAdresVuza));
        }

        return bean;
    }      
}