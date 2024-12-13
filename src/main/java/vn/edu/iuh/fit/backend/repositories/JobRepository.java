package vn.edu.iuh.fit.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.backend.models.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findByJobNameContainingOrCompany_CompNameContainingOrJobSkills_Skill_SkillNameContaining(
            String jobName, String companyName, String skillName, Pageable pageable);
}