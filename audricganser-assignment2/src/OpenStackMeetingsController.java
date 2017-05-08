import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OpenStackMeetingsController {

	private OpenStackService openStackMeetingsParamImpl;

	public OpenStackMeetingsController() {
		
	}
	
	public OpenStackMeetingsController(OpenStackService openStackMeetingsParamImpl) {
		this.openStackMeetingsParamImpl = openStackMeetingsParamImpl;
	}
	
	@ResponseBody
    @RequestMapping(value = "/")
    public String helloWorld()
    {
        return "Enter query parameter to begin.";
    }
	
	@ResponseBody
	@RequestMapping(value = "/openstackmeetings")
	public String getComposedEmail() {
		return "Welcome to OpenStack meetings statistics calculation page. Please provide project and year as query parameters";
	}
	
	@ResponseBody
    @RequestMapping(value = "/openstackmeetings", params = {"project"}, method=RequestMethod.GET)
    public String getProj(@RequestParam("project") String project)
    {

		return "Required paramter < year > is missing";
    }
	
	@ResponseBody
    @RequestMapping(value = "/openstackmeetings", params = {"year"}, method=RequestMethod.GET)
    public String getYear(@RequestParam("year") String year)
    {
		return "Required paramter < project > is missing";
    }
	
	@ResponseBody
    @RequestMapping(value = "/openstackmeetings", params = {"project", "year"}, method=RequestMethod.GET)
    public String getParameters(@RequestParam("project") String project, @RequestParam("year") String year)
    {
		return openStackMeetingsParamImpl.getMeetings(project, year);
		
    }
	
}
