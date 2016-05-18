package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class GoFBSForm extends ActionForm {

    private Integer kodZapisi  = null;
    private String  seriaAtt   = null;
    private String  special1   = "one";
    private String  special2   = null;
    private String  special3   = null;
    private String  nomerAtt   = null;
    private String  action     = null;

    public Integer  getKodZapisi()  { return kodZapisi;   }
    public String   getSeriaAtt()   { return seriaAtt;    }
    public String   getSpecial1()   { return special1;    }
    public String   getSpecial2()   { return special2;    }
    public String   getSpecial3()   { return special3;    }
    public String   getNomerAtt()   { return nomerAtt;    }
    public String   getAction()     { return action;      }

    public void setKodZapisi(Integer value)  { kodZapisi = value;        }
    public void setSpecial1(String value)    { special1  = value.trim(); }
    public void setSpecial2(String value)    { special2  = value.trim(); }
    public void setSpecial3(String value)    { special3  = value.trim(); }
    public void setSeriaAtt(String value)    { seriaAtt  = value.trim(); }
    public void setNomerAtt(String value)    { nomerAtt  = value.trim(); }
    public void setAction(String value)      { action    = value.trim(); }

    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
        kodZapisi   = null;
        nomerAtt    = null;
        seriaAtt    = null;
        special1    = "one";
        special2    = null;
        special3    = null;
        action      = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors ) throws ServletException {

        if ( bean != null) {

             kodZapisi = bean.getKodZapisi();

             if ( bean.getNomerAtt() != null) {
                  nomerAtt = bean.getNomerAtt();
             }
             if ( bean.getSeriaAtt() != null) {
                  seriaAtt = bean.getSeriaAtt();
             }
             if ( bean.getSpecial1() != null) {
                  special1 = bean.getSpecial1();
             }
             if ( bean.getSpecial2() != null) {
                  special2 = bean.getSpecial2();
             }
             if ( bean.getSpecial3() != null) {
                  special3 = bean.getSpecial3();
             }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setKodZapisi(kodZapisi);

        if (nomerAtt!=null && !nomerAtt.equals("")) {
            bean.setNomerAtt(StringUtil.toDB(nomerAtt));
        }
        if (seriaAtt!=null && !seriaAtt.equals("")) {
            bean.setSeriaAtt(StringUtil.toDB(seriaAtt));
        }
        if (special1!=null && !special1.equals("")) {
            bean.setSpecial1(StringUtil.toDB(special1));
        }
        if (special2!=null && !special2.equals("")) {
            bean.setSpecial2(StringUtil.toDB(special2));
        }
        if (special3!=null && !special3.equals("")) {
            bean.setSpecial3(StringUtil.toDB(special3));
        }
        return bean;
    }      
}