package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class PrikazForm extends ActionForm {

    private String  action                 = null;
    private String  abbreviatura           = null;
    private String  abbreviaturaFakulteta  = null;
    private String  forma_Pr               = null;
    private String  in_Prik                = null;
    private String  priznakSortirovki      = "budgetniki";
    private String  shifrFakulteta         = null;
    private String  nazvanie               = null;
    private String  special2               = null;
    private String  description            = null;
    private String  nomerLichnogoDela      = null;
    private String  nomerPrik              = null;
    private String  dataPrik               = null;
    private Integer kodFakulteta           = null;
    private Integer kodPrikaza             = null;

    public String  getAction()                { return action;                }    
    public String  getForma_Pr()              { return forma_Pr;              }
    public String  getIn_Prik()               { return in_Prik;               }
    public String  getPriznakSortirovki()     { return priznakSortirovki;     }
    public String  getShifrFakulteta()        { return shifrFakulteta; 	      }
    public String  getNazvanie()              { return nazvanie;              }
    public String  getSpecial2()              { return special2;              }
    public String  getDescription()           { return description;           }
    public String  getNomerLichnogoDela()     { return nomerLichnogoDela;     }
    public String  getNomerPrik()             { return nomerPrik;             }
    public String  getDataPrik()              { return dataPrik;              }
    public Integer getKodFakulteta()          { return kodFakulteta;          }
    public Integer getKodPrikaza()            { return kodPrikaza;            }

    public void setShifrFakulteta(String value)	       { shifrFakulteta         = value.trim(); }
    public void setPriznakSortirovki(String value)     { priznakSortirovki      = value.trim(); }
    public void setNazvanie(String value)              { nazvanie               = value.trim(); }
    public void setSpecial2(String value)              { special2               = value.trim(); }
    public void setDescription(String value)           { description            = value.trim(); }
    public void setNomerLichnogoDela(String value)     { nomerLichnogoDela      = value.trim(); }
    public void setNomerPrik(String value)             { nomerPrik              = value.trim(); }
    public void setDataPrik(String value)              { dataPrik               = value.trim(); }
    public void setForma_Pr(String value)              { forma_Pr               = value.trim(); }
    public void setIn_Prik(String value)               { in_Prik                = value.trim(); }
    public void setAction(String value)                { action                 = value.trim(); }
    public void setKodFakulteta(Integer value)         { kodFakulteta           = value;        }
    public void setKodPrikaza(Integer value)           { kodPrikaza             = value;        }

    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
        action                = null;
        forma_Pr              = null;
        in_Prik               = null;
        shifrFakulteta        = null;
        priznakSortirovki     = "budgetniki";
        nazvanie              = null;
        special2              = null;
        description           = null;
        nomerLichnogoDela     = null;
        nomerPrik             = null;
        dataPrik              = null;
        kodFakulteta          = null;
        kodPrikaza            = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors ) throws ServletException {

        if ( bean != null) {

            kodFakulteta = bean.getKodFakulteta();

            kodPrikaza = bean.getKodPrikaza();

            if ( bean.getShifrFakulteta() != null ) {
                shifrFakulteta = bean.getShifrFakulteta();
		}
            if ( bean.getForma_Pr() != null ) {
                forma_Pr = bean.getForma_Pr();
            }
            if ( bean.getIn_Prik() != null ) {
                in_Prik = bean.getIn_Prik();
            }
            if ( bean.getPriznakSortirovki() != null ) {
                priznakSortirovki = bean.getPriznakSortirovki();
		}
            if ( bean.getNazvanie() != null ) {
                nazvanie = bean.getNazvanie();
            }
            if ( bean.getSpecial2() != null ) {
                special2 = bean.getSpecial2();
            }
            if ( bean.getDescription() != null ) {
                description = bean.getDescription();
            }
            if ( bean.getNomerLichnogoDela() != null ) {
                nomerLichnogoDela = bean.getNomerLichnogoDela();
            }
            if ( bean.getNomerPrik() != null ) {
                nomerPrik = bean.getNomerPrik();
            }
            if ( bean.getDataPrik() != null ) {
                dataPrik = bean.getDataPrik();
            }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      
  
        bean.setKodFakulteta(kodFakulteta);

        bean.setKodPrikaza(kodPrikaza);

        if ( shifrFakulteta!=null && !shifrFakulteta.equals("") ) {
            bean.setShifrFakulteta(StringUtil.toDB(shifrFakulteta));
        }
        if ( forma_Pr!=null && !forma_Pr.equals("") ) {
            bean.setForma_Pr(StringUtil.toDB(forma_Pr));
        }
        if ( in_Prik!=null && !in_Prik.equals("") ) {
            bean.setIn_Prik(StringUtil.toDB(in_Prik));
        }
        if ( priznakSortirovki!=null && !priznakSortirovki.equals("") ) {
            bean.setPriznakSortirovki(StringUtil.toDB(priznakSortirovki));
        }
        if ( nazvanie!=null && !nazvanie.equals("") ) {
            bean.setNazvanie(StringUtil.toDB(nazvanie));
        }
        if ( special2!=null && !special2.equals("") ) {
            bean.setSpecial2(StringUtil.toDB(special2));
        }
        if ( description!=null && !description.equals("") ) {
            bean.setDescription(StringUtil.toDB(description));
        }
        if ( nomerLichnogoDela!=null && !nomerLichnogoDela.equals("") ) {
            bean.setNomerLichnogoDela(StringUtil.toDB(nomerLichnogoDela));
        }
        if ( nomerPrik!=null && !nomerPrik.equals("") ) {
            bean.setNomerPrik(StringUtil.toDB(nomerPrik));
        }
        if ( dataPrik!=null && !dataPrik.equals("") ) {
            bean.setDataPrik(StringUtil.toDB(dataPrik));
        }
        return bean;
    }      
}
