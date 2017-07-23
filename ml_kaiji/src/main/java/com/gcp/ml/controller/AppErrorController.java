package com.gcp.ml.controller;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.gcp.dataobject.DataFormat;


@Controller
public class AppErrorController implements ErrorController {

    /**
     * Error Attributes in the Application
     */
    private ErrorAttributes errorAttributes;

    @Value("${server.context-path}")
    String contextPath;
    
    private final static String ERROR_PATH = "/error";
    private final static String ERROR_API_PATH = "/error_api";
    
    private final static Logger logger = LoggerFactory.getLogger(AppErrorController.class);

    /**
     * Controller for the Error Controller
     * @param errorAttributes
     */
    public AppErrorController(ErrorAttributes errorAttributes) {
    	System.out.println("construct done!");
        this.errorAttributes = errorAttributes;
    }
    
    @RequestMapping(value = "/test")
    @ResponseBody
    public Object test(HttpServletRequest request)
    {
    	Map<String, Object> body = getErrorAttributes(request, getTraceParameter(request));
        HttpStatus status = getStatus(request);
        
        DataFormat df = new DataFormat();
   	 	df.setResult((int)body.get("status"), (String)body.get("message"));
        return df;
    }

    @RequestMapping(value = ERROR_API_PATH)
    @ResponseBody
    public Object errorAPI(HttpServletRequest request, HttpServletResponse response)
    {
    	 Map<String, Object> body = getErrorAttributes(request, getTraceParameter(request));
         
         DataFormat df = new DataFormat();
    	 	df.setResult((int)body.get("status"), (String)body.get("message"));
         return df;
    }
    
    /**
     * Supports the HTML Error View
     * @param request
     * @return
     * @throws ServletException 
     */
   
    @RequestMapping(value = ERROR_PATH)

    public String error(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Map<String, Object> body = getErrorAttributes(request, getTraceParameter(request));
        HttpStatus status = getStatus(request);
        
        
        
        Set<Entry<String, Object>> entryset =  body.entrySet();
        for(Entry<String, Object> entry : entryset)
        {
        	logger.info(entry.getKey() + " : " + entry.getValue());
        }
        
        
        if(body.get("path")== null)
        {
        	request.setAttribute("status", body.get("status").toString());
        	//response.setHeader("Content-Type", "text/html;charset=UTF-8");

        	return "redirect:"+"/web/index?error="+body.get("status").toString();
        	
        }
        if(body.get("path").toString().contains("api"))
        {
        	request.setAttribute("status", body.get("status").toString());

        	return "forward:"+ERROR_API_PATH;        	
        }
        request.setAttribute("status", body.get("status").toString());
        //response.setHeader("Content-Type", "text/html;charset=UTF-8");
        //response.setStatus(200);
    	return "redirect:"+"/web/index?error="+body.get("status").toString();
    }



    /**
     * Returns the path of the error page.
     *
     * @return the error path
     */
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }


    private boolean getTraceParameter(HttpServletRequest request) {
        String parameter = request.getParameter("trace");
        if (parameter == null) {
            return false;
        }
        return !"false".equals(parameter.toLowerCase());
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request,
                                                   boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return this.errorAttributes.getErrorAttributes(requestAttributes,
                includeStackTrace);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");
        if (statusCode != null) {
            try {
                return HttpStatus.valueOf(statusCode);
            }
            catch (Exception ex) {
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
