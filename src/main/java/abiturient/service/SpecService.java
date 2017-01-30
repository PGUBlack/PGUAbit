package abiturient.service;

/**
 * Created by black on 27.01.17.
 */
import java.util.List;

import abiturient.model.Spec;


public interface SpecService {

    Spec findById(int id);

    Spec findByName(String name);

    void saveSpec(Spec spec);

    void updateSpec(Spec spec);

    void deleteById(int id);

    List<Spec> findAllSpecs();

    boolean isSpecNameUnique(Integer id, String name);

}