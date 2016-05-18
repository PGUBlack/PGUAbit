package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class EkzamenyNaSpetsialnostiForm extends ActionForm {

    private Integer kodZapisi        = null;
    private Integer kodPredmeta      = null;
    private Integer kodVuza          = null;
    private String  special2         = null;
    private String  special1         = null;
    private String  action           = null;
    private String  prioritet                          = null;

    public Integer getKodZapisi()        { return kodZapisi;        }
    public Integer getKodVuza()          { return kodVuza;          }
    public Integer getKodPredmeta()      { return kodPredmeta;      }
    public String  getSpecial2()         { return special2;         }
    public String  getSpecial1()         { return special1;         }
    public String  getAction()           { return action;           }
    public String  getPrioritet()           { return prioritet;           }

    public void setKodZapisi(Integer value)        { kodZapisi        = value;        }
    public void setKodPredmeta(Integer value)      { kodPredmeta      = value;        }
    public void setKodVuza(Integer value)          { kodVuza          = value;        }
    public void setSpecial2(String value)          { special2         = value.trim(); }
    public void setSpecial1(String value)          { special1         = value.trim(); }
    public void setAction(String value)            { action           = value.trim(); }
    public void setPrioritet(String value)            { prioritet           = value.trim(); }
    
    public void reset(ActionMapping mapping, HttpServletRequest request )
    {
        kodZapisi        = null;
        kodPredmeta      = null;
        kodVuza          = null;
        special2         = null;
        special1         = null;
        action           = null;
        prioritet           = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors) throws ServletException {

        if ( bean != null ) {

            kodZapisi = bean.getKodZapisi();

            kodPredmeta = bean.getKodPredmeta();

            kodVuza = bean.getKodVuza();

            if ( bean.getSpecial2() != null ) {
                special2 = bean.getSpecial2();
            }
            if ( bean.getSpecial1() != null ) {
                special1 = bean.getSpecial1();
            }
            if ( bean.getPrioritet() != null ) {
                prioritet = bean.getPrioritet();
            }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setKodZapisi(kodZapisi);

        bean.setKodVuza(kodVuza);

        bean.setKodPredmeta(kodPredmeta);

        if ( special2!=null && !special2.equals("") ) {
            bean.setSpecial2(StringUtil.toDB(special2));
        }
        if ( special1!=null && !special1.equals("") ) {
            bean.setSpecial1(StringUtil.toDB(special1));
        }
        if ( prioritet!=null && !prioritet.equals("") ) {
            bean.setPrioritet(StringUtil.toDB(prioritet));
        }
        return bean;
    }      
}