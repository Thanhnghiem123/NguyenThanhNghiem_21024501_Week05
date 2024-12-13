package vn.edu.iuh.fit.frontend.controllers;

import com.neovisionaries.i18n.CountryCode;
import vn.edu.iuh.fit.backend.models.Address;
import vn.edu.iuh.fit.backend.models.Candidate;
import vn.edu.iuh.fit.backend.repositories.AddressRepository;
import vn.edu.iuh.fit.backend.repositories.CandidateRepository;
import vn.edu.iuh.fit.backend.services.CandidateServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/candidates")
public class CandidateController {
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private CandidateServices candidateServices;
    @Autowired
    private AddressRepository addressRepository;

    /**
     * Show list of candiidates no paging
     * @param model
     * @return link to view of list of candidates no paging
     */
    @GetMapping("/list")
    public String showCandidateList(Model model) {
        model.addAttribute("candidates", candidateRepository.findAll()); //  add attribute "candidates" to model
        return "candidates/list_no_paging";
    }

    /**
     * Show list of candidates with paging
     * @param model
     * @param page
     * @param size
     * @return link to view of list of candidates
     */
    @GetMapping("")
    public String showCandidateListPaging(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<Candidate> candidatePage = candidateServices.findAll(currentPage - 1, pageSize, "id", "asc");
        model.addAttribute("candidatePage", candidatePage); // add attribute "candidatePage" to model

        int totalPages = candidatePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "candidates/list";
    }

    /**
     * display form to add new candidate
     * @param model
     * @return model and view
     */
    @GetMapping("/show-add-form")
    public ModelAndView add(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        Candidate candidate = new Candidate();
        candidate.setAddress(new Address());
        // add object candidate to model and view with name "candidate" to form can bind to it
        modelAndView.addObject("candidate", candidate);
        modelAndView.addObject("address", candidate.getAddress());
        modelAndView.addObject("countries", CountryCode.values());
        modelAndView.setViewName("candidates/add");
        return modelAndView;
    }

    /**
     * add new candidate
     * @param candidate
     * @param result
     * @param model
     * @return link to view of list of candidates
     */
    @PostMapping("/add")
    public String addCandidate( @ModelAttribute("candidate") Candidate candidate, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("countries", CountryCode.values()); // add attribute "countries" to model
            return "candidates/add";
        }

        addressRepository.save(candidate.getAddress());
        candidateRepository.save(candidate);

        return "redirect:/candidates";
    }

    /**
     * Form to edit candidate
     * @param id
     * @return
     */
    @GetMapping("/show-edit-form/{id}")
    public ModelAndView edit(@PathVariable("id") long id) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<Candidate> opt = candidateRepository.findById(id);
        if (opt.isPresent()) {
            Candidate candidate = opt.get();
            modelAndView.addObject("candidate", candidate);
            modelAndView.addObject("countries", CountryCode.values());

            modelAndView.setViewName("candidates/update");
        } else {
            modelAndView.setViewName("redirect:/candidates?error=candidateNotFound");
        }
        return modelAndView;
    }

    /**
     * Update candidate
     * @param candidate
     * @param result
     * @param model
     * @return
     */
    @PostMapping("/update")
    public String update(@ModelAttribute("candidate") Candidate candidate,BindingResult result, Model model) {

        if (candidate.getAddress() == null || candidate.getEmail() == null || candidate.getAddress().getCountry() == null) {
            model.addAttribute("error", "Country is required.");
            model.addAttribute("candidate", candidate);
            model.addAttribute("countries", CountryCode.values());
            return "candidates/update";
        }


        Address address = candidate.getAddress();
//        if (address.getId() == null) {
//            addressRepository.save(address);
//        } else {
            addressRepository.save(address);
//        }
        candidate.setAddress(address);
        candidateRepository.save(candidate);

        return "redirect:/candidates?success=updateSuccess";
    }


    /**
     * Suggest skill to candidate to learn
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/suggest-skill-to-learn/{id}")
    public String suggestSkillToLearn(@PathVariable Long id, Model model) {
        Candidate candidate = candidateRepository.findById(id).get();
        model.addAttribute("candidate", candidate);
        model.addAttribute("suggestedSkills", candidateServices.suggestSkillToLearn(id));
        return "candidates/suggestedskill";
    }


}