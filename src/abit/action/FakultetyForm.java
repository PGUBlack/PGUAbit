package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class FakultetyForm extends ActionForm {

    private Integer kodFakulteta               = null;
    private Integer planPriemaFakulteta        = null;
    private Integer poluProhodnoiBallFakulteta = null;
    private Integer prohodnoiBallFakulteta     = null;
    private Integer stolbetsSortirovki         = new Integer(1);
    private String  abbreviaturaFakulteta      = null;
    private String  action                     = null;
    private String  dekan                      = null;
    private String  fakultet                   = null;
    private String  nazvanieVRoditelnom        = null;
    private String  priznakSortirovki          = null;
    private String  shifrFakulteta             = null;


    public Integer getKodFakulteta()               { return kodFakulteta;               }
    public Integer getPlanPriemaFakulteta()        { return planPriemaFakulteta;        }
    public Integer getPoluProhodnoiBallFakulteta() { return poluProhodnoiBallFakulteta; }
    public Integer getProhodnoiBallFakulteta()     { return prohodnoiBallFakulteta;     }
    public Integer getStolbetsSortirovki()         { return stolbetsSortirovki;         }
    public String  getAbbreviaturaFakulteta()      { return abbreviaturaFakulteta;      }
    public String  getAction()                     { return action;                     }
    public String  getDekan()                      { return dekan;                      }
    public String  getFakultet()                   { return fakultet;                   }
    public String  getNazvanieVRoditelnom()        { return nazvanieVRoditelnom;        }
    public String  getPriznakSortirovki()          { return priznakSortirovki;          }
    public String  getShifrFakulteta()             { return shifrFakulteta;             }

    public void setKodFakulteta(Integer value)               { kodFakulteta               = value;        }
    public void setPlanPriemaFakulteta(Integer value)        { planPriemaFakulteta        = value;        }
    public void setPoluProhodnoiBallFakulteta(Integer value) { poluProhodnoiBallFakulteta = value;        }  
    public void setProhodnoiBallFakulteta(Integer value)     { prohodnoiBallFakulteta     = value;        }  
    public void setStolbetsSortirovki(Integer value)         { stolbetsSortirovki         = value;        }
    public void setAbbreviaturaFakulteta(String value)       { abbreviaturaFakulteta      = value.trim(); }
    public void setAction(String value)                      { action                     = value.trim(); }
    public void setDekan(String value)                       { dekan                      = value.trim(); }
    public void setFakultet(String value)                    { fakultet                   = value.trim(); }
    public void setNazvanieVRoditelnom(String value)         { nazvanieVRoditelnom        = value.trim(); }
    public void setPriznakSortirovki(String value)           { priznakSortirovki          = value.trim(); }
    public void setShifrFakulteta(String value)              { shifrFakulteta             = value.trim(); }

 public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        abbreviaturaFakulteta      =  null;
        action                     =  null;
        dekan                      =  null;
        fakultet                   =  null;
        kodFakulteta               =  null;
        nazvanieVRoditelnom        =  null;
        planPriemaFakulteta        =  null;
        poluProhodnoiBallFakulteta =  null;
        prohodnoiBallFakulteta     =  null;
        priznakSortirovki          =  null;
        shifrFakulteta             =  null;
        stolbetsSortirovki         =  new Integer(1);
    }

    public void setBean(AbiturientBean bean,
                    HttpServletRequest request,
                    ActionErrors errors)
                    throws ServletException {
        if (bean!=null) {

            kodFakulteta = bean.getKodFakulteta();

            planPriemaFakulteta = bean.getPlanPriemaFakulteta();

            poluProhodnoiBallFakulteta = bean.getPoluProhodnoiBallFakulteta();

            prohodnoiBallFakulteta = bean.getProhodnoiBallFakulteta();

            stolbetsSortirovki = bean.getStolbetsSortirovki();

            if (bean.getPriznakSortirovki()!=null) {
                priznakSortirovki = bean.getPriznakSortirovki();
            }
            if (bean.getFakultet()!=null) {
                fakultet = bean.getFakultet();
            }
            if (bean.getAbbreviaturaFakulteta()!=null) {
                abbreviaturaFakulteta = bean.getAbbreviaturaFakulteta();
            }
            if (bean.getShifrFakulteta()!=null) {
                shifrFakulteta = bean.getShifrFakulteta();
            }
            if (bean.getNazvanieVRoditelnom()!=null) {
                nazvanieVRoditelnom = bean.getNazvanieVRoditelnom();
            }
            if (bean.getDekan()!=null) {
                dekan = bean.getDekan();
            }
        }
    }
    public AbiturientBean getBean(HttpServletRequest request, ActionErrors errors)
                           throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setKodFakulteta(kodFakulteta);

        bean.setPlanPriemaFakulteta(planPriemaFakulteta);

        bean.setPoluProhodnoiBallFakulteta(poluProhodnoiBallFakulteta);

        bean.setProhodnoiBallFakulteta(prohodnoiBallFakulteta);

        bean.setStolbetsSortirovki(stolbetsSortirovki);

        if (priznakSortirovki!=null && !priznakSortirovki.equals("")) {
            bean.setPriznakSortirovki(StringUtil.toDB(priznakSortirovki));
        }
        if (fakultet!=null && !fakultet.equals("")) {
            bean.setFakultet(StringUtil.toDB(fakultet));
        }
        if (abbreviaturaFakulteta!=null && !abbreviaturaFakulteta.equals("")) {
            bean.setAbbreviaturaFakulteta(StringUtil.toDB(abbreviaturaFakulteta));
        }
        if (shifrFakulteta!=null && !shifrFakulteta.equals("")) {
            bean.setShifrFakulteta(StringUtil.toDB(shifrFakulteta));
        }
        if (nazvanieVRoditelnom!=null && !nazvanieVRoditelnom.equals("")) {
            bean.setNazvanieVRoditelnom(StringUtil.toDB(nazvanieVRoditelnom));
        }
        if (dekan!=null && !dekan.equals("")) {
            bean.setDekan(StringUtil.toDB(dekan));
        }
        return bean;
    }      
}