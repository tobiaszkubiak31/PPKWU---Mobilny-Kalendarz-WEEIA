package pkkwu.Calendar.api;

import java.io.IOException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
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
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
	}

	String parseHtmlInGsoup(String urlToCheck){
		Document doc = null;
		try {
			doc = Jsoup.connect(urlToCheck).get();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		getInformationAboutEvents(doc);
		return doc.toString();
	}

	private void getInformationAboutEvents(Document doc) {
		Elements elementsWithEventInformation = extractDivs(doc);

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

