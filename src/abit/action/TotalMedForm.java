package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class TotalMedForm extends ActionForm {

    private Integer kodZapisi                  = null;
    private Integer kodFakulteta               = null;
    private Integer kodMedali                  = null;
    private Integer kodVuza                    = null;
    private Integer nomerPotoka                = null;
    private String  abbreviaturaFakulteta      = null;
    private Integer vsegoMed                   = null;
    private String  shifrMedali                = null;
    private String  podtvMed                   = null;
    private String  action                     = null;

    public Integer getKodZapisi()                  { return kodZapisi;                  }
    public Integer getKodVuza()                    { return kodVuza;                    }
    public Integer getKodFakulteta()               { return kodFakulteta;               }
    public Integer getKodMedali()                  { return kodMedali;                  }
    public Integer getNomerPotoka()                { return nomerPotoka;                }
    public String  getAbbreviaturaFakulteta()      { return abbreviaturaFakulteta;      }
    public Integer getVsegoMed()                   { return vsegoMed;                   }
    public String  getShifrMedali()                { return shifrMedali;                }
    public String  getPodtvMed()                   { return podtvMed;                   }
    public String  getAction()                     { return action;                     }

    public void setKodZapisi(Integer value)                  { kodZapisi                  = value;        }
    public void setKodFakulteta(Integer value)               { kodFakulteta               = value;        }
    public void setKodMedali(Integer value)                  { kodMedali                  = value;        }
    public void setKodVuza(Integer value)                    { kodVuza                    = value;        }
    public void setNomerPotoka(Integer value)                { nomerPotoka                = value;        }
    public void setAbbreviaturaFakulteta(String value)       { abbreviaturaFakulteta      = value;        }
    public void setVsegoMed(Integer value)                   { vsegoMed                   = value;        }
    public void setShifrMedali(String value)                 { shifrMedali                = value.trim(); }
    public void setPodtvMed(String value)                    { podtvMed                   = value.trim(); }
    public void setAction(String value)                      { action                     = value.trim(); }

    public void reset(ActionMapping mapping, HttpServletRequest request )
    {
        kodZapisi                  = null;
        kodFakulteta               = null;
        kodMedali                  = null;
        kodVuza                    = null;
        nomerPotoka                = null;
        abbreviaturaFakulteta      = null;
        vsegoMed                   = null;
        shifrMedali                = null;
        podtvMed                   = null;
        action                     = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors) throws ServletException {

        if ( bean != null ) {

            kodZapisi = bean.getKodZapisi();

            kodFakulteta = bean.getKodFakulteta();

            kodMedali = bean.getKodMedali();

            kodVuza = bean.getKodVuza();

            nomerPotoka = bean.getNomerPotoka();

            abbreviaturaFakulteta = bean.getAbbreviaturaFakulteta();

            vsegoMed = bean.getVsegoMed();

            if ( bean.getShifrMedali() != null ) {
                shifrMedali = bean.getShifrMedali();
            }

            if ( bean.getPodtvMed() != null ) {
                podtvMed = bean.getPodtvMed();
            }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setKodZapisi(kodZapisi);

        bean.setKodVuza(kodVuza);

        bean.setNomerPotoka(nomerPotoka);

        bean.setAbbreviaturaFakulteta(abbreviaturaFakulteta);

        bean.setVsegoMed(vsegoMed);

        bean.setKodFakulteta(kodFakulteta);

        bean.setKodMedali(kodMedali);

        if ( shifrMedali!=null && !shifrMedali.equals("") ) {
            bean.setShifrMedali(StringUtil.toDB(shifrMedali));
        }

        if ( podtvMed!=null && !podtvMed.equals("") ) {
            bean.setPodtvMed(StringUtil.toDB(podtvMed));
        }
        return bean;
    }      
}