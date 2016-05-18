package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class BallPotokForm extends ActionForm {

   
    private Integer kodFakulteta           = null;
    private Integer kodVuza                = null;
    private Integer nomerPotoka            = null;
    private String  nazvanie               = null;
    private String  abbreviaturaFakulteta  = null;
    private String  action                 = null;
    private Integer otsenka		   = null;
    private String  special1               = null;
    private String  special2               = null;
    private String  special3               = null;
    private String  special4               = null;
    private String  special5               = null;
    private String  special6               = null;
    private String  special7               = null;
    private String  special8               = null;
    private Integer special11              = null;
    private Integer special22              = null;
    private Integer special23              = null;
    private Integer special24              = null;
    private Integer special25              = null;
    private Integer special26              = null;

    public Integer getKodVuza()               { return kodVuza;               }
    public Integer getKodFakulteta()          { return kodFakulteta;          }
    public Integer getNomerPotoka()           { return nomerPotoka;           }
    public String  getNazvanie()              { return nazvanie;              }
    public String  getAbbreviaturaFakulteta() { return abbreviaturaFakulteta; }
    public Integer getOtsenka()               { return otsenka;               }
    public String  getAction()                { return action;                }
    public String  getSpecial1()              { return special1;              }
    public String  getSpecial2()              { return special2;              }
    public String  getSpecial3()              { return special3;              }
    public String  getSpecial4()              { return special4;              }
    public String  getSpecial5()              { return special5;              }
    public String  getSpecial6()              { return special6;              }
    public String  getSpecial7()              { return special7;              }
    public String  getSpecial8()              { return special8;              }
    public Integer  getSpecial11()            { return special11;             }
    public Integer  getSpecial22()            { return special22;             }
    public Integer  getSpecial23()            { return special23;             }
    public Integer getSpecial24()             { return special24;             }
    public Integer getSpecial25()             { return special25;             }
    public Integer getSpecial26()             { return special26;             }

    public void setKodFakulteta(Integer value)         { kodFakulteta           = value;        }
    public void setKodVuza(Integer value)              { kodVuza                = value;        }
    public void setNomerPotoka(Integer value)          { nomerPotoka            = value;        }
    public void setOtsenka(Integer value)              { otsenka                = value;        }
    public void setNazvanie(String value)              { nazvanie               = value;        }
    public void setAbbreviaturaFakulteta(String value) { abbreviaturaFakulteta  = value;        }
    public void setAction(String value)                { action                 = value.trim(); }
    public void setSpecial1(String value)              { special1               = value.trim(); }
    public void setSpecial2(String value)              { special2               = value.trim(); }
    public void setSpecial3(String value)              { special3               = value.trim(); }
    public void setSpecial4(String value)              { special4               = value.trim(); }
    public void setSpecial5(String value)              { special5               = value.trim(); }
    public void setSpecial6(String value)              { special6               = value.trim(); }
    public void setSpecial7(String value)              { special7               = value.trim(); }
    public void setSpecial8(String value)              { special8               = value.trim(); }
    public void setSpecial11(Integer value)            { special11              = value;        }
    public void setSpecial22(Integer value)            { special22              = value;        }
    public void setSpecial23(Integer value)            { special23              = value;        }
    public void setSpecial24(Integer value)            { special24              = value;        }
    public void setSpecial25(Integer value)            { special25              = value;        }
    public void setSpecial26(Integer value)            { special26              = value;        }

    public void reset(ActionMapping mapping, HttpServletRequest request )
    {

        kodFakulteta          = null;
        kodVuza               = null;
        nomerPotoka           = null;
        nazvanie              = null;
        abbreviaturaFakulteta = null;
	otsenka               = null;
        action                = null;
	special1              = null;
	special2              = null;
	special3              = null;
	special4              = null;
	special5              = null;
	special6              = null;
	special7              = null;
	special8              = null;
	special11             = null;
	special22             = null;
	special23             = null;
	special24             = null;
	special25             = null;
	special26             = null;
	
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors) throws ServletException {

        if ( bean != null ) {

           kodFakulteta = bean.getKodFakulteta();
           kodVuza = bean.getKodVuza();
           nomerPotoka = bean.getNomerPotoka();
           nazvanie = bean.getNazvanie();
           abbreviaturaFakulteta = bean.getAbbreviaturaFakulteta();
           otsenka = bean.getOtsenka();
           special1 = bean.getSpecial1();
           special2 = bean.getSpecial2();
           special3 = bean.getSpecial3();
           special4 = bean.getSpecial4();
           special5 = bean.getSpecial5();
           special6 = bean.getSpecial6();
           special7 = bean.getSpecial7();
           special8 = bean.getSpecial8();
           special11 = bean.getSpecial11();
           special22 = bean.getSpecial22();
           special23 = bean.getSpecial23();
           special24 = bean.getSpecial24();
           special25 = bean.getSpecial25();
           special26 = bean.getSpecial26();
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      
        bean.setKodVuza(kodVuza);
        bean.setNomerPotoka(nomerPotoka);
        bean.setNazvanie(nazvanie);
        bean.setAbbreviaturaFakulteta(abbreviaturaFakulteta);
	bean.setOtsenka(otsenka);
        bean.setKodFakulteta(kodFakulteta);
        bean.setSpecial1(special1);
        bean.setSpecial2(special2);
        bean.setSpecial3(special3);
        bean.setSpecial4(special4);
        bean.setSpecial5(special5);
        bean.setSpecial6(special6);
        bean.setSpecial7(special7);
        bean.setSpecial8(special8);
        bean.setSpecial11(special11);
        bean.setSpecial22(special22);
        bean.setSpecial23(special23);
        bean.setSpecial24(special24);
        bean.setSpecial25(special25);
        bean.setSpecial26(special26);
        return bean;
    }      
}