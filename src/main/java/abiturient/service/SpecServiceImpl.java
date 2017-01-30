package abiturient.service;

/**
 * Created by black on 27.01.17.
 */
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import abiturient.dao.SpecDao;
import abiturient.model.Spec;


@Service("specService")
@Transactional
public class SpecServiceImpl implements SpecService{

    @Autowired
    private SpecDao dao;

    public Spec findById(int id) {
        return dao.findById(id);
    }

    public Spec findByName(String name) {
        Spec spec = dao.findByName(name);
        return spec;
    }

    public void saveSpec(Spec spec) {
        dao.save(spec);
    }

    /*
     * Since the method is running with Transaction, No need to call hibernate update explicitly.
     * Just fetch the entity from db and update it with proper values within transaction.
     * It will be updated in db once transaction ends.
     */
    public void updateSpec(Spec spec) {
        Spec entity = dao.findById(spec.getId());
        if(entity!=null){
            entity.setName(spec.getName());
            entity.setCode(spec.getCode());
            entity.setShifr(spec.getShifr());
            entity.setDir(spec.getDir());
            entity.setFis_id(spec.getFis_id());
            entity.setO_b(spec.getO_b());
            entity.setO_l(spec.getO_l());
            entity.setO_t(spec.getO_t());
            entity.setO_d(spec.getO_d());
            entity.setZ_b(spec.getZ_b());
            entity.setZ_l(spec.getZ_l());
            entity.setZ_t(spec.getZ_t());
            entity.setZ_d(spec.getZ_d());
        }
    }


    public void deleteById(int id) {
        dao.deleteById(id);
    }

    public List<Spec> findAllSpecs() {
        return dao.findAllSpecs();
    }

    public boolean isSpecNameUnique(Integer id, String name) {
        Spec spec = findByName(name);
        return ( spec == null || ((id != null) && (spec.getId() == id)));
    }

}