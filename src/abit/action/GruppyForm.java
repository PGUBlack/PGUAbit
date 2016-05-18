package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class GruppyForm extends ActionForm {

    private Integer kodGruppy      = null;
    private Integer kodKonGrp      = null;
    private Integer kodFakulteta   = null;
    private String  shifrFakulteta = null;
    private String  dogovornaja    = null;
    private Integer nomerPotoka    = null;
    private String  gruppa         = null;
    private String  abbr           = null;
    private String  action         = null;

    public Integer  getKodGruppy()      { return kodGruppy;      }
    public Integer  getKodKonGrp()      { return kodKonGrp;      }
    public Integer  getKodFakulteta()   { return kodFakulteta;   }
    public String   getShifrFakulteta() { return shifrFakulteta; }
    public String   getDogovornaja()    { return dogovornaja;    }
    public Integer  getNomerPotoka()    { return nomerPotoka;    }
    public String   getGruppa()         { return gruppa;         }
    public String   getAbbr()           { return abbr;           }
    public String   getAction()         { return action;         }

    public void setKodGruppy(Integer value)     { kodGruppy      = value;        }
    public void setKodKonGrp(Integer value)     { kodKonGrp      = value;        }
    public void setKodFakulteta(Integer value)  { kodFakulteta   = value;        }
    public void setShifrFakulteta(String value) { shifrFakulteta = value.trim(); }
    public void setDogovornaja(String value)    { dogovornaja    = value.trim(); }
    public void setNomerPotoka(Integer value)   { nomerPotoka    = value;        }
    public void setGruppa(String value)         { gruppa         = value.trim(); }
    public void setAbbr(String value)           { abbr           = value.trim(); }
    public void setAction(String value)         { action         = value.trim(); }

    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
        kodGruppy      = null;
        kodKonGrp      = null;
        kodFakulteta   = null;
        shifrFakulteta = null;
        dogovornaja    = null;
        nomerPotoka    = null;
        gruppa         = null;
        abbr           = null;
        action         = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors ) throws ServletException {

        if ( bean != null) {

             kodGruppy = bean.getKodGruppy();
             kodKonGrp = bean.getKodKonGrp();
             kodFakulteta = bean.getKodFakulteta();
             nomerPotoka = bean.getNomerPotoka();
             if ( bean.getShifrFakulteta() != null) {
                  shifrFakulteta = bean.getShifrFakulteta();
             }
             if ( bean.getDogovornaja() != null) {
                  dogovornaja = bean.getDogovornaja();
             }
             if ( bean.getGruppa() != null) {
                  gruppa = bean.getGruppa();
             }
             if ( bean.getAbbr() != null) {
                  abbr = bean.getAbbr();
             }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setKodGruppy(kodGruppy);
        bean.setKodKonGrp(kodKonGrp);
        bean.setKodFakulteta(kodFakulteta);
        bean.setNomerPotoka(nomerPotoka);

        if (shifrFakulteta!=null && !shifrFakulteta.equals("")) {
            bean.setShifrFakulteta(StringUtil.toDB(shifrFakulteta));
        }
        if (dogovornaja!=null && !dogovornaja.equals("")) {
            bean.setDogovornaja(StringUtil.toDB(dogovornaja));
        }
        if (gruppa!=null && !gruppa.equals("")) {
            bean.setGruppa(StringUtil.toDB(gruppa));
        }
        if (abbr!=null && !abbr.equals("")) {
            bean.setAbbr(StringUtil.toDB(abbr));
        }

        return bean;
    }      
}