package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import java.util.ArrayList;
import abit.util.StringUtil;

public class CheckEGEForm  extends ActionForm {

	    private Integer ball                         = null;
	    private Integer ballEge                      = null;
	    private Integer godOkonchanijaSrObrazovanija = null;
	    private String  dataRojdenija                = null;
	    private String  mestoRojdenija               = null;
	    private String  udostoverenieLgoty           = null;
	    private String  diplomOtlichija              = null;
	    private String  stepen_Mag                   = null;
	    private String  exists_st_Mag                = null;
	    private String  shifr_DipBak                 = null;
	    private String  shifr_DipSpec                = null;
	    private String  nazv_DipBak                  = null;
	    private String  nazv_DipSpec                 = null;
	    private String  s_okso_1                     = null;
	    private String  s_okso_2                     = null;
	    private String  s_okso_3                     = null;
	    private String  s_okso_4                     = null;
	    private String  s_okso_5                     = null;
	    private String  s_okso_6                     = null;
	    private String  bud_1                        = null;
	    private String  bud_2                        = null;
	    private String  bud_3                        = null;
	    private String  bud_4                        = null;
	    private String  bud_5                        = null;
	    private String  bud_6                        = null;
	    private String  dop_Info                     = null;
	    private String  dog_1                        = null;
	    private String  dog_2                        = null;
	    private String  dog_3                        = null;
	    private String  dog_4                        = null;
	    private String  dog_5                        = null;
	    private String  dog_6                        = null;
	    private String  dog_ok_1                     = null;
	    private String  dog_ok_2                     = null;
	    private String  dog_ok_3                     = null;
	    private String  dog_ok_4                     = null;
	    private String  dog_ok_5                     = null;
	    private String  dog_ok_6                     = null;
	    private String  six_1                        = null;
	    private String  six_2                        = null;
	    private String  six_3                        = null;
	    private String  six_4                        = null;
	    private String  six_5                        = null;
	    private String  six_6                        = null;
	    private String  three_1                      = null;
	    private String  three_2                      = null;
	    private String  three_3                      = null;
	    private String  three_4                      = null;
	    private String  three_5                      = null;
	    private String  three_6                      = null;
	    private String  olimp_1                      = null;
	    private String  olimp_2                      = null;
	    private String  olimp_3                      = null;
	    private String  olimp_4                      = null;
	    private String  olimp_5                      = null;
	    private String  olimp_6                      = null;
	    private String  target_1                     = null;
	    private String  target_2                     = null;
	    private String  target_3                     = null;
	    private String  target_4                     = null;
	    private String  target_5                     = null;
	    private String  target_6                     = null;
	    private String  fito_1                       = null;
	    private String  fito_2                       = null;
	    private String  fito_3                       = null;
	    private String  fito_4                       = null;
	    private String  fito_5                       = null;
	    private String  fito_6                       = null;
	    private String  gruppa                       = null;
	    private String  fileName1                    = null;
	    private String  fileName2                    = null;
	    private String  forma_Ob1                    = null;
	    private String  forma_Ob2                    = null;
	    private String  forma_Ob3                    = null;
	    private String  kopijaSertifikata            = null;
	    private Integer kodAbiturienta               = null;
	    private Integer kodSpetsialnZach             = null;
	    private Integer kodTselevogoPriema           = null;
	    private Integer kodGruppy                    = null;
	    private Integer kodLgot                      = null;
	    private Integer kodKursov                    = null;
	    private Integer kodMedali                    = null;
	    private Integer kodPredmeta                  = null;
	    private Integer kodPunkta                    = null;
	    private Integer kodVuza                      = null;
	    private Integer kodZapisi                    = null;
	    private Integer kodOblasti                   = null;
	    private Integer kodRajona                    = null;
	    private Integer kodZavedenija                = null;
	    private Integer kodFormyOb                   = null;
	    private Integer kodOsnovyOb                  = null;
	    private String  lgoty                        = null;
	    private String  may_del                      = null;
	    private String  medal                        = null;
	    private String  formaOb                      = null;
	    private String  osnovaOb                     = null;
	    private String  shifrMedali                  = null;
	    private String  shifrLgot                    = null;
	    private String  priznakSortirovki            = null;
	    private String  dataApelljatsii              = null;
	    private String  dataJekzamena                = null;
	    private Integer special22                    = null;
	    private Integer otsenkaegeabiturienta        = null;
	    private String  otsenka_Att_ot               = null;
	    private String  otsenka_Att_do               = null;
	    private String  otsenka_Att                  = null;
	    private String  otsenka_Zaj_ot               = null;
	    private String  otsenka_Zaj_do               = null;
	    private String  otsenka_Zaj                  = null;
	    private String  otsenka_Ege_ot               = null;
	    private String  otsenka_Ege_do               = null;
	    private String  otsenka_Ege                  = null;
	    private String  otsenka_Exam_ot              = null;
	    private String  otsenka_Exam_do              = null;
	    private String  otsenka_Exam                 = null;
	    private Integer otsenkiabiturienta           = null;
	    private Integer atestatabiturienta           = null;
	    private Integer kodFakulteta                 = null;
	    private String  dokumentyHranjatsja          = null;
	    private String  shifrKursov                  = null;
	    private String  gdePoluchilSrObrazovanie     = null;
	    private String  inostrannyjJazyk             = null;
	    private String  napravlenieOtPredprijatija   = null;
	    private String  srokObuchenija               = null;
	    private String  nujdaetsjaVObschejitii       = null;
	    private String  medSpravka       = null;
	    private String  pol                          = null;
	    private String  prinjat                      = null;
	    private String  sobesedovanie                = null;
	    private String  shifrFakulteta               = null;
	    private String  sokr                         = null;
	    private String  attestat                     = null;
	    private String  apelljatsija                 = null;
	    private String  ege                          = null;
	    private String  gorod_Prop                   = null;
	    private String  ulica_Prop                   = null;
	    private String  useAllSpecs                  = null;
	    private String  dom_Prop                     = null;
	    private String  kvart_Prop                   = null;
	    private String  examen                       = null;
	    private String  tel                          = null;
	    private String  zajavlen                     = null;
	    private String  polnoeNaimenovanieZavedenija = null;
	    private String  special1                     = null;
	    private String  special2                     = null;
	    private String  special3                     = null;
	    private String  special4                     = null;
	    private String  special5                     = null;
	    private String  special6                     = null;
	    private String  special7                     = null;
	    private String  special8                     = null;
	    private String  special9                     = null;
	    private String  special10                    = null;
	    private String  special13                    = null;
	    private String  tipOkonchennogoZavedenija    = null;
	    private String  tipDokumenta                 = null;
	    private String  tip_Spec                     = null;
	    private String  seriaDokumenta               = null;
	    private String  dataVydDokumenta             = null;
	    private String  kemVydDokument               = null;
	    private String  tipDokSredObraz              = null;
	    private String  vidDokSredObraz              = null;
	    private String  nomerSertifikata             = null;
	    private String  trudovajaDejatelnost         = null;
	    private String  tselevojPriem                = null;
	    private String  familija                     = null;
	    private String  dogovornaja                  = null;
	    private String  fio                          = null;
	    private String  grajdanstvo                  = null;
	    private String  gruppaOplativshego           = null;
	    private String  imja                         = null;
	    private String  need_Spo                     = null;
	    private String  nomerAtt                     = null;
	    private String  seriaAtt                     = null;
	    private String  nazvanie                     = null;
	    private String  nazvanieRajona               = null;
	    private String  nazvanieOblasti              = null;
	    private String  nomerDokumenta               = null;
	    private String  nomerLichnogoDela            = null;
	    private Integer nomerPotoka                  = null;
	    private String  nomerPlatnogoDogovora        = null;
	    private String  nomerShkoly                  = null;
	    private ArrayList notes                      = null;
	    private String  otchestvo                    = null;
	    private String  predmetspecial               = null;
	    private int     maxCountAbiturients          = 0;
	    private String  action                       = null;
	    private String podtverjdenieMedSpravki       = null;
	    private String abitEmail				         = null;
	    private String dopAddress       = null;
	    private String providingSpecialCondition      = null;
	    private String returnDocument       = null;
	    private String preemptiveRight       = null;
	    private String postgraduateStudies     = null;
	    private String traineeship       = null;
	    private String internship       = null;
	    

