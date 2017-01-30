package abiturient.dao;

/**
 * Created by black on 27.01.17.
 */
import java.util.List;

import abiturient.model.Spec;


public interface SpecDao {

    Spec findById(int id);

    Spec findByName(String name);

    void save(Spec spec);

    void deleteById(int id);

    List<Spec> findAllSpecs();

}