package vn.edu.iuh.fit.backend.services;

import vn.edu.iuh.fit.backend.enums.SkillLevel;
import vn.edu.iuh.fit.backend.models.Candidate;
import vn.edu.iuh.fit.backend.models.Job;
import vn.edu.iuh.fit.backend.models.JobSkill;
import vn.edu.iuh.fit.backend.repositories.CandidateRepository;
import vn.edu.iuh.fit.backend.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidateServices {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JobRepository jobRepository;

    /**
     * Find all candidates with pagination
     * @param pageNo - page number
     * @param pageSize - number of items per page
     * @param sortBy - sort by field
     * @param sortDirection - sort direction (desc/asc)
     * @return - page of candidates
     */
    public Page<Candidate> findAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy); // Sort.by(Sort.Order.asc("name"))
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort); // PageRequest.of(0, 10, sort)
        return candidateRepository.findAll(pageable);//findFirst.../findTop...
    }

//    public Page<Candidate> findPaginated(Pageable pageable) {
//        int pageSize = pageable.getPageSize();
//        int currentPage = pageable.getPageNumber();
//        int startItem = currentPage * pageSize;
//        List<Candidate> list;
//        List<Candidate> candidates = candidateRepository.findAll();
//
//        if (candidates.size() < startItem) {
//            list = Collections.emptyList();
//        } else {
//            int toIndex = Math.min(startItem + pageSize, candidates.size());
//            list = candidates.subList(startItem, toIndex);
//        }
//
//        Page<Candidate> candidatePage
//                = new PageImpl<>(list, PageRequest.of(currentPage, pageSize), candidates.size());
//
//        return candidatePage;
//    }

    /**
     * Find candidates for a job
     *  select c from Candidate c inner join c.candidateSkills candidateSkills
     *   where candidateSkills.skillLevel = ?1 and candidateSkills.skill.skillName = ?2
     * @param jobId
     * @return list of candidates
     */
    public List<Candidate> findCandidatesForJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found")); // return job
        return job.getJobSkills().stream()
                .flatMap(jobSkill -> candidateRepository.findBySkillLevelAndSkillName(
                        jobSkill.getSkillLevel(), jobSkill.getSkill().getSkillName()).stream())
                .collect(Collectors.toList());
    }

    /**
     * Suggest skills to learn for a candidate
     * Not choose the skills that the candidate already has and dislay only once
     * @param candidateId
     * @return list of skills
     */
    public List<JobSkill> suggestSkillToLearn(Long candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        return jobRepository.findAll().stream()
                .flatMap(job -> job.getJobSkills().stream())
                .filter(jobSkill -> !candidate.getCandidateSkills().contains(jobSkill))
                .distinct()
                .collect(Collectors.toList());
    }


}