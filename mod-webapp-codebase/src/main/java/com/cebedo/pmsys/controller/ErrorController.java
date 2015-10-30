package com.cebedo.pmsys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cebedo.pmsys.constants.ConstantsSystem;
import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.constants.RegistryURL;
import com.cebedo.pmsys.factory.AlertBoxFactory;

@Controller
@RequestMapping("fix")
public class ErrorController {

    /**
     * Everything else.
     * 
     * @param redirectAttrs
     * @return
     */
    @RequestMapping(value = { "", "/" })
    public String error(RedirectAttributes redirectAttrs) {
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, AlertBoxFactory.ERROR);
	return RegistryURL.REDIRECT_DASHBOARD;
    }

    /**
     * Internal server error.
     * 
     * @param redirectAttrs
     * @return
     */
    @RequestMapping(value = { "", "/500" })
    public String error500(RedirectAttributes redirectAttrs) {
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, AlertBoxFactory.ERROR);
	return RegistryURL.REDIRECT_DASHBOARD;
    }

    /**
     * POST not supported.
     * 
     * @param redirectAttrs
     * @return
     */
    @RequestMapping(value = { "", "/405" })
    public String error405(RedirectAttributes redirectAttrs) {
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, AlertBoxFactory.ERROR);
	return RegistryURL.REDIRECT_DASHBOARD;
    }

    /**
     * Resource not found.
     * 
     * @param redirectAttrs
     * @return
     */
    @RequestMapping(value = { "", "/404" })
    public String error404(RedirectAttributes redirectAttrs) {
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, AlertBoxFactory.ERROR);
	return RegistryURL.REDIRECT_DASHBOARD;
    }

    /**
     * Form is syntactically incorrect.
     * 
     * @param redirectAttrs
     * @return
     */
    @RequestMapping(value = { "", "/400" })
    public String error400(RedirectAttributes redirectAttrs) {
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT,
		AlertBoxFactory.FAILED.generateHTML(RegistryResponseMessage.ERROR_COMMON_400));
	return RegistryURL.REDIRECT_DASHBOARD;
    }

}
