package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class ZavedenijaForm extends ActionForm {

    private Integer kodZavedenija                = null;
    private String  polnoeNaimenovanieZavedenija = null;
    private String  sokr                         = null;
    private String  action                       = null;

    public Integer getKodZavedenija()                { return kodZavedenija;                }
    public String  getPolnoeNaimenovanieZavedenija() { return polnoeNaimenovanieZavedenija; }
    public String  getSokr()                         { return sokr;                         }
    public String  getAction()                       { return action;                       }

    public void setKodZavedenija(Integer value)               { kodZavedenija                = value;        }
    public void setPolnoeNaimenovanieZavedenija(String value) { polnoeNaimenovanieZavedenija = value.trim(); }
    public void setSokr(String value)                         { sokr                         = value.trim(); }
    public void setAction(String value)                       { action                       = value.trim(); }

    public void reset(ActionMapping mapping, HttpServletRequest request )
    {
        kodZavedenija                = null;
        polnoeNaimenovanieZavedenija = null;
        sokr                         = null;
        action                       = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors) throws ServletException {

        if ( bean != null ) {

            kodZavedenija = bean.getKodZavedenija();

            if ( bean.getPolnoeNaimenovanieZavedenija() != null ) {
                polnoeNaimenovanieZavedenija = bean.getPolnoeNaimenovanieZavedenija();
            }
            if ( bean.getSokr() != null ) {
                sokr = bean.getSokr();
            }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setKodZavedenija(kodZavedenija);

        if ( polnoeNaimenovanieZavedenija!=null && !polnoeNaimenovanieZavedenija.equals("") ) {
            bean.setPolnoeNaimenovanieZavedenija(StringUtil.toDB(polnoeNaimenovanieZavedenija));
        }
        if ( sokr!=null && !sokr.equals("") ) {
            bean.setSokr(StringUtil.toDB(sokr));
        }
        return bean;
    }      
}