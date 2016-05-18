package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class KursyForm extends ActionForm {

    private Integer kodKursov           = null;
    private String  informatsijaOKursah = null;
    private String  shifrKursov         = null;
    private String  action              = null;

    public Integer  getKodKursov()           { return kodKursov;           }
    public String   getInformatsijaOKursah() { return informatsijaOKursah; }
    public String   getShifrKursov()         { return shifrKursov;         }
    public String   getAction()              { return action;              }

    public void setKodKursov(Integer value)          { kodKursov           = value;        }
    public void setInformatsijaOKursah(String value) { informatsijaOKursah = value.trim(); }
    public void setShifrKursov(String value)         { shifrKursov         = value.trim(); }
    public void setAction(String value)              { action              = value.trim(); }

    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
        kodKursov           = null;
        shifrKursov         = null;
        informatsijaOKursah = null;
        action              = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors ) throws ServletException {

        if ( bean != null) {

             kodKursov = bean.getKodKursov();

             if ( bean.getShifrKursov() != null) {
                  shifrKursov = bean.getShifrKursov();
             }
             if ( bean.getInformatsijaOKursah() != null) {
                  informatsijaOKursah = bean.getInformatsijaOKursah();
             }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setKodKursov(kodKursov);

        if (shifrKursov!=null && !shifrKursov.equals("")) {
            bean.setShifrKursov(StringUtil.toDB(shifrKursov));
        }
        if (informatsijaOKursah!=null && !informatsijaOKursah.equals("")) {
            bean.setInformatsijaOKursah(StringUtil.toDB(informatsijaOKursah));
        }
        return bean;
    }      
}