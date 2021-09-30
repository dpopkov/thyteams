package learn.thyme.thyteams.infrastructure.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Value("${application.version}")
    private String applicationVersion;

    @ModelAttribute("version")
    public String getVersion() {
        return applicationVersion;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({DataIntegrityViolationException.class,
            ObjectOptimisticLockingFailureException.class})
    public ModelAndView handleConflicts(HttpServletRequest request, Exception e) {
        ModelAndView result = new ModelAndView("error/409");
        // Add the request URL as url in the model so the error page can use this.
        result.addObject("url", request.getRequestURL());
        return result;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor trimmerEditor = new StringTrimmerEditor(false);
        binder.registerCustomEditor(String.class, trimmerEditor);
    }
}
