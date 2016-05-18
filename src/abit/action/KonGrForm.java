package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class KonGrForm extends ActionForm {

    private Integer kodKonGrp                  = null;
    private String  abbreviatura               = null;
    private String  action                     = null;
    private String  nazvanie                   = null;
    private String  nazvanieVRoditelnom        = null;
    private String  priznakSortirovki          = null;

    public Integer getKodKonGrp()                  { return kodKonGrp;                  }
    public String  getAbbreviatura()               { return abbreviatura;               }
    public String  getAction()                     { return action;                     }
    public String  getNazvanie()                   { return nazvanie;                   }
    public String  getNazvanieVRoditelnom()        { return nazvanieVRoditelnom;        }
    public String  getPriznakSortirovki()          { return priznakSortirovki;          }

    public void setKodKonGrp(Integer value)                  { kodKonGrp                  = value;        }
    public void setAbbreviatura(String value)                { abbreviatura               = value.trim(); }
    public void setAction(String value)                      { action                     = value.trim(); }
    public void setNazvanie(String value)                    { nazvanie                   = value.trim(); }
    public void setNazvanieVRoditelnom(String value)         { nazvanieVRoditelnom        = value.trim(); }
    public void setPriznakSortirovki(String value)           { priznakSortirovki          = value.trim(); }

 public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        abbreviatura               =  null;
        action                     =  null;
        nazvanie                   =  null;
        kodKonGrp                  =  null;
        nazvanieVRoditelnom        =  null;
    }

    public void setBean(AbiturientBean bean,
                    HttpServletRequest request,
                    ActionErrors errors)
                    throws ServletException {
        if (bean!=null) {

            kodKonGrp = bean.getKodKonGrp();

            if (bean.getPriznakSortirovki()!=null) {
                priznakSortirovki = bean.getPriznakSortirovki();
            }
            if (bean.getNazvanie()!=null) {
                nazvanie = bean.getNazvanie();
            }
            if (bean.getAbbreviatura()!=null) {
                abbreviatura = bean.getAbbreviatura();
            }
            if (bean.getNazvanieVRoditelnom()!=null) {
                nazvanieVRoditelnom = bean.getNazvanieVRoditelnom();
            }
        }
    }
    public AbiturientBean getBean(HttpServletRequest request, ActionErrors errors)
                           throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setKodKonGrp(kodKonGrp);

        if (priznakSortirovki!=null && !priznakSortirovki.equals("")) {
            bean.setPriznakSortirovki(StringUtil.toDB(priznakSortirovki));
        }
        if (nazvanie!=null && !nazvanie.equals("")) {
            bean.setNazvanie(StringUtil.toDB(nazvanie));
        }
        if (abbreviatura!=null && !abbreviatura.equals("")) {
            bean.setAbbreviatura(StringUtil.toDB(abbreviatura));
        }
        if (nazvanieVRoditelnom!=null && !nazvanieVRoditelnom.equals("")) {
            bean.setNazvanieVRoditelnom(StringUtil.toDB(nazvanieVRoditelnom));
        }
        return bean;
    }      
}