package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class NazvanijaPredmetovForm extends ActionForm {

    private Integer kodPredmeta = null;
    private String  predmet     = null;
    private String  datelnyj    = null;
    private String  sokr        = null;
    private String  action      = null;

    public Integer getKodPredmeta() { return kodPredmeta; }
    public String  getPredmet()     { return predmet;     }
    public String  getDatelnyj()    { return datelnyj;    }
    public String  getSokr()        { return sokr;        }
    public String  getAction()      { return action;      }

    public void setKodPredmeta(Integer value) { kodPredmeta = value;        }
    public void setPredmet(String value)      { predmet     = value.trim(); }
    public void setDatelnyj(String value)     { datelnyj    = value.trim(); }
    public void setSokr(String value)         { sokr        = value.trim(); }
    public void setAction(String value)       { action      = value.trim(); }

    public void reset(ActionMapping mapping, HttpServletRequest request )
    {
        kodPredmeta = null;
        predmet     = null;
        datelnyj    = null;
        sokr        = null;
        action      = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors) throws ServletException {

        if ( bean != null ) {

            kodPredmeta = bean.getKodPredmeta();

            if ( bean.getPredmet() != null ) {
                predmet = bean.getPredmet();
            }
            if ( bean.getDatelnyj() != null ) {
                datelnyj = bean.getDatelnyj();
            }
            if ( bean.getSokr() != null ) {
                sokr = bean.getSokr();
            }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setKodPredmeta(kodPredmeta);

        if ( predmet!=null && !predmet.equals("") ) {
            bean.setPredmet(StringUtil.toDB(predmet));
        }
        if ( datelnyj!=null && !datelnyj.equals("") ) {
            bean.setDatelnyj(StringUtil.toDB(datelnyj));
        }
        if ( sokr!=null && !sokr.equals("") ) {
            bean.setSokr(StringUtil.toDB(sokr));
        }
        return bean;
    }      
}