package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class GenEgeForm extends ActionForm {

    private Integer kodSpetsialnosti       = null;
    private Integer kodPredmeta            = null;
    private Integer kodMedali              = null;
    private Integer kodVuza                = null;
    private Integer kodFakulteta           = null;
    private Integer kodAbiturienta         = null;
    private Integer otsenka                = null;
    private Integer special22              = null;
    private String  exzamOtsenka           = null;
    private String  shifrMedali            = null;
    private String  special4               = null;
    private String  nomerPlatnogoDogovora  = null;
    private String  special5               = null;
    private String  fakultet               = null;
    private String  sokr                   = null;
    private String  special2               = null;
    private String  abbreviatura           = null;
    private String  special3               = null;
    private String  special1               = null;
    private String  special6               = null;
    private String  special7               = null;
    private String  special8               = null;
    private String  action                 = null;

    public Integer getKodSpetsialnosti()      { return kodSpetsialnosti;      }
    public Integer getKodMedali()             { return kodMedali;             }
    public Integer getKodVuza()               { return kodVuza;               }
    public Integer getKodAbiturienta()        { return kodAbiturienta;        }
    public Integer getOtsenka()               { return otsenka;               }
    public Integer getSpecial22()             { return special22;             }
    public Integer getKodFakulteta()          { return kodFakulteta;          }
    public Integer getKodPredmeta()           { return kodPredmeta;           }
    public String  getExzamOtsenka()          { return exzamOtsenka;          }
    public String  getShifrMedali()           { return shifrMedali;           }
    public String  getSpecial4()              { return special4;              }
    public String  getNomerPlatnogoDogovora() { return nomerPlatnogoDogovora; }
    public String  getSpecial5()              { return special5;              }
    public String  getFakultet()              { return fakultet;              }
    public String  getAbbreviatura()          { return abbreviatura;          }
    public String  getSokr()                  { return sokr;                  }
    public String  getSpecial2()              { return special2;              }
    public String  getSpecial3()              { return special3;              }
    public String  getSpecial1()              { return special1;              }
    public String  getSpecial6()              { return special6;              }
    public String  getSpecial7()              { return special7;              }
    public String  getSpecial8()              { return special8;              }
    public String  getAction()                { return action;                }

    public void setKodSpetsialnosti(Integer value)     { kodSpetsialnosti       = value;        }
    public void setKodPredmeta(Integer value)          { kodPredmeta            = value;        }
    public void setKodMedali(Integer value)            { kodMedali              = value;        }
    public void setKodVuza(Integer value)              { kodVuza                = value;        }
    public void setKodAbiturienta(Integer value)       { kodAbiturienta         = value;        }
    public void setOtsenka(Integer value)              { otsenka                = value;        }
    public void setSpecial22(Integer value)            { special22              = value;        }
    public void setKodFakulteta(Integer value)         { kodFakulteta           = value;        }
    public void setExzamOtsenka(String value)          { exzamOtsenka           = value.trim(); }
    public void setShifrMedali(String value)           { shifrMedali            = value.trim(); }
    public void setSpecial4(String value)              { special4               = value.trim(); }
    public void setNomerPlatnogoDogovora(String value) { nomerPlatnogoDogovora  = value.trim(); }
    public void setSpecial5(String value)              { special5               = value.trim(); }
    public void setFakultet(String value)              { fakultet               = value.trim(); }
    public void setAbbreviatura(String value)          { abbreviatura           = value.trim(); }
    public void setSokr(String value)                  { sokr                   = value.trim(); }
    public void setSpecial2(String value)              { special2               = value.trim(); }
    public void setSpecial3(String value)              { special3               = value.trim(); }
    public void setSpecial1(String value)              { special1               = value.trim(); }
    public void setSpecial6(String value)              { special6               = value.trim(); }
    public void setSpecial7(String value)              { special7               = value.trim(); }
    public void setSpecial8(String value)              { special8               = value.trim(); }
    public void setAction(String value)                { action                 = value.trim(); }

    public void reset(ActionMapping mapping, HttpServletRequest request )
    {
        kodSpetsialnosti       = null;
        kodPredmeta            = null;
        kodMedali              = null;
        kodVuza                = null;
        kodAbiturienta         = null;
        otsenka                = null;
        special22              = null;
        kodFakulteta           = null;
        exzamOtsenka           = null;
        shifrMedali            = null;
        special4               = null;
        nomerPlatnogoDogovora  = null;
        special5               = null;
        fakultet               = null;
        abbreviatura           = null;
        sokr                   = null;
        special2               = null;
        special3               = null;
        special1               = null;
        special6               = null;
        special7               = null;
        special8               = null;
        action                 = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors) throws ServletException {

        if ( bean != null ) {

            kodSpetsialnosti = bean.getKodSpetsialnosti();

            kodPredmeta = bean.getKodPredmeta();

            kodMedali = bean.getKodMedali();

            kodVuza = bean.getKodVuza();

            kodAbiturienta = bean.getKodAbiturienta();

            otsenka = bean.getOtsenka();

            special22 = bean.getSpecial22();

            kodFakulteta = bean.getKodFakulteta();

            if ( bean.getExzamOtsenka() != null ) {
                exzamOtsenka = bean.getExzamOtsenka();
            }

            if ( bean.getShifrMedali() != null ) {
                shifrMedali = bean.getShifrMedali();
            }

            if ( bean.getSpecial4() != null ) {
                special4 = bean.getSpecial4();
            }

            if ( bean.getNomerPlatnogoDogovora() != null ) {
                nomerPlatnogoDogovora = bean.getNomerPlatnogoDogovora();
            }

            if ( bean.getSpecial5() != null ) {
                special5 = bean.getSpecial5();
            }

            if ( bean.getFakultet() != null ) {
                fakultet = bean.getFakultet();
            }

            if ( bean.getAbbreviatura() != null ) {
                abbreviatura = bean.getAbbreviatura();
            }

            if ( bean.getSokr() != null ) {
                sokr = bean.getSokr();
            }

            if ( bean.getSpecial2() != null ) {
                special2 = bean.getSpecial2();
            }

            if ( bean.getSpecial3() != null ) {
                special3 = bean.getSpecial3();
            }

            if ( bean.getSpecial1() != null ) {
                special1 = bean.getSpecial1();
            }

            if ( bean.getSpecial6() != null ) {
                special6 = bean.getSpecial6();
            }

            if ( bean.getSpecial7() != null ) {
                special7 = bean.getSpecial7();
            }

            if ( bean.getSpecial8() != null ) {
                special8 = bean.getSpecial8();
            }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setKodSpetsialnosti(kodSpetsialnosti);

        bean.setKodMedali(kodMedali);

        bean.setKodVuza(kodVuza);

        bean.setKodAbiturienta(kodAbiturienta);

        bean.setOtsenka(otsenka);

        bean.setSpecial22(special22);

        bean.setKodFakulteta(kodFakulteta);

        bean.setKodPredmeta(kodPredmeta);

        if ( exzamOtsenka!=null && !exzamOtsenka.equals("") ) {
            bean.setExzamOtsenka(StringUtil.toDB(exzamOtsenka));
        }

        if ( shifrMedali!=null && !shifrMedali.equals("") ) {
            bean.setShifrMedali(StringUtil.toDB(shifrMedali));
        }

        if ( special4!=null && !special4.equals("") ) {
            bean.setSpecial4(StringUtil.toDB(special4));
        }

        if ( nomerPlatnogoDogovora!=null && !nomerPlatnogoDogovora.equals("") ) {
            bean.setNomerPlatnogoDogovora(StringUtil.toDB(nomerPlatnogoDogovora));
        }

        if ( special5!=null && !special5.equals("") ) {
            bean.setSpecial5(StringUtil.toDB(special5));
        }

        if ( fakultet!=null && !fakultet.equals("") ) {
            bean.setFakultet(StringUtil.toDB(fakultet));
        }

        if ( abbreviatura!=null && !abbreviatura.equals("") ) {
            bean.setAbbreviatura(StringUtil.toDB(abbreviatura));
        }

        if ( sokr!=null && !sokr.equals("") ) {
            bean.setSokr(StringUtil.toDB(sokr));
        }

        if ( special2!=null && !special2.equals("") ) {
            bean.setSpecial2(StringUtil.toDB(special2));
        }

        if ( special3!=null && !special3.equals("") ) {
            bean.setSpecial3(StringUtil.toDB(special3));
        }

        if ( special1!=null && !special1.equals("") ) {
            bean.setSpecial1(StringUtil.toDB(special1));
        }

        if ( special6!=null && !special6.equals("") ) {
            bean.setSpecial6(StringUtil.toDB(special6));
        }

        if ( special7!=null && !special7.equals("") ) {
            bean.setSpecial7(StringUtil.toDB(special7));
        }

        if ( special8!=null && !special8.equals("") ) {
            bean.setSpecial8(StringUtil.toDB(special8));
        }
        return bean;
    }      
}