package vn.edu.iuh.fit.frontend.controllers;

import vn.edu.iuh.fit.backend.ids.JobSkillId;
import vn.edu.iuh.fit.backend.models.Candidate;
import vn.edu.iuh.fit.backend.models.Company;
import vn.edu.iuh.fit.backend.models.Job;
import vn.edu.iuh.fit.backend.models.JobSkill;
import vn.edu.iuh.fit.backend.repositories.CandidateRepository;
import vn.edu.iuh.fit.backend.repositories.CompanyRepository;
import vn.edu.iuh.fit.backend.repositories.JobRepository;
import vn.edu.iuh.fit.backend.repositories.JobSkillRepository;
import vn.edu.iuh.fit.backend.services.CandidateServices;
import vn.edu.iuh.fit.backend.services.EmailService;
import vn.edu.iuh.fit.backend.services.JobServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobServices jobServices;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobSkillRepository jobSkillRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private CandidateServices candidateServices;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Show list of jobs no paging
     * @param model
     * @return
     */
    @GetMapping("/list_job")
    public String showJobList(Model model) {
        model.addAttribute("jobs", jobRepository.findAll());
        return "jobs/list_no_paging_job";
    }

    /**
     * Show list of jobs with paging
     * @param model
     * @param page
     * @param search
     * @param size
     * @return
     */
    @GetMapping("")
    public String showJobListPaging(Model model,
                                    @RequestParam("page") Optional<Integer> page,
                                    @RequestParam(("search")) Optional<String> search,
                                    @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<Job> jobPage = jobServices.findAll(currentPage - 1, pageSize, "id", "asc");

        if (search.isPresent()) {
            jobPage = jobServices.searchJobs(currentPage - 1, pageSize, search.get());
        }


        model.addAttribute("jobPage", jobPage);
        model.addAttribute("search", search.orElse(""));

        int totalPages = jobPage.getTotalPages();
        if (totalPages > 0) {
            model.addAttribute("pageNumbers", IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList()));
        }
        return "jobs/list_job";
    }

    /**
     * Show add job form
     * @param model
     * @return
     */
    @GetMapping("/show-add-form")
    public ModelAndView showAddForm(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("job", new Job());
        modelAndView.setViewName("jobs/add_job");
        return modelAndView;
    }

    /**
     * Add new job
     * @param job
     * @param result
     * @param model
     * @return
     */
    @PostMapping("/add")
    public String addJob(@ModelAttribute("job") Job job, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "jobs/add_job";
        }
        jobRepository.save(job);
        return "redirect:/jobs";
    }

    /**
     * show edit form
     * @param id
     * @return
     */
    @GetMapping("/show-edit-form/{id}")
    public ModelAndView showEditForm(@PathVariable("id") long id) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<Job> job = jobRepository.findById(id);
        if (job.isPresent()) {
            modelAndView.addObject("job", job.get());
            modelAndView.setViewName("jobs/update_job");
        } else {
            modelAndView.setViewName("redirect:/jobs?error=jobNotFound");
        }
        return modelAndView;
    }

    /***
     * Update job
     * @param job
     * @param result
     * @param model
     * @return
     */
    @PostMapping("/update")
    public String updateJob(@ModelAttribute("job") Job job, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "jobs/update_job";
        }
        jobRepository.save(job);
        return "redirect:/jobs?success=updateSuccess";
    }

    /**
     * Delete job
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public String deleteJob(@PathVariable("id") long id) {
        jobRepository.deleteById(id);
        return "redirect:/jobs?success=deleteSuccess";
    }

    /**
     * Show view job form
     * @param id
     * @return
     */
    @GetMapping("/view/{id}")
    public ModelAndView viewCompany(@PathVariable("id") long id) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<Company> company = companyRepository.findById(id);
        if (company.isPresent()) {
            modelAndView.addObject("company", company.get());
            modelAndView.setViewName("jobs/view_company");
        } else {
            modelAndView.setViewName("redirect:/jobs?error=companyNotFound");
        }
        return modelAndView;
    }

    /**
     * Show view candidate form
     * @param jobId
     * @return
     */
    @GetMapping("/view_candidatebyskill/{id}")
    public ModelAndView viewCandidateBySkill(@PathVariable("id") long jobId) {
        ModelAndView mav = new ModelAndView("jobs/view_candidate");
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        mav.addObject("job", job);
        mav.addObject("listCandidate", candidateServices.findCandidatesForJob(jobId));
        return mav;
    }

    /**
     * Send email to candidate
     * @param jobId
     * @param candidateId
     * @return
     */
    @GetMapping("/{jobId}/{candidateId}/send-email")
    public String sendEmail(@PathVariable("jobId") Long jobId, @PathVariable("candidateId") Long candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        String subject = "Job Application for " + candidate.getFullName();
        String body = "Hello " + candidate.getFullName() + ",\n\nWe are pleased to inform you about the job opportunity at our company. Please visit our website for more details.";

        sendEmail(candidate.getEmail(), subject, body);

        return "redirect:/jobs?success=applySuccess";
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("thanhnghiem199@gmail.com");
        mailSender.send(message);
    }


    @GetMapping("/apply/{id}")
    public String applyJob(@PathVariable("id") long id) {
        return "redirect:/jobs?success=applySuccess";
    }
}