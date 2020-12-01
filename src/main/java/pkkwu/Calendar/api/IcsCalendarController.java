package pkkwu.Calendar.api;

import biweekly.ICalendar;
import biweekly.component.VEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IcsCalendarController {

	public static final String HOSTURL = "http://www.weeia.p.lodz.pl/pliki_strony_kontroler/kalendarz.php?/kalendarz.php?";

	@GetMapping("/ics")
	public String testController(@RequestParam(value = "month") String month,
		@RequestParam(value = "year") String year) {
		String UrlToCheck = buildUrlByMonthAndDate(month,year);
		createIcsCalendar();
		return parseHtmlInGsoup(UrlToCheck);
	}

	private void createIcsCalendar() {
		ICalendar calendar = new ICalendar();
		calendar.setExperimentalProperty("X-WR-CALNAME", "Wydarzenia WEEIA");

		System.out.println(calendar.toString());
	}

	String parseHtmlInGsoup(String urlToCheck){
		Document doc = null;
		try {
			doc = Jsoup.connect(urlToCheck).get();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		getEvents(doc);
		return doc.toString();
	}

	private void getEvents(Document doc) {
		Elements elementsWithEventInformation = extractDivs(doc);
		for (Element element : elementsWithEventInformation) {
			String day = element.html().toString();
			System.out.println(day);

		}
//		int day = elementsWithEventInformation.get(0);

	}

	private Elements extractDivs(Document doc) {
		Elements activeElements = doc.getElementsByClass("active");
		Elements elementsWithTdTag = new Elements();
		for (Element activeElement : activeElements) {
			if(activeElement.tag().toString().equals("td")){
				elementsWithTdTag.add(activeElement);
			}
		}
		return elementsWithTdTag;
	}

	private String buildUrlByMonthAndDate(String month, String year) {
		return HOSTURL + "rok=" + year + "&miesiac=" + month;
	}

}

