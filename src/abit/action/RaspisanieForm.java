package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class RaspisanieForm extends ActionForm {

    private String  action                 = null;
    private String  auditorijaJekzamena    = null;
    private String  auditorijaKonsultatsii = null;
    private String  dataJekzamena          = null;
    private String  dataKonsultatsii       = null;
    private String  priznakSortirovki      = null;
    private String  special1               = null;
    private String  gruppa                 = null;
    private Integer kodGruppy              = null;
    private Integer kodFakulteta           = null;
    private Integer kodPredmeta            = null;
    private Integer kodRaspisanija         = null;
    private Integer nomerPotoka            = null;
    private Integer stolbetsSortirovki     = null;

    public String   getAction()                 { return action;                 }
    public String   getAuditorijaJekzamena()    { return auditorijaJekzamena;    }
    public String   getAuditorijaKonsultatsii() { return auditorijaKonsultatsii; }
    public String   getDataJekzamena()          { return dataJekzamena;          }
    public String   getDataKonsultatsii()       { return dataKonsultatsii;       }
    public String   getPriznakSortirovki()      { return priznakSortirovki;      }
    public String   getGruppa()                 { return gruppa;               }
    public String   getSpecial1()               { return special1;               }
    public Integer  getKodGruppy()              { return kodGruppy;              }
    public Integer  getKodFakulteta()           { return kodFakulteta;           }
    public Integer  getKodPredmeta()            { return kodPredmeta;            }
    public Integer  getKodRaspisanija()         { return kodRaspisanija;         }
    public Integer  getNomerPotoka()            { return nomerPotoka;            }
    public Integer  getStolbetsSortirovki()     { return stolbetsSortirovki;     }

    public void setAction(String value)                 { action                 = value.trim(); }
    public void setAuditorijaJekzamena(String value)    { auditorijaJekzamena    = value.trim(); }
    public void setAuditorijaKonsultatsii(String value) { auditorijaKonsultatsii = value.trim(); }
    public void setDataJekzamena(String value)          { dataJekzamena          = value.trim(); }
    public void setDataKonsultatsii(String value)       { dataKonsultatsii       = value.trim(); }
    public void setGruppa(String value)                 { gruppa               = value.trim(); }
    public void setSpecial1(String value)               { special1               = value.trim(); }
    public void setKodGruppy(Integer value)             { kodGruppy              = value;        }
    public void setKodFakulteta(Integer value)          { kodFakulteta           = value;        }
    public void setKodPredmeta(Integer value)           { kodPredmeta            = value;        }
    public void setKodRaspisanija(Integer value)        { kodRaspisanija         = value;        }
    public void setNomerPotoka(Integer value)           { nomerPotoka            = value;        }
    public void setPriznakSortirovki(String value)      { priznakSortirovki      = value.trim(); }
    public void setStolbetsSortirovki(Integer value)    { stolbetsSortirovki     = value;        }

    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
        action                 = null;
        auditorijaJekzamena    = null;
        auditorijaKonsultatsii = null;
        dataJekzamena          = null;
        dataKonsultatsii       = null;
        gruppa                 = null;
        special1               = null;
        kodRaspisanija         = null;
        kodFakulteta           = null;
        kodGruppy              = null;
        priznakSortirovki      = null;
        stolbetsSortirovki     = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors ) throws ServletException {

        if ( bean != null) {

             kodGruppy = bean.getKodGruppy();

             kodRaspisanija = bean.getKodRaspisanija();

             kodFakulteta = bean.getKodFakulteta();

             kodPredmeta = bean.getKodPredmeta();

             nomerPotoka = bean.getNomerPotoka();

             stolbetsSortirovki = bean.getStolbetsSortirovki();

             if ( bean.getAuditorijaJekzamena() != null) {
                  auditorijaJekzamena = bean.getAuditorijaJekzamena();
             }

             if ( bean.getAuditorijaKonsultatsii() != null) {
                  auditorijaKonsultatsii = bean.getAuditorijaKonsultatsii();
             }

             if ( bean.getGruppa() != null) {
                  gruppa = bean.getGruppa();
             }

             if ( bean.getSpecial1() != null) {
                  special1 = bean.getSpecial1();
             }

             if ( bean.getDataJekzamena() != null) {
                  dataJekzamena = bean.getDataJekzamena();
             }

             if ( bean.getDataKonsultatsii() != null) {
                  dataKonsultatsii = bean.getDataKonsultatsii();
             }
 
             if (bean.getPriznakSortirovki()!=null) {
                 priznakSortirovki = bean.getPriznakSortirovki();
             }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setKodGruppy(kodGruppy);

        bean.setKodRaspisanija(kodRaspisanija);

        bean.setKodFakulteta(kodFakulteta);

        bean.setKodPredmeta(kodPredmeta);

        bean.setNomerPotoka(nomerPotoka);

        bean.setStolbetsSortirovki(stolbetsSortirovki);

        if (auditorijaJekzamena!=null && !auditorijaJekzamena.equals("")) {
            bean.setAuditorijaJekzamena(StringUtil.toDB(auditorijaJekzamena));
        }

        if (gruppa!=null && !gruppa.equals("")) {
            bean.setGruppa(StringUtil.toDB(gruppa));
        }

        if (special1!=null && !special1.equals("")) {
            bean.setSpecial1(StringUtil.toDB(special1));
        }

        if (auditorijaKonsultatsii!=null && !auditorijaKonsultatsii.equals("")) {
            bean.setAuditorijaKonsultatsii(StringUtil.toDB(auditorijaKonsultatsii));
        }

        if (dataJekzamena!=null && !dataJekzamena.equals("")) {
            bean.setDataJekzamena(StringUtil.toDB(dataJekzamena));
        }

        if (dataKonsultatsii!=null && !dataKonsultatsii.equals("")) {
            bean.setDataKonsultatsii(StringUtil.toDB(dataKonsultatsii));
        }

        if (priznakSortirovki!=null && !priznakSortirovki.equals("")) {
            bean.setPriznakSortirovki(StringUtil.toDB(priznakSortirovki));
        }

        return bean;
    }      
}