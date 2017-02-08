package abiturient.service;

/**
 * Created by black on 03.02.17.
 */
import java.util.List;

import abiturient.model.FisSpec;


public interface FisSpecService {

    FisSpec findById(int id);

    FisSpec findByName(String name);

    void saveFisSpec(FisSpec fisSpec);

    void updateFisSpec(FisSpec fisSpec);

    void deleteById(int id);

    List<FisSpec> findAllFisSpecs();

    List<FisSpec> findByLevel(String level);

}
