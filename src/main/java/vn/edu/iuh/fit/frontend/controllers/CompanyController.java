package vn.edu.iuh.fit.frontend.controllers;

import com.neovisionaries.i18n.CountryCode;
import vn.edu.iuh.fit.backend.models.Address;
import vn.edu.iuh.fit.backend.models.Candidate;
import vn.edu.iuh.fit.backend.models.Company;
import vn.edu.iuh.fit.backend.repositories.AddressRepository;
import vn.edu.iuh.fit.backend.repositories.CompanyRepository;
import vn.edu.iuh.fit.backend.services.CompanyServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyServices companyServices;

    @Autowired
    private AddressRepository addressRepository;

    /**
     * Show list of companies no paging
     * @param model
     * @return link to view of list of companies no paging
     */
    @GetMapping("/list_company")
    public String showCompanyList(Model model) {
        model.addAttribute("companies", companyRepository.findAll());
        return "companies/list_no_paging_company";
    }

    /**
     * Show list of companies with paging
     * @param model
     * @param page
     * @param size
     * @return link to view of list of companies
     */
    @GetMapping("")
    public String showCompanyListPaging(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        Page<Company> companyPage = companyServices.findAll(currentPage - 1, pageSize, "id", "asc");

        model.addAttribute("companyPage", companyPage); // add attribute "companyPage" to model

        int totalPages = companyPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "companies/list_company";
    }

    /**
     * Show add company form
     * @param model
     * @return model and view
     */
    @GetMapping("/show-add-form")
    public ModelAndView add(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        Company company = new Company();
        company.setAddress(new Address());
        modelAndView.addObject("company", company);
        modelAndView.addObject("countries", CountryCode.values());
        modelAndView.setViewName("companies/add_company");
        return modelAndView;
    }

    /**
     * Add new company
     * @param company
     * @param result
     * @param model
     * @return link to view of list of companies
     */
    @PostMapping("/add")
    public String addCompany(@ModelAttribute("company") Company company,
                             BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("countries", CountryCode.values());
            return "companies/add_company";
        }

        addressRepository.save(company.getAddress());
        companyRepository.save(company);

        return "redirect:/companies";
    }

    /**
     * Show edit company form
     * @param id
     * @return model and view
     */
    @GetMapping("/show-edit-form/{id}")
    public ModelAndView edit(@PathVariable("id") long id) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<Company> opt = companyRepository.findById(id);
        if (opt.isPresent()) {
            Company company = opt.get();
            modelAndView.addObject("company", company);
            modelAndView.addObject("countries", CountryCode.values());
            modelAndView.setViewName("companies/update");
        } else {
            modelAndView.setViewName("redirect:/companies?error=companyNotFound");
        }
        return modelAndView;
    }

    /**
     * Update company
     * @param company
     * @param result
     * @param model
     * @return link to view of list of companies
     */
    @PostMapping("/update")
    public String update(@ModelAttribute("company") Company company,
                         BindingResult result, Model model) {

        if (company.getAddress() == null || company.getAddress().getCountry() == null) {
            model.addAttribute("error", "Country is required.");
            model.addAttribute("company", company);
            model.addAttribute("countries", CountryCode.values());
            return "companies/update";
        }

        Address address = company.getAddress();
        addressRepository.save(address);
        company.setAddress(address);
        companyRepository.save(company);

        return "redirect:/companies?success=updateSuccess";
    }


}
