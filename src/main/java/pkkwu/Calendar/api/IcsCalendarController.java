package pkkwu.Calendar.api;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IcsCalendarController {

	public static final String URL_TO_CALENDAR = "http://www.weeia.p.lodz.pl/pliki_strony_kontroler/kalendarz.php?rok=2020&miesiac=10&fbclid=IwAR0B-hAZKwveCi8CkjZ9SOUWxGLtgbL57bIE3DFxlmoVFnWx8PzTfmNRbtM";


	@GetMapping("/ics")
	public String testController(@RequestParam(value = "month") String month,
		@RequestParam(value = "year") String year) {

		return parseHtmlInGsoup();
	}

	String parseHtmlInGsoup(){
		Document doc = null;
		try {
			doc = Jsoup.connect(URL_TO_CALENDAR).get();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		doc.select("p").forEach(System.out::println);
		return doc.toString();
	}

}

