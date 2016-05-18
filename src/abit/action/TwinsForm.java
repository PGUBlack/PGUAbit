package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class TwinsForm extends ActionForm {

    private Integer kodAbiturienta               = null;
    private String  special2                     = null;
    private String  tipDokumenta                 = null;
    private String  nomerDokumenta               = null;
    private String  familija                     = null;
    private String  imja                         = null;
    private String  otchestvo                    = null;
    private String  nomerLichnogoDela            = null;
    private String  action                       = null;

    public Integer getKodAbiturienta()    { return kodAbiturienta;     }
    public String  getSpecial2()          { return special2;           }
    public String  getTipDokumenta()      { return tipDokumenta;       }
    public String  getNomerDokumenta()    { return nomerDokumenta;     }
    public String  getFamilija()          { return familija;           }
    public String  getImja()              { return imja;               }
    public String  getOtchestvo()         { return otchestvo;          }
    public String  getNomerLichnogoDela() { return nomerLichnogoDela;  }
    public String  getAction()            { return action;             }

    public void setKodAbiturienta(Integer value)   { kodAbiturienta    = value;        }
    public void setSpecial2(String value)          { special2          = value.trim(); }
    public void setTipDokumenta(String value)      { tipDokumenta      = value.trim(); }
    public void setNomerDokumenta(String value)    { nomerDokumenta    = value.trim(); }
    public void setFamilija(String value)          { familija          = value.trim(); }
    public void setImja(String value)              { imja              = value.trim(); }
    public void setOtchestvo(String value)         { otchestvo         = value.trim(); }
    public void setNomerLichnogoDela(String value) { nomerLichnogoDela = value.trim(); }
    public void setAction(String value)            { action            = value.trim(); }

    public void reset(ActionMapping mapping, HttpServletRequest request )
    {
        kodAbiturienta      = null;
        special2            = null;
        tipDokumenta        = null;
        nomerDokumenta      = null;
        familija            = null;
        imja                = null;
        otchestvo           = null;
        nomerLichnogoDela   = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors) throws ServletException {

        if ( bean != null ) {

                kodAbiturienta     = bean.getKodAbiturienta();

            if ( bean.getTipDokumenta() != null ) {
                tipDokumenta = bean.getTipDokumenta();
            }
            if ( bean.getSpecial2() != null ) {
                special2 = bean.getSpecial2();
            }
            if ( bean.getNomerDokumenta() != null ) {
                nomerDokumenta = bean.getNomerDokumenta();
            }
            if ( bean.getFamilija() != null ) {
                familija = bean.getFamilija();
            }
            if ( bean.getImja() != null ) {
                imja = bean.getImja();
            }
            if ( bean.getOtchestvo() != null ) {
                otchestvo = bean.getOtchestvo();
            }
            if ( bean.getNomerLichnogoDela() != null ) {
                nomerLichnogoDela = bean.getNomerLichnogoDela();
            }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

            bean.setKodAbiturienta(kodAbiturienta);

        if ( tipDokumenta!=null && !tipDokumenta.equals("") ) {
            bean.setTipDokumenta(StringUtil.toDB(tipDokumenta));
        }
        if ( special2!=null && !special2.equals("") ) {
            bean.setSpecial2(StringUtil.toDB(special2));
        }
        if ( nomerDokumenta!=null && !nomerDokumenta.equals("") ) {
            bean.setNomerDokumenta(StringUtil.toDB(nomerDokumenta));
        }
        if ( familija!=null && !familija.equals("") ) {
            bean.setFamilija(StringUtil.toDB(familija));
        }
        if ( imja!=null && !imja.equals("") ) {
            bean.setImja(StringUtil.toDB(imja));
        }
        if ( otchestvo!=null && !otchestvo.equals("") ) {
            bean.setOtchestvo(StringUtil.toDB(otchestvo));
        }
        if ( nomerLichnogoDela!=null && !nomerLichnogoDela.equals("") ) {
            bean.setNomerLichnogoDela(StringUtil.toDB(nomerLichnogoDela));
        }
        return bean;
    }      
}