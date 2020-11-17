package pkkwu.Calendar.api;

import java.io.IOException;
import java.util.Date;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;
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
		System.out.println(calendar.toString());

		java.util.Calendar calendarEvent = java.util.Calendar.getInstance();
		calendarEvent.set(java.util.Calendar.MONTH, java.util.Calendar.DECEMBER);
		calendarEvent.set(java.util.Calendar.DAY_OF_MONTH, 25);

		VEvent christmas = new VEvent(new Date(calendarEvent.getTime()), "Christmas Day");

		UidGenerator ug = new UidGenerator("1");
		christmas.getProperties().add(ug.generateUid());

		net.fortuna.ical4j.model.Calendar cal = new net.fortuna.ical4j.model.Calendar();
		cal.getComponents().add(christmas);

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
		for (Element element : elementsWithEventInformation) {
			String day = element.html().toString();
			System.out.println(day);

		}
		int day = elementsWithEventInformation.get(0);

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

