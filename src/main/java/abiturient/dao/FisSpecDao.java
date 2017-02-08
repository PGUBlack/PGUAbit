package abiturient.dao;

/**
 * Created by black on 03.02.17.
 */

import java.util.List;

import abiturient.model.FisSpec;


public interface FisSpecDao {

    FisSpec findById(int id);

    FisSpec findByName(String name);

    List<FisSpec> findByLevel(String level);

    void save(FisSpec fisSpec);

    void deleteById(int id);

    List<FisSpec> findAllFisSpecs();

}