	    public Integer getBall()                         { return ball;                         }
	    public Integer getBallEge()                      { return ballEge;                      }
	    public Integer getGodOkonchanijaSrObrazovanija() { return godOkonchanijaSrObrazovanija; }
	    public String  getStepen_Mag()                   { return stepen_Mag;                   }
	    public String  getExists_st_Mag()                { return exists_st_Mag;                }
	    public String  getShifr_DipBak()                 { return shifr_DipBak;                 }
	    public String  getShifr_DipSpec()                { return shifr_DipSpec;                }
	    public String  getNazv_DipBak()                  { return nazv_DipBak;                  }
	    public String  getNazv_DipSpec()                 { return nazv_DipSpec;                 }
	    public String  getS_oksko_1()                    { return s_okso_1;                     }
	    public String  getS_oksko_2()                    { return s_okso_2;                     }
	    public String  getS_oksko_3()                    { return s_okso_3;                     }
	    public String  getS_oksko_4()                    { return s_okso_4;                     }
	    public String  getS_oksko_5()                    { return s_okso_5;                     }
	    public String  getS_oksko_6()                    { return s_okso_6;                     }
	    public String  getSix_1()                        { return six_1;                        }
	    public String  getSix_2()                        { return six_2;                        }
	    public String  getSix_3()                        { return six_3;                        }
	    public String  getSix_4()                        { return six_4;                        }
	    public String  getSix_5()                        { return six_5;                        }
	    public String  getSix_6()                        { return six_6;                        }
	    public String  getFito_1()                       { return fito_1;                       }
	    public String  getFito_2()                       { return fito_2;                       }
	    public String  getFito_3()                       { return fito_3;                       }
	    public String  getFito_4()                       { return fito_4;                       }
	    public String  getFito_5()                       { return fito_5;                       }
	    public String  getFito_6()                       { return fito_6;                       }
	    public String  getThree_1()                      { return three_1;                      }
	    public String  getThree_2()                      { return three_2;                      }
	    public String  getThree_3()                      { return three_3;                      }
	    public String  getThree_4()                      { return three_4;                      }
	    public String  getThree_5()                      { return three_5;                      }
	    public String  getThree_6()                      { return three_6;                      }
	    public String  getOlimp_1()                      { return olimp_1;                      }
	    public String  getOlimp_2()                      { return olimp_2;                      }
	    public String  getOlimp_3()                      { return olimp_3;                      }
	    public String  getOlimp_4()                      { return olimp_4;                      }
	    public String  getOlimp_5()                      { return olimp_5;                      }
	    public String  getOlimp_6()                      { return olimp_6;                      }
	    public String  getTarget_1()                     { return target_1;                     }
	    public String  getTarget_2()                     { return target_2;                     }
	    public String  getTarget_3()                     { return target_3;                     }
	    public String  getTarget_4()                     { return target_4;                     }
	    public String  getTarget_5()                     { return target_5;                     }
	    public String  getTarget_6()                     { return target_6;                     }
	    public String  getBud_1()                        { return bud_1;                        }
	    public String  getBud_2()                        { return bud_2;                        }
	    public String  getBud_3()                        { return bud_3;                        }
	    public String  getBud_4()                        { return bud_4;                        }
	    public String  getBud_5()                        { return bud_5;                        }
	    public String  getBud_6()                        { return bud_6;                        }
	    public String  getDop_Info()                     { return dop_Info;                     }
	    public String  getDog_1()                        { return dog_1;                        }
	    public String  getDog_2()                        { return dog_2;                        }
	    public String  getDog_3()                        { return dog_3;                        }
	    public String  getDog_4()                        { return dog_4;                        }
	    public String  getDog_5()                        { return dog_5;                        }
	    public String  getDog_6()                        { return dog_6;                        }
	    public String  getDog_ok_1()                     { return dog_ok_1;                     }
	    public String  getDog_ok_2()                     { return dog_ok_2;                     }
	    public String  getDog_ok_3()                     { return dog_ok_3;                     }
	    public String  getDog_ok_4()                     { return dog_ok_4;                     }
	    public String  getDog_ok_5()                     { return dog_ok_5;                     }
	    public String  getDog_ok_6()                     { return dog_ok_6;                     }
	    public Integer getOtsenkaegeabiturienta()        { return otsenkaegeabiturienta;        }
	    public String  getOtsenka_Att_ot()               { return otsenka_Att_ot;               }
	    public String  getOtsenka_Att_do()               { return otsenka_Att_do;               }
	    public String  getOtsenka_Att()                  { return otsenka_Att;                  }
	    public String  getOtsenka_Zaj_ot()               { return otsenka_Zaj_ot;               }
	    public String  getOtsenka_Zaj_do()               { return otsenka_Zaj_do;               }
	    public String  getOtsenka_Zaj()                  { return otsenka_Zaj;                  }
	    public String  getOtsenka_Ege_ot()               { return otsenka_Ege_ot;               }
	    public String  getOtsenka_Ege_do()               { return otsenka_Ege_do;               }
	    public String  getOtsenka_Ege()                  { return otsenka_Ege;                  }
	    public String  getOtsenka_Exam_ot()              { return otsenka_Exam_ot;              }
	    public String  getOtsenka_Exam_do()              { return otsenka_Exam_do;              }
	    public String  getOtsenka_Exam()                 { return otsenka_Exam;                 }
	    public Integer getOtsenkiabiturienta()           { return otsenkiabiturienta;           }
	    public Integer getAtestatabiturienta()           { return atestatabiturienta;           }
	    public String  getDataJekzamena()                { return dataJekzamena;                }
	    public String  getDataRojdenija()                { return dataRojdenija;                }
	    public String  getMestoRojdenija()               { return mestoRojdenija;               }
	    public String  getUdostoverenieLgoty()           { return udostoverenieLgoty;           }
	    public String  getDiplomOtlichija()              { return diplomOtlichija;              }
	    public String  getDataApelljatsii()              { return dataApelljatsii;              }
	    public Integer getKodVuza()                      { return kodVuza;                      }
	    public Integer getKodZapisi()                    { return kodZapisi;                    }
	    public Integer getKodZavedenija()                { return kodZavedenija;                }
	    public Integer getSpecial22()                    { return special22;                    }
	    public Integer getKodFakulteta()                 { return kodFakulteta;                 }
	    public Integer getKodKursov()                    { return kodKursov;                    }
	    public Integer getKodLgot()                      { return kodLgot;                      }
	    public String  getKopijaSertifikata()            { return kopijaSertifikata;            }
	    public Integer getKodAbiturienta()               { return kodAbiturienta;               }
	    public Integer getKodSpetsialnZach()             { return kodSpetsialnZach;             }
	    public Integer getKodTselevogoPriema()           { return kodTselevogoPriema;           }
	    public Integer getKodGruppy()                    { return kodGruppy;                    }
	    public String  getLgoty()                        { return lgoty;                        }
	    public String  getMay_del()                      { return may_del;                      }
	    public String  getMedal()                        { return medal;                        }
	    public String  getFormaOb()                      { return formaOb;                      }
	    public String  getOsnovaOb()                     { return osnovaOb;                     }
	    public String  getShifrMedali()                  { return shifrMedali;                  }
	    public String  getShifrLgot()                    { return shifrLgot;                    }
	    public String  getPriznakSortirovki()            { return priznakSortirovki;            }
	    public String  getPrinjat()                      { return prinjat;                      }
	    public String  getSobesedovanie()                { return sobesedovanie;                }
	    public String  getShifrFakulteta()               { return shifrFakulteta;               }
	    public String  getSpecial1()                     { return special1;                     }
	    public String  getSokr()                         { return sokr;                         }
	    public String  getAttestat()                     { return attestat;                     }
	    public String  getApelljatsija()                 { return apelljatsija;                 }
	    public String  getEge()                          { return ege;                          }
	    public String  getExamen()                       { return examen;                       }
	    public Integer getKodMedali()                    { return kodMedali;                    }
	    public Integer getKodPredmeta()                  { return kodPredmeta;                  }
	    public Integer getKodPunkta()                    { return kodPunkta;                    }
	    public Integer getKodRajona()                    { return kodRajona;                    }
	    public Integer getKodOblasti()                   { return kodOblasti;                   }
	    public Integer getKodFormyOb()                   { return kodFormyOb;                   }
	    public Integer getKodOsnovyOb()                  { return kodOsnovyOb;                  }
	    public String  getShifrKursov()                  { return shifrKursov;                  }
	    public String  getDokumentyHranjatsja()          { return dokumentyHranjatsja;          }
	    public String  getGdePoluchilSrObrazovanie()     { return gdePoluchilSrObrazovanie;     }
	    public String  getInostrannyjJazyk()             { return inostrannyjJazyk;             }
	    public String  getNapravlenieOtPredprijatija()   { return napravlenieOtPredprijatija;   }
	    public String  getSrokObuchenija()               { return srokObuchenija;               }
	    public String  getNujdaetsjaVObschejitii()       { return nujdaetsjaVObschejitii;       }
	    public String  getPol()                          { return pol;                          }
	    public String  getGorod_Prop()                   { return gorod_Prop;                   }
	    public String  getUlica_Prop()                   { return ulica_Prop;                   }
	    public String  getUseAllSpecs()                  { return useAllSpecs;                  }
	    public String  getdom_Prop()                     { return dom_Prop;                     }
	    public String  getKvart_Prop()                   { return kvart_Prop;                   }
	    public String  getTel()                          { return tel;                          }
	    public String  getZajavlen()                     { return zajavlen;                     }
	    public String  getPolnoeNaimenovanieZavedenija() { return polnoeNaimenovanieZavedenija; }
	    public String  getSpecial2()                     { return special2;                     }
	    public String  getSpecial3()                     { return special3;                     }
	    public String  getMedSpravka()                   { return medSpravka;                     }
	    public String  getSpecial4()                     { return special4;                     }
	    public String  getSpecial5()                     { return special5;                     }
	    public String  getSpecial6()                     { return special6;                     }
	    public String  getSpecial7()                     { return special7;                     }
	    public String  getSpecial8()                     { return special8;                     }
	    public String  getSpecial9()                     { return special9;                     }
	    public String  getSpecial10()                    { return special10;                    }
	    public String  getSpecial13()                    { return special13;                    }
	    public String  getDogovornaja()                  { return dogovornaja;                  }
	    public String  getFio()                          { return fio;                          }
	    public Integer getNomerPotoka()                  { return nomerPotoka;                  }
	    public ArrayList  getNotes()                     { return notes;                        }
	    public String  getNomerSertifikata()             { return nomerSertifikata;             }
	    public String  getPredmetspecial()               { return predmetspecial;               }
	    public int     getMaxCountAbiturients()          { return maxCountAbiturients;          }
	    public String  getTipOkonchennogoZavedenija()    { return tipOkonchennogoZavedenija;    }
	    public String  getTipDokumenta()                 { return tipDokumenta;                 }
	    public String  getTip_Spec()                     { return tip_Spec;                     }
	    public String  getTrudovajaDejatelnost()         { return trudovajaDejatelnost;         }
	    public String  getTselevojPriem()                { return tselevojPriem;                }
	    public String  getFamilija()                     { return familija;                     }
	    public String  getGrajdanstvo()                  { return grajdanstvo;                  }
	    public String  getGruppa()                       { return gruppa;                       }
	    public String  getFileName1()                    { return fileName1;                    }
	    public String  getFileName2()                    { return fileName2;                    }
	    public String  getForma_Ob1()                    { return forma_Ob1;                    }
	    public String  getForma_Ob2()                    { return forma_Ob2;                    }
	    public String  getForma_Ob3()                    { return forma_Ob3;                    }
	    public String  getGruppaOplativshego()           { return gruppaOplativshego;           }
	    public String  getImja()                         { return imja;                         }
	    public String  getNomerDokumenta()               { return nomerDokumenta;               }
	    public String  getSeriaDokumenta()               { return seriaDokumenta;               }
	    public String  getDataVydDokumenta()             { return dataVydDokumenta;             }
	    public String  getKemVydDokument()               { return kemVydDokument;               }
	    public String  getTipDokSredObraz()              { return tipDokSredObraz;              }
	    public String  getVidDokSredObraz()              { return vidDokSredObraz;              }
	    public String  getNomerLichnogoDela()            { return nomerLichnogoDela;            }
	    public String  getNomerPlatnogoDogovora()        { return nomerPlatnogoDogovora;        }
	    public String  getNomerShkoly()                  { return nomerShkoly;                  }
	    public String  getNazvanie()                     { return nazvanie;                     }
	    public String  getNazvanieOblasti()              { return nazvanieOblasti;              }
	    public String  getNazvanieRajona()               { return nazvanieRajona;               }
	    public String  getOtchestvo()                    { return otchestvo;                    }
	    public String  getSeriaAtt()                     { return seriaAtt;                     }
	    public String  getNeed_Spo()                     { return need_Spo;                     }
	    public String  getNomerAtt()                     { return nomerAtt;                     }
	    public String  getAction()                       { return action;                       }
	    public String  getPodtverjdenieMedSpravki()      { return podtverjdenieMedSpravki;      }
	    public String  getAbitEmail()    					  { return abitEmail;      }
	    public String  getDopAddress()      { return dopAddress;      }
	    public String  getProvidingSpecialCondition()      { return providingSpecialCondition;      }
	    public String  getReturnDocument()      { return returnDocument;      }
	    public String  getPreemptiveRight()      { return preemptiveRight;      }
	    public String  getPostgraduateStudies()      { return postgraduateStudies;      }
	    public String  getTraineeship()      { return traineeship;      }
	    public String  getInternship()      { return internship;      }

