package finder.service;


import finder.repo.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import finder.model.Vacancy;

import java.util.*;

@Service
public class VacancyService {


    @Autowired
    VacancyRepository vacancyRepository;


    @Transactional
   public void saveVacancy(List<Vacancy> vacancyList){
        vacancyRepository.saveAll(vacancyList);
    }

}
