package abiturient.service;

/**
 * Created by black on 03.02.17.
 */
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import abiturient.dao.FisSpecDao;
import abiturient.model.FisSpec;


@Service("fisSpecService")
@Transactional
public class FisSpecServiceImpl implements FisSpecService{

    @Autowired
    private FisSpecDao dao;

    public FisSpec findById(int id) {
        return dao.findById(id);
    }

    public FisSpec findByName(String name) {
        FisSpec fisSpec = dao.findByName(name);
        return fisSpec;
    }

    public void saveFisSpec(FisSpec fisSpec) {
        dao.save(fisSpec);
    }

    /*
     * Since the method is running with Transaction, No need to call hibernate update explicitly.
     * Just fetch the entity from db and update it with proper values within transaction.
     * It will be updated in db once transaction ends.
     */
    public void updateFisSpec(FisSpec fisSpec) {
        FisSpec entity = dao.findById(fisSpec.getId());
        if(entity!=null){
            entity.setName(fisSpec.getName());
            entity.setCode(fisSpec.getCode());
            entity.setUgscode(fisSpec.getUgscode());
            entity.setUgsname(fisSpec.getUgsname());
            entity.setMoodle_id(fisSpec.getMoodle_id());
            entity.setLevel(fisSpec.getLevel());

        }
    }


    public void deleteById(int id) {
        dao.deleteById(id);
    }

    public List<FisSpec> findAllFisSpecs() {
        return dao.findAllFisSpecs();
    }

    public List<FisSpec> findByLevel(String level) {
        return dao.findByLevel(level);
    }


}