	    public void setBall(Integer value)                         { ball                         = value;        }
	    public void setBallEge(Integer value)                      { ballEge                      = value;        }
	    public void setGodOkonchanijaSrObrazovanija(Integer value) { godOkonchanijaSrObrazovanija = value;        }
	    public void setKodSpetsialnZach(Integer value)             { kodSpetsialnZach             = value;        }
	    public void setKodZapisi(Integer value)                    { kodZapisi                    = value;        }
	    public void setStepen_Mag(String value)                    { stepen_Mag                   = value.trim(); }
	    public void setExists_st_Mag(String value)                 { exists_st_Mag                = value.trim(); }
	    public void setShifr_DipBak(String value)                  { shifr_DipBak                 = value.trim(); }
	    public void setShifr_DipSpec(String value)                 { shifr_DipSpec                = value.trim(); }
	    public void setNazv_DipBak(String value)                   { nazv_DipBak                  = value.trim(); }
	    public void setNazv_DipSpec(String value)                  { nazv_DipSpec                 = value.trim(); }
	    public void setS_okso_1(String value)                      { s_okso_1                     = value.trim(); }
	    public void setS_okso_2(String value)                      { s_okso_2                     = value.trim(); }
	    public void setS_okso_3(String value)                      { s_okso_3                     = value.trim(); }
	    public void setS_okso_4(String value)                      { s_okso_4                     = value.trim(); }
	    public void setS_okso_5(String value)                      { s_okso_5                     = value.trim(); }
	    public void setS_okso_6(String value)                      { s_okso_6                     = value.trim(); }
	    public void setSix_1(String value)                         { six_1                        = value.trim(); }
	    public void setSix_2(String value)                         { six_2                        = value.trim(); }
	    public void setSix_3(String value)                         { six_3                        = value.trim(); }
	    public void setSix_4(String value)                         { six_4                        = value.trim(); }
	    public void setSix_5(String value)                         { six_5                        = value.trim(); }
	    public void setSix_6(String value)                         { six_6                        = value.trim(); }
	    public void setFito_1(String value)                        { fito_1                       = value.trim(); }
	    public void setFito_2(String value)                        { fito_2                       = value.trim(); }
	    public void setFito_3(String value)                        { fito_3                       = value.trim(); }
	    public void setFito_4(String value)                        { fito_4                       = value.trim(); }
	    public void setFito_5(String value)                        { fito_5                       = value.trim(); }
	    public void setFito_6(String value)                        { fito_6                       = value.trim(); }
	    public void setThree_1(String value)                       { three_1                      = value.trim(); }
	    public void setThree_2(String value)                       { three_2                      = value.trim(); }
	    public void setThree_3(String value)                       { three_3                      = value.trim(); }
	    public void setThree_4(String value)                       { three_4                      = value.trim(); }
	    public void setThree_5(String value)                       { three_5                      = value.trim(); }
	    public void setThree_6(String value)                       { three_6                      = value.trim(); }
	    public void setOlimp_1(String value)                       { olimp_1                      = value.trim(); }
	    public void setOlimp_2(String value)                       { olimp_2                      = value.trim(); }
	    public void setOlimp_3(String value)                       { olimp_3                      = value.trim(); }
	    public void setOlimp_4(String value)                       { olimp_4                      = value.trim(); }
	    public void setOlimp_5(String value)                       { olimp_5                      = value.trim(); }
	    public void setOlimp_6(String value)                       { olimp_6                      = value.trim(); }
	    public void setTarget_1(String value)                      { target_1                     = value.trim(); }
	    public void setTarget_2(String value)                      { target_2                     = value.trim(); }
	    public void setTarget_3(String value)                      { target_3                     = value.trim(); }
	    public void setTarget_4(String value)                      { target_4                     = value.trim(); }
	    public void setTarget_5(String value)                      { target_5                     = value.trim(); }
	    public void setTarget_6(String value)                      { target_6                     = value.trim(); }
	    public void setBud_1(String value)                         { bud_1                        = value.trim(); }
	    public void setBud_2(String value)                         { bud_2                        = value.trim(); }
	    public void setBud_3(String value)                         { bud_3                        = value.trim(); }
	    public void setBud_4(String value)                         { bud_4                        = value.trim(); }
	    public void setBud_5(String value)                         { bud_5                        = value.trim(); }
	    public void setBud_6(String value)                         { bud_6                        = value.trim(); }
	    public void setDop_Info(String value)                      { dop_Info                     = value.trim(); }
	    public void setDog_1(String value)                         { dog_1                        = value.trim(); }
	    public void setDog_2(String value)                         { dog_2                        = value.trim(); }
	    public void setDog_3(String value)                         { dog_3                        = value.trim(); }
	    public void setDog_4(String value)                         { dog_4                        = value.trim(); }
	    public void setDog_5(String value)                         { dog_5                        = value.trim(); }
	    public void setDog_6(String value)                         { dog_6                        = value.trim(); }
	    public void setDog_ok_1(String value)                      { dog_ok_1                     = value.trim(); }
	    public void setDog_ok_2(String value)                      { dog_ok_2                     = value.trim(); }
	    public void setDog_ok_3(String value)                      { dog_ok_3                     = value.trim(); }
	    public void setDog_ok_4(String value)                      { dog_ok_4                     = value.trim(); }
	    public void setDog_ok_5(String value)                      { dog_ok_5                     = value.trim(); }
	    public void setDog_ok_6(String value)                      { dog_ok_6                     = value.trim(); }
	    public void setDataRojdenija(String value)                 { dataRojdenija                = value.trim(); }
	    public void setMestoRojdenija(String value)                { mestoRojdenija               = value.trim(); }
	    public void setUdostoverenieLgoty(String value)            { udostoverenieLgoty           = value.trim(); }
	    public void setDiplomOtlichija(String value)               { diplomOtlichija              = value.trim(); }
	    public void setKopijaSertifikata(String value)             { kopijaSertifikata            = value.trim(); }
	    public void setKodAbiturienta(Integer value)               { kodAbiturienta               = value;        }
	    public void setKodTselevogoPriema(Integer value)           { kodTselevogoPriema           = value;        }
	    public void setKodGruppy(Integer value)                    { kodGruppy                    = value;        }
	    public void setKodLgot(Integer value)                      { kodLgot                      = value;        }
	    public void setKodKursov(Integer value)                    { kodKursov                    = value;        }
	    public void setKodMedali(Integer value)                    { kodMedali                    = value;        }
	    public void setKodPredmeta(Integer value)                  { kodPredmeta                  = value;        }
	    public void setKodPunkta(Integer value)                    { kodPunkta                    = value;        }
	    public void setKodVuza(Integer value)                      { kodVuza                      = value;        }
	    public void setKodOblasti(Integer value)                   { kodOblasti                   = value;        }
	    public void setKodRajona(Integer value)                    { kodRajona                    = value;        }
	    public void setKodFormyOb(Integer value)                   { kodFormyOb                   = value;        }
	    public void setKodOsnovyOb(Integer value)                  { kodOsnovyOb                  = value;        }
	    public void setKodZavedenija(Integer value)                { kodZavedenija                = value;        }
	    public void setSpecial22(Integer value)                    { special22                    = value;        }
	    public void setOtsenkaegeabiturienta(Integer value)        { otsenkaegeabiturienta        = value;        }
	    public void setOtsenka_Att_ot(String  value)               { otsenka_Att_ot               = value.trim(); }
	    public void setOtsenka_Att_do(String  value)               { otsenka_Att_do               = value.trim(); }
	    public void setOtsenka_Att(String  value)                  { otsenka_Att                  = value.trim(); }
	    public void setOtsenka_Zaj_ot(String  value)               { otsenka_Zaj_ot               = value.trim(); }
	    public void setOtsenka_Zaj_do(String  value)               { otsenka_Zaj_do               = value.trim(); }
	    public void setOtsenka_Zaj(String  value)                  { otsenka_Zaj                  = value.trim(); }
	    public void setOtsenka_Ege_ot(String  value)               { otsenka_Ege_ot               = value.trim(); }
	    public void setOtsenka_Ege_do(String  value)               { otsenka_Ege_do               = value.trim(); }
	    public void setOtsenka_Ege(String  value)                  { otsenka_Ege                  = value.trim(); }
	    public void setOtsenka_Exam_ot(String  value)              { otsenka_Exam_ot              = value.trim(); }
	    public void setOtsenka_Exam_do(String  value)              { otsenka_Exam_do              = value.trim(); }
	    public void setOtsenka_Exam(String  value)                 { otsenka_Exam                 = value.trim(); }
	    public void setOtsenkiabiturienta(Integer value)           { otsenkiabiturienta           = value;        }
	    public void setKodFakulteta(Integer value)                 { kodFakulteta                 = value;        }
	    public void setAtestatabiturienta(Integer value)           { atestatabiturienta           = value;        }
	    public void setDataJekzamena(String value)                 { dataJekzamena                = value.trim(); }
	    public void setDataApelljatsii(String value)               { dataApelljatsii              = value.trim(); }
	    public void setShifrKursov(String value)                   { shifrKursov                  = value.trim(); }
	    public void setDokumentyHranjatsja(String value)           { dokumentyHranjatsja          = value.trim(); }
	    public void setGdePoluchilSrObrazovanie(String value)      { gdePoluchilSrObrazovanie     = value.trim(); }
	    public void setInostrannyjJazyk(String value)              { inostrannyjJazyk             = value.trim(); }
	    public void setNapravlenieOtPredprijatija(String value)    { napravlenieOtPredprijatija   = value.trim(); }
	    public void setSrokObuchenija(String value)                { srokObuchenija               = value.trim(); }
	    public void setNujdaetsjaVObschejitii(String value)        { nujdaetsjaVObschejitii       = value.trim(); }
	    public void setPol(String value)                           { pol                          = value.trim(); }
	    public void setMay_del(String value)                       { may_del                      = value.trim(); }
	    public void setMedal(String value)                         { medal                        = value.trim(); }
	    public void setFormaOb(String value)                       { formaOb                      = value.trim(); }
	    public void setOsnovaOb(String value)                      { osnovaOb                     = value.trim(); }
	    public void setShifrMedali(String value)                   { shifrMedali                  = value.trim(); }
	    public void setShifrLgot(String value)                     { shifrLgot                    = value.trim(); }
	    public void setLgoty(String value)                         { lgoty                        = value.trim(); }
	    public void setPriznakSortirovki(String value)             { priznakSortirovki            = value.trim(); }
	    public void setPrinjat(String value)                       { prinjat                      = value.trim(); }
	    public void setSobesedovanie(String value)                 { sobesedovanie                = value.trim(); }
	    public void setShifrFakulteta(String value)                { shifrFakulteta               = value.trim(); }
	    public void setSpecial1(String value)                      { special1                     = value.trim(); }
	    public void setSokr(String value)                          { sokr                         = value.trim(); }
	    public void setAttestat(String value)                      { attestat                     = value.trim(); }
	    public void setApelljatsija(String value)                  { apelljatsija                 = value.trim(); }
	    public void setEge(String value)                           { ege                          = value.trim(); }
	    public void setGorod_Prop(String value)                    { gorod_Prop                   = value.trim(); }
	    public void setUlica_Prop(String value)                    { ulica_Prop                   = value.trim(); }
	    public void setUseAllSpecs(String value)                   { useAllSpecs                  = value.trim(); }
	    public void setDom_Prop(String value)                      { dom_Prop                     = value.trim(); }
	    public void setKvart_Prop(String value)                    { kvart_Prop                   = value.trim(); }
	    public void setExamen(String value)                        { examen                       = value.trim(); }
	    public void setTel(String value)                           { tel                          = value.trim(); }
	    public void setZajavlen(String value)                      { zajavlen                     = value.trim(); }
	    public void setPolnoeNaimenovanieZavedenija(String value)  { polnoeNaimenovanieZavedenija = value.trim(); }
	    public void setSpecial2(String value)                      { special2                     = value.trim(); }
	    public void setSpecial3(String value)                      { special3                     = value.trim(); }
	    public void setSpecial4(String value)                      { special4                     = value.trim(); }
	    public void setSpecial5(String value)                      { special5                     = value.trim(); }
	    public void setSpecial6(String value)                      { special6                     = value.trim(); }
	    public void setSpecial7(String value)                      { special7                     = value.trim(); }
	    public void setSpecial8(String value)                      { special8                     = value.trim(); }
	    public void setSpecial9(String value)                      { special9                     = value.trim(); }
	    public void setSpecial10(String value)                     { special10                    = value.trim(); }
	    public void setSpecial13(String value)                     { special13                    = value.trim(); }
	    public void setTipOkonchennogoZavedenija(String value)     { tipOkonchennogoZavedenija    = value.trim(); }
	    public void setTipDokumenta(String value)                  { tipDokumenta                 = value.trim(); }
	    public void setTip_Spec(String value)                      { tip_Spec                     = value.trim(); }
	    public void setNomerSertifikata(String value)              { nomerSertifikata             = value.trim(); }
	    public void setTrudovajaDejatelnost(String value)          { trudovajaDejatelnost         = value.trim(); }
	    public void setTselevojPriem(String value)                 { tselevojPriem                = value.trim(); }
	    public void setFamilija(String value)                      { familija                     = value.trim(); }
	    public void setGrajdanstvo(String value)                   { grajdanstvo                  = value.trim(); }
	    public void setGruppa(String value)                        { gruppa                       = value.trim(); }
	    public void setFileName1(String value)                     { fileName1                    = value.trim(); }
	    public void setFileName2(String value)                     { fileName2                    = value.trim(); }
	    public void setForma_Ob1(String value)                     { forma_Ob1                    = value.trim(); }
	    public void setForma_Ob2(String value)                     { forma_Ob2                    = value.trim(); }
	    public void setForma_Ob3(String value)                     { forma_Ob3                    = value.trim(); }
	    public void setGruppaOplativshego(String value)            { gruppaOplativshego           = value.trim(); }
	    public void setImja(String value)                          { imja                         = value.trim(); }
	    public void setNomerDokumenta(String value)                { nomerDokumenta               = value.trim(); }
	    public void setSeriaDokumenta(String value)                { seriaDokumenta               = value.trim(); }
	    public void setDataVydDokumenta(String value)              { dataVydDokumenta             = value.trim(); }
	    public void setKemVydDokument(String value)                { kemVydDokument               = value.trim(); }
	    public void setTipDokSredObraz(String value)               { tipDokSredObraz              = value.trim(); }
	    public void setVidDokSredObraz(String value)               { vidDokSredObraz              = value.trim(); }
	    public void setNomerLichnogoDela(String value)             { nomerLichnogoDela            = value.trim(); }
	    public void setNomerPlatnogoDogovora(String value)         { nomerPlatnogoDogovora        = value.trim(); }
	    public void setNomerShkoly(String value)                   { nomerShkoly                  = value.trim(); }
	    public void setNazvanie(String value)                      { nazvanie                     = value.trim(); }
	    public void setNazvanieOblasti(String value)               { nazvanieOblasti              = value.trim(); }
	    public void setNazvanieRajona(String value)                { nazvanieRajona               = value.trim(); }
	    public void setOtchestvo(String value)                     { otchestvo                    = value.trim(); }
	    public void setSeriaAtt(String value)                      { seriaAtt                     = value.trim(); }
	    public void setNeed_Spo(String value)                      { need_Spo                     = value.trim(); }
	    public void setNomerAtt(String value)                      { nomerAtt                     = value.trim(); }
	    public void setAction(String value)                        { action                       = value.trim(); }
	    public void setDogovornaja(String value)                   { dogovornaja                  = value.trim(); }
	    public void setFio(String value)                           { fio                          = value.trim(); }
	    public void setNomerPotoka(Integer value)                  { nomerPotoka                  = value;        }
	    public void setNotes(ArrayList value)                      { notes                        = value;        }
	    public void setPredmetspecial(String value)                { predmetspecial               = value.trim(); }
	    public void setMaxCountAbiturients(int value)              { maxCountAbiturients          = value;        }
	    public void setMedSpravka(String value)              {medSpravka        = value.trim();        }
	    public void setPodtverjdenieMedSpravki(String value)              {podtverjdenieMedSpravki       = value.trim();        }
	    public void setAbitEmail(String value)              {abitEmail       = value.trim();        }
	    public void setDopAddress(String value)              {dopAddress       = value.trim();        }
	    public void setProvidingSpecialCondition (String value)              {providingSpecialCondition      = value.trim();        }
	    public void setReturnDocument(String value)              {returnDocument      = value.trim();        }
	    public void setPreemptiveRight(String value)              {preemptiveRight      = value.trim();        }
	    public void setPostgraduateStudies(String value)              {postgraduateStudies      = value.trim();        }
	    public void setTraineeship(String value)              {traineeship      = value.trim();        }
	    public void setInternship(String value)              {internship      = value.trim();        }



