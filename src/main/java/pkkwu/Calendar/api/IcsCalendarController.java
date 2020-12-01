package pkkwu.Calendar.api;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IcsCalendarController {

	public static final String HOSTURL = "http://www.weeia.p.lodz.pl/pliki_strony_kontroler/kalendarz.php?/kalendarz.php?";
	public static final String FILE_NAME = "calendar.ics";

	@GetMapping("/ics")
	public ResponseEntity testController(@RequestParam(value = "month") String month,
		@RequestParam(value = "year") String year) throws IOException {
		String UrlToCheck = buildUrlByMonthAndDate(month,year);
		ICalendar iCalendar = createIcsCalendar();
		parseHtmlInGsoup(UrlToCheck);
//		return parseHtmlInGsoup(UrlToCheck);
		File file = new File(FILE_NAME);
		Biweekly.write(iCalendar).go(file);
		Resource resource = new UrlResource(Paths.get(FILE_NAME).toUri());
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
			.body(resource);
	}

	private ICalendar createIcsCalendar() {
		ICalendar calendar = new ICalendar();
		calendar.setExperimentalProperty("X-WR-CALNAME", "Wydarzenia WEEIA");

		System.out.println(calendar.toString());
		return calendar;
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

	private ArrayList<Event> getEvents(Document doc) {
		Elements elementsWithEventInformation = extractDivs(doc);
		Elements elementsWithEvents = elementsWithEventInformation.select("div.InnerBox");
		Elements elementsWithDays = elementsWithEventInformation.select("a.active");
		ArrayList<Event> eventList = new ArrayList<>();
		for (int i = 0; i < elementsWithEvents.size(); i++) {
			eventList.add(new Event(Integer.parseInt(elementsWithDays.get(i).text()), elementsWithEventInformation.get(i).text()));
		}
		return eventList;
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

