package abiturient.model;

/**
 * Created by black on 27.01.17.
 */
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="spec")
public class Spec implements Serializable{

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @Column(name="LEVEL", unique=true, nullable=false)
    private String level;

    @NotEmpty
    @Column(name="NAME", unique=true, nullable=false)
    private String name;

    @NotEmpty
    @Column(name="CODE", nullable=false)
    private String code;

    @NotEmpty
    @Column(name="shifr", nullable=false)
    private String shifr;

    @NotEmpty
    @Column(name="dir", nullable=false)
    private String dir;


    @Column(name="fis_id", nullable=false)
    private Integer fis_id;

    @Column(name="o_b", nullable=false)
    private Integer o_b;


    @Column(name="o_l", nullable=false)
    private Integer o_l;


    @Column(name="o_t", nullable=false)
    private Integer o_t;


    @Column(name="o_d", nullable=false)
    private Integer o_d;


    @Column(name="z_b", nullable=false)
    private Integer z_b;


    @Column(name="z_l", nullable=false)
    private Integer z_l;


    @Column(name="z_t", nullable=false)
    private Integer z_t;


    @Column(name="z_d", nullable=false)
    private Integer z_d;

    @Column(name="year", nullable=false)
    private Integer year;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShifr() {
        return shifr;
    }

    public void setShifr(String shifr) {
        this.shifr = shifr;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public Integer getFis_id() {
        return fis_id;
    }

    public void setFis_id(Integer fis_id) {
        this.fis_id = fis_id;
    }

    public Integer getO_b() {
        return o_b;
    }

    public void setO_b(Integer o_b) {
        this.o_b = o_b;
    }

    public Integer getO_l() {
        return o_l;
    }

    public void setO_l(Integer o_l) {
        this.o_l = o_l;
    }

    public Integer getO_t() {
        return o_t;
    }

    public void setO_t(Integer o_t) {
        this.o_t = o_t;
    }

    public Integer getO_d() {
        return o_d;
    }

    public void setO_d(Integer o_d) {
        this.o_d = o_d;
    }

    public Integer getZ_b() {
        return z_b;
    }

    public void setZ_b(Integer z_b) {
        this.z_b = z_b;
    }

    public Integer getZ_l() {
        return z_l;
    }

    public void setZ_l(Integer z_l) {
        this.z_l = z_l;
    }

    public Integer getZ_t() {
        return z_t;
    }

    public void setZ_t(Integer z_t) {
        this.z_t = z_t;
    }

    public Integer getZ_d() {
        return z_d;
    }

    public void setZ_d(Integer z_d) {
        this.z_d = z_d;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof User))
            return false;
        Spec other = (Spec) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    /*
     * DO-NOT-INCLUDE passwords in toString function.
     * It is done here just for convenience purpose.
     */
    @Override
    public String toString() {
        return "Spec [id=" + id + ", Name=" + name + ", Code=" + code
                + ", Shifr=" + shifr + ", Dir=" + dir
                + ", fis_id =" + fis_id + "]";
    }



}