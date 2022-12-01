package vn.edu.fpt.account.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.account.entity.CurriculumVitae;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 02/12/2022 - 00:26
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Repository
public interface CVRepository extends MongoRepository<CurriculumVitae, String> {

}