	    public void reset(ActionMapping mapping, HttpServletRequest request )
	    {
	        ball                         = null;
	        ballEge                      = null;
	        stepen_Mag                   = null;
	        exists_st_Mag                = null;
	        shifr_DipBak                 = null;
	        shifr_DipSpec                = null;
	        nazv_DipBak                  = null;
	        nazv_DipSpec                 = null;
	        s_okso_1                     = null;
	        s_okso_2                     = null;
	        s_okso_3                     = null;
	        s_okso_4                     = null;
	        s_okso_5                     = null;
	        s_okso_6                     = null;
	        six_1                        = null;
	        six_2                        = null;
	        six_3                        = null;
	        six_4                        = null;
	        six_5                        = null;
	        six_6                        = null;
	        fito_1                       = null;
	        fito_2                       = null;
	        fito_3                       = null;
	        fito_4                       = null;
	        fito_5                       = null;
	        fito_6                       = null;
	        three_1                      = null;
	        three_2                      = null;
	        three_3                      = null;
	        three_4                      = null;
	        three_5                      = null;
	        three_6                      = null;
	        olimp_1                      = null;
	        olimp_2                      = null;
	        olimp_3                      = null;
	        olimp_4                      = null;
	        olimp_5                      = null;
	        olimp_6                      = null;
	        target_1                     = null;
	        target_2                     = null;
	        target_3                     = null;
	        target_4                     = null;
	        target_5                     = null;
	        target_6                     = null;
	        bud_1                        = null;
	        bud_2                        = null;
	        bud_3                        = null;
	        bud_4                        = null;
	        bud_5                        = null;
	        bud_6                        = null;
	        dop_Info                     = null;
	        dog_1                        = null;
	        dog_2                        = null;
	        dog_3                        = null;
	        dog_4                        = null;
	        dog_5                        = null;
	        dog_6                        = null;
	        dog_ok_1                     = null;
	        dog_ok_2                     = null;
	        dog_ok_3                     = null;
	        dog_ok_4                     = null;
	        dog_ok_5                     = null;
	        dog_ok_6                     = null;
	        godOkonchanijaSrObrazovanija = null;
	        dokumentyHranjatsja          = null;
	        lgoty                        = null;
	        dataJekzamena                = null;
	        dataRojdenija                = null;
	        mestoRojdenija               = null;
	        udostoverenieLgoty           = null;
	        diplomOtlichija              = null;
	        dataApelljatsii              = null;
	        medal                        = null;
	        may_del                      = null;
	        formaOb                      = null;
	        osnovaOb                     = null;
	        shifrMedali                  = null;
	        shifrLgot                    = null;
	        kodFormyOb                   = null;
	        kodOsnovyOb                  = null;
	        kopijaSertifikata            = null;
	        kodAbiturienta               = null;
	        kodTselevogoPriema           = null;
	        kodGruppy                    = null;
	        kodLgot                      = null;
	        kodSpetsialnZach             = null;
	        kodTselevogoPriema           = null;
	        kodVuza                      = null;
	        kodZapisi                    = null;
	        kodZavedenija                = null;
	        kodFakulteta                 = null;
	        kodKursov                    = null;
	        kodLgot                      = null;
	        kodMedali                    = null;
	        kodPredmeta                  = null;
	        kodPunkta                    = null;
	        kodVuza                      = null;
	        kodOblasti                   = null;
	        kodRajona                    = null;
	        kodFormyOb                   = null;
	        kodOsnovyOb                  = null;
	        shifrKursov                  = null;
	        gdePoluchilSrObrazovanie     = null;
	        inostrannyjJazyk             = null;
	        napravlenieOtPredprijatija   = null;
	        srokObuchenija               = null;
	        nujdaetsjaVObschejitii       = null;
	        pol                          = null;
	        priznakSortirovki            = null;
	        prinjat                      = null;
	        sobesedovanie                = null;
	        shifrFakulteta               = null;
	        special1                     = null;
	        sokr                         = null;
	        attestat                     = null;
	        apelljatsija                 = null;
	        ege                          = null;
	        gorod_Prop                   = null;
	        ulica_Prop                   = null;
	        useAllSpecs                  = null;
	        dom_Prop                     = null;
	        kvart_Prop                   = null;
	        examen                       = null;
	        tel                          = null;
	        zajavlen                     = null;
	        polnoeNaimenovanieZavedenija = null;
	        sobesedovanie                = null;
	        otsenkaegeabiturienta        = null;
	        otsenka_Att_ot               = null;
	        otsenka_Att_do               = null;
	        otsenka_Att                  = null;
	        otsenka_Zaj_ot               = null;
	        otsenka_Zaj_do               = null;
	        otsenka_Zaj                  = null;
	        otsenka_Ege_ot               = null;
	        otsenka_Ege_do               = null;
	        otsenka_Ege                  = null;
	        otsenka_Exam_ot              = null;
	        otsenka_Exam_do              = null;
	        otsenka_Exam                 = null;
	        otsenkiabiturienta           = null;
	        atestatabiturienta           = null;
	        special1                     = null;
	        special2                     = null;
	        special22                    = null;
	        special3                     = null;
	        special4                     = null;
	        special5                     = null;
	        special6                     = null;
	        special7                     = null;
	        special8                     = null;
	        special9                     = null;
	        special10                    = null;
	        special13                    = null;
	        tipOkonchennogoZavedenija    = null;
	        tipDokumenta                 = null;
	        tip_Spec                     = null;
	        nomerSertifikata             = null;
	        trudovajaDejatelnost         = null;
	        tselevojPriem                = null;
	        familija                     = null;
	        grajdanstvo                  = null;
	        gruppa                       = null;
	        fileName1                    = null;
	        fileName2                    = null;
	        forma_Ob1                    = null;
	        forma_Ob2                    = null;
	        forma_Ob3                    = null;
	        gruppaOplativshego           = null;
	        imja                         = null;
	        nomerDokumenta               = null;
	        seriaDokumenta               = null;
	        dataVydDokumenta             = null;
	        kemVydDokument               = null;
	        tipDokSredObraz              = null;
	        vidDokSredObraz              = null;
	        nomerLichnogoDela            = null;
	        nomerPlatnogoDogovora        = null;
	        nomerShkoly                  = null;
	        nazvanie                     = null;
	        nazvanieOblasti              = null;
	        nazvanieRajona               = null;
	        dokumentyHranjatsja          = null;
	        dogovornaja                  = null;
	        fio                          = null;
	        nomerPotoka                  = null;
	        notes                        = null;
	        nomerSertifikata             = null;
	        kopijaSertifikata            = null;
	        predmetspecial               = null;
	        polnoeNaimenovanieZavedenija = null;
	        godOkonchanijaSrObrazovanija = null;
	        otchestvo                    = null;
	        seriaAtt                     = null;
	        need_Spo                     = null;
	        nomerAtt                     = null;
	        shifrFakulteta               = null;
	        kodZavedenija                = null;
	        medSpravka                = null;
	        maxCountAbiturients          = 0;
	        medSpravka           =  null;
	        podtverjdenieMedSpravki = null;
	        abitEmail = null;
	        dopAddress = null;
	        providingSpecialCondition  = null;
	        returnDocument = null;
	        preemptiveRight = null;
	        postgraduateStudies = null;
	        traineeship = null;
	        internship = null;

	    }

	    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors) throws ServletException {

	        if ( bean != null ) {

	                ball                         = bean.getBall();
	                ballEge                      = bean.getBallEge();
	                godOkonchanijaSrObrazovanija = bean.getGodOkonchanijaSrObrazovanija();
	                kodAbiturienta               = bean.getKodAbiturienta();
	                kodTselevogoPriema           = bean.getKodTselevogoPriema();
	                kodGruppy                    = bean.getKodGruppy();
	                kodLgot                      = bean.getKodLgot();
	                kodKursov                    = bean.getKodKursov();
	                kodMedali                    = bean.getKodMedali();
	                kodPunkta                    = bean.getKodPunkta();
	                kodVuza                      = bean.getKodVuza();
	                kodOblasti                   = bean.getKodOblasti();
	                kodRajona                    = bean.getKodRajona();
	                kodZavedenija                = bean.getKodZavedenija();
	                kodPredmeta                  = bean.getKodPredmeta();
	                kodFormyOb                   = bean.getKodFormyOb();
	                kodOsnovyOb                  = bean.getKodOsnovyOb();
	                kodSpetsialnZach             = bean.getKodSpetsialnZach();
	                kodZapisi                    = bean.getKodZapisi();
	                kodFakulteta                 = bean.getKodFakulteta();
	                kodKursov                    = bean.getKodKursov();
	                kodLgot                      = bean.getKodLgot();
	                kodMedali                    = bean.getKodMedali();
	                special22                    = bean.getSpecial22();
	                otsenkaegeabiturienta        = bean.getOtsenkaegeabiturienta();
	                otsenka_Att_ot               = bean.getOtsenka_Att_ot();
	                otsenka_Att_do               = bean.getOtsenka_Att_do();
	                otsenka_Att                  = bean.getOtsenka_Att();
	                otsenka_Zaj_ot               = bean.getOtsenka_Zaj_ot();
	                otsenka_Zaj_do               = bean.getOtsenka_Zaj_do();
	                otsenka_Zaj                  = bean.getOtsenka_Zaj();
	                otsenka_Ege_ot               = bean.getOtsenka_Ege_ot();
	                otsenka_Ege_do               = bean.getOtsenka_Ege_do();
	                otsenka_Ege                  = bean.getOtsenka_Ege();
	                otsenka_Exam_ot              = bean.getOtsenka_Exam_ot();
	                otsenka_Exam_do              = bean.getOtsenka_Exam_do();
	                otsenka_Exam                 = bean.getOtsenka_Exam();
	                otsenkiabiturienta           = bean.getOtsenkiabiturienta();
	                atestatabiturienta           = bean.getAtestatabiturienta();
	                godOkonchanijaSrObrazovanija = bean.getGodOkonchanijaSrObrazovanija();
	                nomerPotoka                  = bean.getNomerPotoka();
	                maxCountAbiturients          = bean.getMaxCountAbiturients();
	                medSpravka          = bean.getMedSpravka();
	                podtverjdenieMedSpravki = bean.getPodtverjdenieMedSpravki();
	                abitEmail = bean.getAbitEmail();
	                dopAddress = bean.getDopAddress();
	                providingSpecialCondition  = bean.getProvidingSpecialCondition();
	                returnDocument = bean.getReturnDocument();
	                postgraduateStudies = bean.getPostgraduateStudies();
	                preemptiveRight = bean.getPreemptiveRight();
	                traineeship = bean.getTraineeship();
	                internship = bean.getInternship();

	            if ( bean.getStepen_Mag() != null ) {
	                stepen_Mag = bean.getStepen_Mag();
	            }
	            if ( bean.getExists_st_Mag() != null ) {
	                exists_st_Mag = bean.getExists_st_Mag();
	            }
	            if ( bean.getShifr_DipBak() != null ) {
	                shifr_DipBak = bean.getShifr_DipBak();
	            }
	            if ( bean.getShifr_DipSpec() != null ) {
	                shifr_DipSpec = bean.getShifr_DipSpec();
	            }
	            if ( bean.getNazv_DipBak() != null ) {
	                nazv_DipBak = bean.getNazv_DipBak();
	            }
	            if ( bean.getNazv_DipSpec() != null ) {
	                nazv_DipSpec = bean.getNazv_DipSpec();
	            }
	            if ( bean.getFito_1() != null ) {
	                fito_1 = bean.getFito_1();
	            }
	            if ( bean.getFito_2() != null ) {
	                fito_2 = bean.getFito_2();
	            }
	            if ( bean.getFito_3() != null ) {
	                fito_3 = bean.getFito_3();
	            }
	            if ( bean.getFito_4() != null ) {
	                fito_4 = bean.getFito_4();
	            }
	            if ( bean.getFito_5() != null ) {
	                fito_5 = bean.getFito_5();
	            }
	            if ( bean.getFito_6() != null ) {
	                fito_6 = bean.getFito_6();
	            }
	            if ( bean.getLgoty() != null ) {
	                lgoty = bean.getLgoty();
	            }
	            if ( bean.getMay_del() != null ) {
	                may_del = bean.getMay_del();
	            }
	            if ( bean.getMedal() != null ) {
	                medal = bean.getMedal();
	            }
	            if ( bean.getFormaOb() != null ) {
	                formaOb = bean.getFormaOb();
	            }
	            if ( bean.getOsnovaOb() != null ) {
	                osnovaOb = bean.getOsnovaOb();
	            }
	            if ( bean.getShifrMedali() != null ) {
	                shifrMedali = bean.getShifrMedali();
	            }
	            if ( bean.getShifrLgot() != null ) {
	                shifrLgot = bean.getShifrLgot();
	            }
	            if ( bean.getShifrKursov() != null ) {
	                shifrKursov = bean.getShifrKursov();
	            }
	            if ( bean.getDataJekzamena() != null ) {
	                dataJekzamena = bean.getDataJekzamena();
	            }
	            if ( bean.getS_okso_1() != null ) {
	                s_okso_1 = bean.getS_okso_1();
	            }
	            if ( bean.getDataApelljatsii() != null ) {
	                dataApelljatsii = bean.getDataApelljatsii();
	            }
	            if ( bean.getPriznakSortirovki() != null ) {
	                priznakSortirovki = bean.getPriznakSortirovki();
	            }
	            if ( bean.getS_okso_2() != null ) {
	                s_okso_2 = bean.getS_okso_2();
	            }
	            if ( bean.getPrinjat() != null ) {
	                prinjat = bean.getPrinjat();
	            }
	            if ( bean.getSpecial5() != null ) {
	                special5 = bean.getSpecial5();
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
	            if ( bean.getSpecial9() != null ) {
	                special9 = bean.getSpecial9();
	            }
	            if ( bean.getSpecial10() != null ) {
	                special10 = bean.getSpecial10();
	            }
	            if ( bean.getSpecial13() != null ) {
	                special13 = bean.getSpecial13();
	            }
	            if ( bean.getNazvanie() != null ) {
	                nazvanie = bean.getNazvanie();
	            }
	            if ( bean.getNazvanieOblasti() != null ) {
	                nazvanieOblasti = bean.getNazvanieOblasti();
	            }
	            if ( bean.getNazvanieRajona() != null ) {
	                nazvanieRajona = bean.getNazvanieRajona();
	            }
	            if ( bean.getSobesedovanie() != null ) {
	                sobesedovanie = bean.getSobesedovanie();
	            }
	            if ( bean.getShifrFakulteta() != null ) {
	                shifrFakulteta = bean.getShifrFakulteta();
	            }
	            if ( bean.getSpecial1() != null ) {
	                special1 = bean.getSpecial1();
	            }
	            if ( bean.getSokr() != null ) {
	                sokr = bean.getSokr();
	            }
	            if ( bean.getAttestat() != null ) {
	                attestat = bean.getAttestat();
	            }
	            if ( bean.getApelljatsija() != null ) {
	                apelljatsija = bean.getApelljatsija();
	            }
	            if ( bean.getEge() != null ) {
	                ege = bean.getEge();
	            }
	            if ( bean.getExamen() != null ) {
	                examen = bean.getExamen();
	            }
	            if ( bean.getZajavlen() != null ) {
	                zajavlen = bean.getZajavlen();
	            }
	            if ( bean.getSpecial2() != null ) {
	                special2 = bean.getSpecial2();
	            }
	            if ( bean.getSpecial3() != null ) {
	                special3 = bean.getSpecial3();
	            }
	            if ( bean.getDogovornaja() != null ) {
	                dogovornaja = bean.getDogovornaja();
	            }
	            if ( bean.getFio() != null ) {
	                fio = bean.getFio();
	            }
	            if ( bean.getGruppa() != null ) {
	                gruppa = bean.getGruppa();
	            }
	            if ( bean.getFileName1() != null ) {
	                fileName1 = bean.getFileName1();
	            }
	            if ( bean.getFileName2() != null ) {
	                fileName2 = bean.getFileName2();
	            }
	            if ( bean.getForma_Ob1() != null ) {
	                forma_Ob1 = bean.getForma_Ob1();
	            }
	            if ( bean.getForma_Ob2() != null ) {
	                forma_Ob2 = bean.getForma_Ob2();
	            }
	            if ( bean.getForma_Ob3() != null ) {
	                forma_Ob3 = bean.getForma_Ob3();
	            }
	            if ( bean.getNomerSertifikata() != null ) {
	                nomerSertifikata = bean.getNomerSertifikata();
	            }
	            if ( bean.getNotes() != null ) {
	                notes = bean.getNotes();
	            }
	            if ( bean.getKopijaSertifikata() != null ) {
	                kopijaSertifikata = bean.getKopijaSertifikata();
	            }
	            if ( bean.getPolnoeNaimenovanieZavedenija() != null ) {
	                polnoeNaimenovanieZavedenija = bean.getPolnoeNaimenovanieZavedenija();
	            }
	            if ( bean.getNeed_Spo() != null ) {
	                need_Spo = bean.getNeed_Spo();
	            }
	            if ( bean.getNomerAtt() != null ) {
	                nomerAtt = bean.getNomerAtt();
	            }
	            if ( bean.getSeriaAtt() != null ) {
	                seriaAtt = bean.getSeriaAtt();
	            }
	            if ( bean.getPredmetspecial() != null ) {
	                predmetspecial = bean.getPredmetspecial();
	            }
	            if ( bean.getS_okso_3() != null ) {
	                s_okso_3 = bean.getS_okso_3();
	            }
	            if ( bean.getS_okso_4() != null ) {
	                s_okso_4 = bean.getS_okso_4();
	            }
	            if ( bean.getS_okso_5() != null ) {
	                s_okso_5 = bean.getS_okso_5();
	            }
	            if ( bean.getS_okso_6() != null ) {
	                s_okso_6 = bean.getS_okso_6();
	            }
	            if ( bean.getSix_1() != null ) {
	                six_1 = bean.getSix_1();
	            }
	            if ( bean.getSix_2() != null ) {
	                six_2 = bean.getSix_2();
	            }
	            if ( bean.getSix_3() != null ) {
	                six_3 = bean.getSix_3();
	            }
	            if ( bean.getSix_4() != null ) {
	                six_4 = bean.getSix_4();
	            }
	            if ( bean.getSix_5() != null ) {
	                six_5 = bean.getSix_5();
	            }
	            if ( bean.getSix_6() != null ) {
	                six_6 = bean.getSix_6();
	            }
	            if ( bean.getThree_1() != null ) {
	                three_1 = bean.getThree_1();
	            }
	            if ( bean.getThree_2() != null ) {
	                three_2 = bean.getThree_2();
	            }
	            if ( bean.getThree_3() != null ) {
	                three_3 = bean.getThree_3();
	            }
	            if ( bean.getThree_4() != null ) {
	                three_4 = bean.getThree_4();
	            }
	            if ( bean.getThree_5() != null ) {
	                three_5 = bean.getThree_5();
	            }
	            if ( bean.getThree_6() != null ) {
	                three_6 = bean.getThree_6();
	            }
	            if ( bean.getOlimp_1() != null ) {
	                olimp_1 = bean.getOlimp_1();
	            }
	            if ( bean.getOlimp_2() != null ) {
	                olimp_2 = bean.getOlimp_2();
	            }
	            if ( bean.getOlimp_3() != null ) {
	                olimp_3 = bean.getOlimp_3();
	            }
	            if ( bean.getOlimp_4() != null ) {
	                olimp_4 = bean.getOlimp_4();
	            }
	            if ( bean.getOlimp_5() != null ) {
	                olimp_5 = bean.getOlimp_5();
	            }
	            if ( bean.getOlimp_6() != null ) {
	                olimp_6 = bean.getOlimp_6();
	            }
	            if ( bean.getTarget_1() != null ) {
	                target_1 = bean.getTarget_1();
	            }
	            if ( bean.getTarget_2() != null ) {
	                target_2 = bean.getTarget_2();
	            }
	            if ( bean.getTarget_3() != null ) {
	                target_3 = bean.getTarget_3();
	            }
	            if ( bean.getTarget_4() != null ) {
	                target_4 = bean.getTarget_4();
	            }
	            if ( bean.getTarget_5() != null ) {
	                target_5 = bean.getTarget_5();
	            }
	            if ( bean.getTarget_6() != null ) {
	                target_6 = bean.getTarget_6();
	            }
	            if ( bean.getBud_1() != null ) {
	                bud_1 = bean.getBud_1();
	            }
	            if ( bean.getBud_2() != null ) {
	                bud_2 = bean.getBud_2();
	            }
	            if ( bean.getBud_3() != null ) {
	                bud_3 = bean.getBud_3();
	            }
	            if ( bean.getBud_4() != null ) {
	                bud_4 = bean.getBud_4();
	            }
	            if ( bean.getBud_5() != null ) {
	                bud_5 = bean.getBud_5();
	            }
	            if ( bean.getBud_6() != null ) {
	                bud_6 = bean.getBud_6();
	            }
	            if ( bean.getDop_Info() != null ) {
	                dop_Info = bean.getDop_Info();
	            }
	            if ( bean.getDog_1() != null ) {
	                dog_1 = bean.getDog_1();
	            }
	            if ( bean.getDog_2() != null ) {
	                dog_2 = bean.getDog_2();
	            }
	            if ( bean.getDog_3() != null ) {
	                dog_3 = bean.getDog_3();
	            }
	            if ( bean.getDog_4() != null ) {
	                dog_4 = bean.getDog_4();
	            }
	            if ( bean.getDog_5() != null ) {
	                dog_5 = bean.getDog_5();
	            }
	            if ( bean.getDog_6() != null ) {
	                dog_6 = bean.getDog_6();
	            }
	            if ( bean.getDog_ok_1() != null ) {
	                dog_ok_1 = bean.getDog_ok_1();
	            }
	            if ( bean.getDog_ok_2() != null ) {
	                dog_ok_2 = bean.getDog_ok_2();
	            }
	            if ( bean.getDog_ok_3() != null ) {
	                dog_ok_3 = bean.getDog_ok_3();
	            }
	            if ( bean.getDog_ok_4() != null ) {
	                dog_ok_4 = bean.getDog_ok_4();
	            }
	            if ( bean.getDog_ok_5() != null ) {
	                dog_ok_5 = bean.getDog_ok_5();
	            }
	            if ( bean.getDog_ok_6() != null ) {
	                dog_ok_6 = bean.getDog_ok_6();
	            }
	            if ( bean.getGorod_Prop() != null ) {
	                gorod_Prop = bean.getGorod_Prop();
	            }
	            if ( bean.getUlica_Prop() != null ) {
	                ulica_Prop = bean.getUlica_Prop();
	            }
	            if ( bean.getUseAllSpecs() != null ) {
	                useAllSpecs = bean.getUseAllSpecs();
	            }
	            if ( bean.getDom_Prop() != null ) {
	                dom_Prop = bean.getDom_Prop();
	            }
	            if ( bean.getKvart_Prop() != null ) {
	                kvart_Prop = bean.getKvart_Prop();
	            }
	            if ( bean.getDataRojdenija() != null ) {
	                dataRojdenija = bean.getDataRojdenija();
	            }
	            if ( bean.getMestoRojdenija() != null ) {
	                mestoRojdenija = bean.getMestoRojdenija();
	            }
	            if ( bean.getUdostoverenieLgoty() != null ) {
	                udostoverenieLgoty = bean.getUdostoverenieLgoty();
	            }
	            if ( bean.getDiplomOtlichija() != null ) {
	                diplomOtlichija = bean.getDiplomOtlichija();
	            }
	            if ( bean.getKopijaSertifikata() != null ) {
	                kopijaSertifikata = bean.getKopijaSertifikata();
	            }
	            if ( bean.getShifrKursov() != null ) {
	                shifrKursov = bean.getShifrKursov();
	            }
	            if ( bean.getDokumentyHranjatsja() != null ) {
	                dokumentyHranjatsja = bean.getDokumentyHranjatsja();
	            }
	            if ( bean.getGdePoluchilSrObrazovanie() != null ) {
	                gdePoluchilSrObrazovanie = bean.getGdePoluchilSrObrazovanie();
	            }
	            if ( bean.getInostrannyjJazyk() != null ) {
	                inostrannyjJazyk = bean.getInostrannyjJazyk();
	            }
	            if ( bean.getNapravlenieOtPredprijatija() != null ) {
	                napravlenieOtPredprijatija = bean.getNapravlenieOtPredprijatija();
	            }
	            if ( bean.getSrokObuchenija() != null ) {
	                srokObuchenija = bean.getSrokObuchenija();
	            }
	            if ( bean.getNujdaetsjaVObschejitii() != null ) {
	                nujdaetsjaVObschejitii = bean.getNujdaetsjaVObschejitii();
	            }
	            if ( bean.getPol() != null ) {
	                pol = bean.getPol();
	            }
	            if ( bean.getAttestat() != null ) {
	                attestat = bean.getAttestat();
	            }
	            if ( bean.getEge() != null ) {
	                ege = bean.getEge();
	            }
	            if ( bean.getTel() != null ) {
	                tel = bean.getTel();
	            }
	            if ( bean.getZajavlen() != null ) {
	                zajavlen = bean.getZajavlen();
	            }
	            if ( bean.getPolnoeNaimenovanieZavedenija() != null ) {
	                polnoeNaimenovanieZavedenija = bean.getPolnoeNaimenovanieZavedenija();
	            }
	            if ( bean.getNazvanie() != null ) {
	                nazvanie = bean.getNazvanie();
	            }
	            if ( bean.getNazvanieOblasti() != null ) {
	                nazvanieOblasti = bean.getNazvanieOblasti();
	            }
	            if ( bean.getNazvanieRajona() != null ) {
	                nazvanieRajona = bean.getNazvanieRajona();
	            }
	            if ( bean.getSobesedovanie() != null ) {
	                sobesedovanie = bean.getSobesedovanie();
	            }
	            if ( bean.getSpecial1() != null ) {
	                special1 = bean.getSpecial1();
	            }
	            if ( bean.getSpecial2() != null ) {
	                special2 = bean.getSpecial2();
	            }
	            if ( bean.getSpecial3() != null ) {
	                special3 = bean.getSpecial3();
	            }
	            if ( bean.getSpecial4() != null ) {
	                special4 = bean.getSpecial4();
	            }
	            if ( bean.getTselevojPriem() != null ) {
	                tselevojPriem = bean.getTselevojPriem();
	            }
	            if ( bean.getTipOkonchennogoZavedenija() != null ) {
	                tipOkonchennogoZavedenija = bean.getTipOkonchennogoZavedenija();
	            }
	            if ( bean.getTipDokumenta() != null ) {
	                tipDokumenta = bean.getTipDokumenta();
	            }
	            if ( bean.getTip_Spec() != null ) {
	                tip_Spec = bean.getTip_Spec();
	            }
	            if ( bean.getNeed_Spo() != null ) {
	                need_Spo = bean.getNeed_Spo();
	            }
	            if ( bean.getSeriaAtt() != null ) {
	                seriaAtt = bean.getSeriaAtt();
	            }
	            if ( bean.getNomerSertifikata() != null ) {
	                nomerSertifikata = bean.getNomerSertifikata();
	            }
	            if ( bean.getTrudovajaDejatelnost() != null ) {
	                trudovajaDejatelnost = bean.getTrudovajaDejatelnost();
	            }
	            if ( bean.getFamilija() != null ) {
	                familija = bean.getFamilija();
	            }
	            if ( bean.getGrajdanstvo() != null ) {
	                grajdanstvo = bean.getGrajdanstvo();
	            }
	            if ( bean.getGruppaOplativshego() != null ) {
	                gruppaOplativshego = bean.getGruppaOplativshego();
	            }
	            if ( bean.getImja() != null ) {
	                imja = bean.getImja();
	            }
	            if ( bean.getNomerDokumenta() != null ) {
	                nomerDokumenta = bean.getNomerDokumenta();
	            }
	            if ( bean.getSeriaDokumenta() != null ) {
	                seriaDokumenta = bean.getSeriaDokumenta();
	            }
	            if ( bean.getDataVydDokumenta() != null ) {
	                dataVydDokumenta = bean.getDataVydDokumenta();
	            }
	            if ( bean.getKemVydDokument() != null ) {
	                kemVydDokument = bean.getKemVydDokument();
	            }
	            if ( bean.getTipDokSredObraz() != null ) {
	                tipDokSredObraz = bean.getTipDokSredObraz();
	            }
	            if ( bean.getVidDokSredObraz() != null ) {
	                vidDokSredObraz = bean.getVidDokSredObraz();
	            }
	            if ( bean.getNomerLichnogoDela() != null ) {
	                nomerLichnogoDela = bean.getNomerLichnogoDela();
	            }
	            if ( bean.getNomerPlatnogoDogovora() != null ) {
	                nomerPlatnogoDogovora = bean.getNomerPlatnogoDogovora();
	            }
	            if ( bean.getNomerShkoly() != null ) {
	                nomerShkoly = bean.getNomerShkoly();
	            }
	            if ( bean.getOtchestvo() != null ) {
	                otchestvo = bean.getOtchestvo();
	            }
	            if ( bean.getShifrFakulteta() != null ) {
	                shifrFakulteta = bean.getShifrFakulteta();
	            }
	        }
	    }

	    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

	        AbiturientBean bean = new AbiturientBean();      

	            bean.setBall(ball);
	            bean.setBallEge(ballEge);
	            bean.setKodSpetsialnZach(kodSpetsialnZach);
	            bean.setGodOkonchanijaSrObrazovanija(godOkonchanijaSrObrazovanija);
	            bean.setKodAbiturienta(kodAbiturienta);
	            bean.setKodTselevogoPriema(kodTselevogoPriema);
	            bean.setKodGruppy(kodGruppy);
	            bean.setKodLgot(kodLgot);
	            bean.setKodKursov(kodKursov);
	            bean.setKodMedali(kodMedali);
	            bean.setKodPredmeta(kodPredmeta);
	            bean.setKodPunkta(kodPunkta);
	            bean.setKodVuza(kodVuza);
	            bean.setKodOblasti(kodOblasti);
	            bean.setKodRajona(kodRajona);
	            bean.setKodZavedenija(kodZavedenija);
	            bean.setKodFormyOb(kodFormyOb);
	            bean.setKodOsnovyOb(kodOsnovyOb);
	            bean.setKodVuza(kodVuza);
	            bean.setKodZapisi(kodZapisi);
	            bean.setKodZavedenija(kodZavedenija);
	            bean.setKodFakulteta(kodFakulteta);
	            bean.setKodLgot(kodLgot);
	            bean.setSpecial22(special22);
	            bean.setOtsenkiabiturienta(otsenkaegeabiturienta);
	            bean.setOtsenka_Att_ot(otsenka_Att_ot);
	            bean.setOtsenka_Att_do(otsenka_Att_do);
	            bean.setOtsenka_Att(otsenka_Att);
	            bean.setOtsenka_Zaj_ot(otsenka_Zaj_ot);
	            bean.setOtsenka_Zaj_do(otsenka_Zaj_do);
	            bean.setOtsenka_Zaj(otsenka_Zaj);
	            bean.setOtsenka_Ege_ot(otsenka_Ege_ot);
	            bean.setOtsenka_Ege_do(otsenka_Ege_do);
	            bean.setOtsenka_Ege(otsenka_Ege);
	            bean.setOtsenka_Exam_ot(otsenka_Exam_ot);
	            bean.setOtsenka_Exam_do(otsenka_Exam_do);
	            bean.setOtsenka_Exam(otsenka_Exam);
	            bean.setOtsenkiabiturienta(otsenkiabiturienta);
	            bean.setAtestatabiturienta(atestatabiturienta);
	            bean.setNomerPotoka(nomerPotoka);
	            bean.setMaxCountAbiturients(maxCountAbiturients);
	            bean.setGodOkonchanijaSrObrazovanija(godOkonchanijaSrObrazovanija);
	            bean.setMedSpravka(medSpravka);
	            bean.setPodtverjdenieMedSpravki(podtverjdenieMedSpravki);
	            bean.setAbitEmail(StringUtil.toDB(abitEmail));
	            bean.setDopAddress(StringUtil.toDB(dopAddress));
	            bean.setProvidingSpecialCondition (StringUtil.toDB(providingSpecialCondition ));
	            bean.setReturnDocument(StringUtil.toDB(returnDocument));
	            bean.setPreemptiveRight(StringUtil.toDB(preemptiveRight));
	            bean.setPostgraduateStudies(StringUtil.toDB(postgraduateStudies));
	            bean.setTraineeship(StringUtil.toDB(traineeship));
	            bean.setInternship(StringUtil.toDB(internship));

	        if ( stepen_Mag!=null && !stepen_Mag.equals("") ) {
	            bean.setStepen_Mag(StringUtil.toDB(stepen_Mag));
	        }
	        if ( exists_st_Mag!=null && !exists_st_Mag.equals("") ) {
	            bean.setExists_st_Mag(StringUtil.toDB(exists_st_Mag));
	        }
	        if ( shifr_DipBak!=null && !shifr_DipBak.equals("") ) {
	            bean.setShifr_DipBak(StringUtil.toDB(shifr_DipBak));
	        }
	        if ( shifr_DipSpec!=null && !shifr_DipSpec.equals("") ) {
	            bean.setShifr_DipSpec(StringUtil.toDB(shifr_DipSpec));
	        }
	        if ( nazv_DipBak!=null && !nazv_DipBak.equals("") ) {
	            bean.setNazv_DipBak(StringUtil.toDB(nazv_DipBak));
	        }
	        if ( nazv_DipSpec!=null && !nazv_DipSpec.equals("") ) {
	            bean.setNazv_DipSpec(StringUtil.toDB(nazv_DipSpec));
	        }
	        if ( fito_1!=null && !fito_1.equals("") ) {
	            bean.setFito_1(StringUtil.toDB(fito_1));
	        }
	        if ( fito_2!=null && !fito_2.equals("") ) {
	            bean.setFito_2(StringUtil.toDB(fito_2));
	        }
	        if ( fito_3!=null && !fito_3.equals("") ) {
	            bean.setFito_3(StringUtil.toDB(fito_3));
	        }
	        if ( fito_4!=null && !fito_4.equals("") ) {
	            bean.setFito_4(StringUtil.toDB(fito_4));
	        }
	        if ( fito_5!=null && !fito_5.equals("") ) {
	            bean.setFito_5(StringUtil.toDB(fito_5));
	        }
	        if ( fito_6!=null && !fito_6.equals("") ) {
	            bean.setFito_6(StringUtil.toDB(fito_6));
	        }
	        if ( lgoty!=null && !lgoty.equals("") ) {
	            bean.setLgoty(StringUtil.toDB(lgoty));
	        }
	        if ( may_del!=null && !may_del.equals("") ) {
	            bean.setMay_del(StringUtil.toDB(may_del));
	        }
	        if ( medal!=null && !medal.equals("") ) {
	            bean.setMedal(StringUtil.toDB(medal));
	        }
	        if ( formaOb!=null && !formaOb.equals("") ) {
	            bean.setFormaOb(StringUtil.toDB(formaOb));
	        }
	        if ( osnovaOb!=null && !osnovaOb.equals("") ) {
	            bean.setOsnovaOb(StringUtil.toDB(osnovaOb));
	        }
	        if ( shifrMedali!=null && !shifrMedali.equals("") ) {
	            bean.setShifrMedali(StringUtil.toDB(shifrMedali));
	        }
	        if ( shifrLgot!=null && !shifrLgot.equals("") ) {
	            bean.setShifrLgot(StringUtil.toDB(shifrLgot));
	        }
	        if ( dataJekzamena!=null && !dataJekzamena.equals("") ) {
	            bean.setDataJekzamena(StringUtil.toDB(dataJekzamena));
	        }
	        if ( prinjat!=null && !prinjat.equals("") ) {
	            bean.setPrinjat(StringUtil.toDB(prinjat));
	        }
	        if ( sobesedovanie!=null && !sobesedovanie.equals("") ) {
	            bean.setSobesedovanie(StringUtil.toDB(sobesedovanie));
	        }
	        if ( shifrFakulteta!=null && !shifrFakulteta.equals("") ) {
	            bean.setShifrFakulteta(StringUtil.toDB(shifrFakulteta));
	        }
	        if ( apelljatsija!=null && !apelljatsija.equals("") ) {
	            bean.setApelljatsija(StringUtil.toDB(apelljatsija));
	        }
	        if ( special5!=null && !special5.equals("") ) {
	            bean.setSpecial5(StringUtil.toDB(special5));
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
	        if ( special9!=null && !special9.equals("") ) {
	            bean.setSpecial9(StringUtil.toDB(special9));
	        }
	        if ( special10!=null && !special10.equals("") ) {
	            bean.setSpecial10(StringUtil.toDB(special10));
	        }
	        if ( gruppa!=null && !gruppa.equals("") ) {
	            bean.setGruppa(StringUtil.toDB(gruppa));
	        }
	        if ( fileName1!=null && !fileName1.equals("") ) {
	            bean.setFileName1(StringUtil.toDB(fileName1));
	        }
	        if ( fileName2!=null && !fileName2.equals("") ) {
	            bean.setFileName2(StringUtil.toDB(fileName2));
	        }
	        if ( forma_Ob1!=null && !forma_Ob1.equals("") ) {
	            bean.setForma_Ob1(StringUtil.toDB(forma_Ob1));
	        }
	        if ( forma_Ob2!=null && !forma_Ob2.equals("") ) {
	            bean.setForma_Ob2(StringUtil.toDB(forma_Ob2));
	        }
	        if ( forma_Ob3!=null && !forma_Ob3.equals("") ) {
	            bean.setForma_Ob3(StringUtil.toDB(forma_Ob3));
	        }
	        if ( nomerSertifikata!=null && !nomerSertifikata.equals("") ) {
	            bean.setNomerSertifikata(StringUtil.toDB(nomerSertifikata));
	        }
	        if ( notes!=null && !notes.equals("") ) {
	            bean.setNotes(notes);
	        }
	        if ( need_Spo!=null && !need_Spo.equals("") ) {
	            bean.setNeed_Spo(StringUtil.toDB(need_Spo));
	        }
	        if ( polnoeNaimenovanieZavedenija!=null && !polnoeNaimenovanieZavedenija.equals("") ) {
	            bean.setPolnoeNaimenovanieZavedenija(StringUtil.toDB(polnoeNaimenovanieZavedenija));
	        }
	        if ( predmetspecial!=null && !predmetspecial.equals("") ) {
	            bean.setPredmetspecial(StringUtil.toDB(predmetspecial));
	        }
	        if ( seriaAtt!=null && !seriaAtt.equals("") ) {
	            bean.setSeriaAtt(StringUtil.toDB(seriaAtt));
	        }
	        if ( kopijaSertifikata!=null && !kopijaSertifikata.equals("") ) {
	            bean.setKopijaSertifikata(StringUtil.toDB(kopijaSertifikata));
	        }
	        if ( dogovornaja!=null && !dogovornaja.equals("") ) {
	            bean.setDogovornaja(StringUtil.toDB(dogovornaja));
	        }
	        if ( fio!=null && !fio.equals("") ) {
	            bean.setFio(StringUtil.toDB(fio));
	        }
	        if ( special13!=null && !special13.equals("") ) {
	            bean.setSpecial13(StringUtil.toDB(special13));
	        }
	        if ( special1!=null && !special1.equals("") ) {
	            bean.setSpecial1(StringUtil.toDB(special1));
	        }
	        if ( sokr!=null && !sokr.equals("") ) {
	            bean.setSokr(StringUtil.toDB(sokr));
	        }
	        if ( dataRojdenija!=null && !dataRojdenija.equals("") ) {
	            bean.setDataRojdenija(StringUtil.toDB(dataRojdenija));
	        }
	        if ( mestoRojdenija!=null && !mestoRojdenija.equals("") ) {
	            bean.setMestoRojdenija(StringUtil.toDB(mestoRojdenija));
	        }
	        if ( udostoverenieLgoty!=null && !udostoverenieLgoty.equals("") ) {
	            bean.setUdostoverenieLgoty(StringUtil.toDB(udostoverenieLgoty));
	        }
	        if ( diplomOtlichija!=null && !diplomOtlichija.equals("") ) {
	            bean.setDiplomOtlichija(StringUtil.toDB(diplomOtlichija));
	        }
	        if ( priznakSortirovki!=null && !priznakSortirovki.equals("") ) {
	            bean.setPriznakSortirovki(StringUtil.toDB(priznakSortirovki));
	        }
	        if ( dataApelljatsii!=null && !dataApelljatsii.equals("") ) {
	            bean.setDataApelljatsii(StringUtil.toDB(dataApelljatsii));
	        }
	        if ( s_okso_1!=null && !s_okso_1.equals("") ) {
	            bean.setS_okso_1(StringUtil.toDB(s_okso_1));
	        }
	        if ( s_okso_2!=null && !s_okso_2.equals("") ) {
	            bean.setS_okso_2(StringUtil.toDB(s_okso_2));
	        }
	        if ( s_okso_3!=null && !s_okso_3.equals("") ) {
	            bean.setS_okso_3(StringUtil.toDB(s_okso_3));
	        }
	        if ( s_okso_4!=null && !s_okso_4.equals("") ) {
	            bean.setS_okso_4(StringUtil.toDB(s_okso_4));
	        }
	        if ( s_okso_5!=null && !s_okso_5.equals("") ) {
	            bean.setS_okso_5(StringUtil.toDB(s_okso_5));
	        }
	        if ( s_okso_6!=null && !s_okso_6.equals("") ) {
	            bean.setS_okso_6(StringUtil.toDB(s_okso_6));
	        }
	        if ( six_1!=null && !six_1.equals("") ) {
	            bean.setSix_1(StringUtil.toDB(six_1));
	        }
	        if ( six_2!=null && !six_2.equals("") ) {
	            bean.setSix_2(StringUtil.toDB(six_2));
	        }
	        if ( six_3!=null && !six_3.equals("") ) {
	            bean.setSix_3(StringUtil.toDB(six_3));
	        }
	        if ( six_4!=null && !six_4.equals("") ) {
	            bean.setSix_4(StringUtil.toDB(six_4));
	        }
	        if ( six_5!=null && !six_5.equals("") ) {
	            bean.setSix_5(StringUtil.toDB(six_5));
	        }
	        if ( six_6!=null && !six_6.equals("") ) {
	            bean.setSix_6(StringUtil.toDB(six_6));
	        }
	        if ( olimp_1!=null && !olimp_1.equals("") ) {
	            bean.setOlimp_1(StringUtil.toDB(olimp_1));
	        }
	        if ( olimp_2!=null && !olimp_2.equals("") ) {
	            bean.setOlimp_2(StringUtil.toDB(olimp_2));
	        }
	        if ( olimp_3!=null && !olimp_3.equals("") ) {
	            bean.setOlimp_3(StringUtil.toDB(olimp_3));
	        }
	        if ( olimp_4!=null && !olimp_4.equals("") ) {
	            bean.setOlimp_4(StringUtil.toDB(olimp_4));
	        }
	        if ( olimp_5!=null && !olimp_5.equals("") ) {
	            bean.setOlimp_5(StringUtil.toDB(olimp_5));
	        }
	        if ( olimp_6!=null && !olimp_6.equals("") ) {
	            bean.setOlimp_6(StringUtil.toDB(olimp_6));
	        }
	        if ( target_1!=null && !target_1.equals("") ) {
	            bean.setTarget_1(StringUtil.toDB(target_1));
	        }
	        if ( target_2!=null && !target_2.equals("") ) {
	            bean.setTarget_2(StringUtil.toDB(target_2));
	        }
	        if ( target_3!=null && !target_3.equals("") ) {
	            bean.setTarget_3(StringUtil.toDB(target_3));
	        }
	        if ( target_4!=null && !target_4.equals("") ) {
	            bean.setTarget_4(StringUtil.toDB(target_4));
	        }
	        if ( target_5!=null && !target_5.equals("") ) {
	            bean.setTarget_5(StringUtil.toDB(target_5));
	        }
	        if ( target_6!=null && !target_6.equals("") ) {
	            bean.setTarget_6(StringUtil.toDB(target_6));
	        }
	        if ( three_1!=null && !three_1.equals("") ) {
	            bean.setThree_1(StringUtil.toDB(three_1));
	        }
	        if ( three_2!=null && !three_2.equals("") ) {
	            bean.setThree_2(StringUtil.toDB(three_2));
	        }
	        if ( three_3!=null && !three_3.equals("") ) {
	            bean.setThree_3(StringUtil.toDB(three_3));
	        }
	        if ( three_4!=null && !three_4.equals("") ) {
	            bean.setThree_4(StringUtil.toDB(three_4));
	        }
	        if ( three_5!=null && !three_5.equals("") ) {
	            bean.setThree_5(StringUtil.toDB(three_5));
	        }
	        if ( three_6!=null && !three_6.equals("") ) {
	            bean.setThree_6(StringUtil.toDB(three_6));
	        }
	        if ( bud_1!=null && !bud_1.equals("") ) {
	            bean.setBud_1(StringUtil.toDB(bud_1));
	        }
	        if ( bud_2!=null && !bud_2.equals("") ) {
	            bean.setBud_2(StringUtil.toDB(bud_2));
	        }
	        if ( bud_3!=null && !bud_3.equals("") ) {
	            bean.setBud_3(StringUtil.toDB(bud_3));
	        }
	        if ( bud_4!=null && !bud_4.equals("") ) {
	            bean.setBud_4(StringUtil.toDB(bud_4));
	        }
	        if ( bud_5!=null && !bud_5.equals("") ) {
	            bean.setBud_5(StringUtil.toDB(bud_5));
	        }
	        if ( bud_6!=null && !bud_6.equals("") ) {
	            bean.setBud_6(StringUtil.toDB(bud_6));
	        }
	        if ( dop_Info!=null && !dop_Info.equals("") ) {
	            bean.setDop_Info(StringUtil.toDB(dop_Info));
	        }
	        if ( dog_1!=null && !dog_1.equals("") ) {
	            bean.setDog_1(StringUtil.toDB(dog_1));
	        }
	        if ( dog_2!=null && !dog_2.equals("") ) {
	            bean.setDog_2(StringUtil.toDB(dog_2));
	        }
	        if ( dog_3!=null && !dog_3.equals("") ) {
	            bean.setDog_3(StringUtil.toDB(dog_3));
	        }
	        if ( dog_4!=null && !dog_4.equals("") ) {
	            bean.setDog_4(StringUtil.toDB(dog_4));
	        }
	        if ( dog_5!=null && !dog_5.equals("") ) {
	            bean.setDog_5(StringUtil.toDB(dog_5));
	        }
	        if ( dog_6!=null && !dog_6.equals("") ) {
	            bean.setDog_6(StringUtil.toDB(dog_6));
	        }
	        if ( dog_ok_1!=null && !dog_ok_1.equals("") ) {
	            bean.setDog_ok_1(StringUtil.toDB(dog_ok_1));
	        }
	        if ( dog_ok_2!=null && !dog_ok_2.equals("") ) {
	            bean.setDog_ok_2(StringUtil.toDB(dog_ok_2));
	        }
	        if ( dog_ok_3!=null && !dog_ok_3.equals("") ) {
	            bean.setDog_ok_3(StringUtil.toDB(dog_ok_3));
	        }
	        if ( dog_ok_4!=null && !dog_ok_4.equals("") ) {
	            bean.setDog_ok_4(StringUtil.toDB(dog_ok_4));
	        }
	        if ( dog_ok_5!=null && !dog_ok_5.equals("") ) {
	            bean.setDog_ok_5(StringUtil.toDB(dog_ok_5));
	        }
	        if ( dog_ok_6!=null && !dog_ok_6.equals("") ) {
	            bean.setDog_ok_6(StringUtil.toDB(dog_ok_6));
	        }
	        if ( gorod_Prop!=null && !gorod_Prop.equals("") ) {
	            bean.setGorod_Prop(StringUtil.toDB(gorod_Prop));
	        }
	        if ( ulica_Prop!=null && !ulica_Prop.equals("") ) {
	            bean.setUlica_Prop(StringUtil.toDB(ulica_Prop));
	        }
	        if ( useAllSpecs!=null && !useAllSpecs.equals("") ) {
	            bean.setUseAllSpecs(StringUtil.toDB(useAllSpecs));
	        }
	        if ( dom_Prop!=null && !dom_Prop.equals("") ) {
	            bean.setDom_Prop(StringUtil.toDB(dom_Prop));
	        }
	        if ( kvart_Prop!=null && !kvart_Prop.equals("") ) {
	            bean.setKvart_Prop(StringUtil.toDB(kvart_Prop));
	        }
	        if ( kopijaSertifikata!=null && !kopijaSertifikata.equals("") ) {
	            bean.setKopijaSertifikata(StringUtil.toDB(kopijaSertifikata));
	        }
	        if ( shifrKursov!=null && !shifrKursov.equals("") ) {
	            bean.setShifrKursov(StringUtil.toDB(shifrKursov));
	        }
	        if ( dokumentyHranjatsja!=null && !dokumentyHranjatsja.equals("") ) {
	            bean.setDokumentyHranjatsja(StringUtil.toDB(dokumentyHranjatsja));
	        }
	        if ( gdePoluchilSrObrazovanie!=null && !gdePoluchilSrObrazovanie.equals("") ) {
	            bean.setGdePoluchilSrObrazovanie(StringUtil.toDB(gdePoluchilSrObrazovanie));
	        }
	        if ( inostrannyjJazyk!=null && !inostrannyjJazyk.equals("") ) {
	            bean.setInostrannyjJazyk(StringUtil.toDB(inostrannyjJazyk));
	        }
	        if ( napravlenieOtPredprijatija!=null && !napravlenieOtPredprijatija.equals("") ) {
	            bean.setNapravlenieOtPredprijatija(StringUtil.toDB(napravlenieOtPredprijatija));
	        }
	        if ( srokObuchenija!=null && !srokObuchenija.equals("") ) {
	            bean.setSrokObuchenija(StringUtil.toDB(srokObuchenija));
	        }
	        if ( nujdaetsjaVObschejitii!=null && !nujdaetsjaVObschejitii.equals("") ) {
	            bean.setNujdaetsjaVObschejitii(StringUtil.toDB(nujdaetsjaVObschejitii));
	        }
	        if ( pol!=null && !pol.equals("") ) {
	            bean.setPol(StringUtil.toDB(pol));
	        }
	        if ( attestat!=null && !attestat.equals("") ) {
	            bean.setAttestat(StringUtil.toDB(attestat));
	        }
	        if ( ege!=null && !ege.equals("") ) {
	            bean.setEge(StringUtil.toDB(ege));
	        }
	        if ( examen!=null && !examen.equals("") ) {
	            bean.setExamen(StringUtil.toDB(examen));
	        }
	        if ( tel!=null && !tel.equals("") ) {
	            bean.setTel(StringUtil.toDB(tel));
	        }
	        if ( zajavlen!=null && !zajavlen.equals("") ) {
	            bean.setZajavlen(StringUtil.toDB(zajavlen));
	        }
	        if ( polnoeNaimenovanieZavedenija!=null && !polnoeNaimenovanieZavedenija.equals("") ) {
	            bean.setPolnoeNaimenovanieZavedenija(StringUtil.toDB(polnoeNaimenovanieZavedenija));
	        }
	        if ( sobesedovanie!=null && !sobesedovanie.equals("") ) {
	            bean.setSobesedovanie(StringUtil.toDB(sobesedovanie));
	        }
	        if ( special1!=null && !special1.equals("") ) {
	            bean.setSpecial1(StringUtil.toDB(special1));
	        }
	        if ( special2!=null && !special2.equals("") ) {
	            bean.setSpecial2(StringUtil.toDB(special2));
	        }
	        if ( special3!=null && !special3.equals("") ) {
	            bean.setSpecial3(StringUtil.toDB(special3));
	        }
	        if ( special4!=null && !special4.equals("") ) {
	            bean.setSpecial4(StringUtil.toDB(special4));
	        }
	        if ( tipOkonchennogoZavedenija!=null && !tipOkonchennogoZavedenija.equals("") ) {
	            bean.setTipOkonchennogoZavedenija(StringUtil.toDB(tipOkonchennogoZavedenija));
	        }
	        if ( tipDokumenta!=null && !tipDokumenta.equals("") ) {
	            bean.setTipDokumenta(StringUtil.toDB(tipDokumenta));
	        }
	        if ( tip_Spec!=null && !tip_Spec.equals("") ) {
	            bean.setTip_Spec(StringUtil.toDB(tip_Spec));
	        }
	        if ( nomerSertifikata!=null && !nomerSertifikata.equals("") ) {
	            bean.setNomerSertifikata(StringUtil.toDB(nomerSertifikata));
	        }
	        if ( trudovajaDejatelnost!=null && !trudovajaDejatelnost.equals("") ) {
	            bean.setTrudovajaDejatelnost(StringUtil.toDB(trudovajaDejatelnost));
	        }
	        if ( tselevojPriem!=null && !tselevojPriem.equals("") ) {
	            bean.setTselevojPriem(StringUtil.toDB(tselevojPriem));
	        }
	        if ( familija!=null && !familija.equals("") ) {
	            bean.setFamilija(StringUtil.toDB(familija));
	        }
	        if ( grajdanstvo!=null && !grajdanstvo.equals("") ) {
	            bean.setGrajdanstvo(StringUtil.toDB(grajdanstvo));
	        }
	        if ( gruppaOplativshego!=null && !gruppaOplativshego.equals("") ) {
	            bean.setGruppaOplativshego(StringUtil.toDB(gruppaOplativshego));
	        }
	        if ( imja!=null && !imja.equals("") ) {
	            bean.setImja(StringUtil.toDB(imja));
	        }
	        if ( nomerDokumenta!=null && !nomerDokumenta.equals("") ) {
	            bean.setNomerDokumenta(StringUtil.toDB(nomerDokumenta));
	        }
	        if ( seriaDokumenta!=null && !seriaDokumenta.equals("") ) {
	            bean.setSeriaDokumenta(StringUtil.toDB(seriaDokumenta));
	        }
	        if ( dataVydDokumenta!=null && !dataVydDokumenta.equals("") ) {
	            bean.setDataVydDokumenta(StringUtil.toDB(dataVydDokumenta));
	        }
	        if ( kemVydDokument!=null && !kemVydDokument.equals("") ) {
	            bean.setKemVydDokument(StringUtil.toDB(kemVydDokument));
	        }
	        if ( tipDokSredObraz!=null && !tipDokSredObraz.equals("") ) {
	            bean.setTipDokSredObraz(StringUtil.toDB(tipDokSredObraz));
	        }
	        if ( vidDokSredObraz!=null && !vidDokSredObraz.equals("") ) {
	            bean.setVidDokSredObraz(StringUtil.toDB(vidDokSredObraz));
	        }
	        if ( nomerAtt!=null && !nomerAtt.equals("") ) {
	            bean.setNomerAtt(StringUtil.toDB(nomerAtt));
	        }
	        if ( seriaAtt!=null && !seriaAtt.equals("") ) {
	            bean.setSeriaAtt(StringUtil.toDB(seriaAtt));
	        }
	        if ( nomerLichnogoDela!=null && !nomerLichnogoDela.equals("") ) {
	            bean.setNomerLichnogoDela(StringUtil.toDB(nomerLichnogoDela));
	        }
	        if ( nomerPlatnogoDogovora!=null && !nomerPlatnogoDogovora.equals("") ) {
	            bean.setNomerPlatnogoDogovora(StringUtil.toDB(nomerPlatnogoDogovora));
	        }
	        if ( nomerShkoly!=null && !nomerShkoly.equals("") ) {
	            bean.setNomerShkoly(StringUtil.toDB(nomerShkoly));
	        }
	        if ( nazvanie!=null && !nazvanie.equals("") ) {
	            bean.setNazvanie(StringUtil.toDB(nazvanie));
	        }
	        if ( nazvanieOblasti!=null && !nazvanieOblasti.equals("") ) {
	            bean.setNazvanieOblasti(StringUtil.toDB(nazvanieOblasti));
	        }
	        if ( nazvanieRajona!=null && !nazvanieRajona.equals("") ) {
	            bean.setNazvanieRajona(StringUtil.toDB(nazvanieRajona));
	        }
	        if ( otchestvo!=null && !otchestvo.equals("") ) {
	            bean.setOtchestvo(StringUtil.toDB(otchestvo));
	        }
	        if ( shifrFakulteta!=null && !shifrFakulteta.equals("") ) {
	            bean.setShifrFakulteta(StringUtil.toDB(shifrFakulteta));
	        }
	        return bean;
	    }      
	

}
