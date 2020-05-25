package finder.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import finder.model.Vacancy;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy,Long> {
}
