package pkkwu.Calendar.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping("/ics")
	public String testController(@RequestParam(value = "month") String month,@RequestParam(value = "year") String year) {
		return "Typed month: " + month + " year: " + year;
	}

}
