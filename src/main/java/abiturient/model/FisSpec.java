package abiturient.model;

/**
 * Created by black on 02.02.17.
 */
import java.io.Serializable;
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
@Table(name="fis_specs")

public class FisSpec implements Serializable{

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
    @Column(name="UGSCODE", nullable=false)
    private String ugscode;

    @NotEmpty
    @Column(name="UGSNAME", nullable=false)
    private String ugsname;

    @Column(name="MOODLE_ID")
    private Integer moodle_id;

    public Integer getMoodle_id() {
        return moodle_id;
    }

    public void setMoodle_id(Integer moodle_id) {
        this.moodle_id = moodle_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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

    public String getUgscode() {
        return ugscode;
    }

    public void setUgscode(String ugscode) {
        this.ugscode = ugscode;
    }

    public String getUgsname() {
        return ugsname;
    }

    public void setUgsname(String ugsname) {
        this.ugsname = ugsname;
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
        FisSpec other = (FisSpec) obj;
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
        return "FisSpec [id=" + id + ", Name=" + name + ", Code=" + code
                + ", UgsCODE=" + ugscode + ", UgsNAME=" + ugsname
                + ", fis_id =" + moodle_id + "]";
    }
}
