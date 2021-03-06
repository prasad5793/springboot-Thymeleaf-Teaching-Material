package lk.lalithk90.springbootthymelaf.employee.controller;

import lk.lalithk90.springbootthymelaf.employee.entity.Employee;
import lk.lalithk90.springbootthymelaf.employee.entity.Enum.CivilStatus;
import lk.lalithk90.springbootthymelaf.employee.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping( "/employee" )
public class EmployeeController {
    private final EmployeeService employeeService;
    private final DesignationService designationService;
    private final GenderService genderService;
    private final HobbiesService hobbiesService;
    private final WeekDayService weekDayService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, DesignationService designationService,
                              GenderService genderService, HobbiesService hobbiesService,
                              WeekDayService weekDayService) {
        this.employeeService = employeeService;
        this.designationService = designationService;
        this.genderService = genderService;
        this.hobbiesService = hobbiesService;
        this.weekDayService = weekDayService;
    }

    // common things send ro frontend {code reusability}
    private String commonCode(Model model) {
        model.addAttribute("genders", genderService.findAll());
        model.addAttribute("designations", designationService.findAll());
        model.addAttribute("civilStatuses", CivilStatus.values());
        model.addAttribute("hobbies", hobbiesService.findAll());
        model.addAttribute("weekDays", weekDayService.findAll());
        //send available employee List
        model.addAttribute("employeeList", employeeService.findAll());
        return "/employee/employee.html";
    }

    //give employee add from
    @GetMapping
    private String giveEmployeeAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("addStatus", true);
        return commonCode(model);

    }

    //save and update employee
    @PostMapping( value = {"/add", "/update"} )
    private String persist(@Valid @ModelAttribute( "employee" ) Employee employee, BindingResult result, Model model,
                           RedirectAttributes redirectAttributes) {
        /*        @Valid
        ====================================
         * Constraints defined on the object and its properties are be validated when the
         * property, method parameter or method return type is validated.
         */
        /*        @ModelAttribute
        ====================================
         *Annotation that binds a method parameter or method return value
         * to a named model attribute, exposed to a web view.Supported
         * for controller classes with methods.
         */
        /*        BindingResult
        ====================================
         * General interface that represents binding results. Extends the for error registration capabilities,
         * allowing for a validate to be applied, and adds  binding-specific analysis and model building.
         */
        /*        Model
        ===================================
         * Java-5-specific interface that defines a holder for model attributes.
         * Primarily designed for adding attributes to the model.
         * Allows for accessing the overall model as a java.util.Map.
         */
        if ( result.hasErrors() ) {
            redirectAttributes.addFlashAttribute("employee", employee);
            model.addAttribute("addStatus", true);
            return commonCode(model);
        }


        System.out.println("im here" + employee.toString());
        employeeService.persist(employee);
        return "redirect:/employee";
    }

    //give one employee to edit details according to employee id
    @GetMapping( "/edit/{id}" )
    public String getEmployee(@PathVariable( "id" ) Integer id, Model model) {
        model.addAttribute("employee", employeeService.findById(id));
        model.addAttribute("addStatus", false);
        return commonCode(model);
    }

    //according to given id employee delete from DB
    @GetMapping( "/delete/{id}" )
    public String deleteEmployee(@PathVariable( "id" ) Integer id) {
        employeeService.delete(id);
        return "redirect:/employee";
    }

}
