package pkkwu.Calendar.api;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
		@RequestParam(value = "year") String year) throws IOException, ParseException {
		String UrlToCheck = buildUrlByMonthAndDate(month,year);
		System.out.println(UrlToCheck);
		ICalendar iCalendar = createIcsCalendar();
		ArrayList<Event> parsedEvents = (getEvents(HOSTURL + "rok=" + year + "&miesiac=" + month));
		for (Event parsedEvent : parsedEvents) {
			createEventInCalendar(parsedEvent,iCalendar,year,month);
		}
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

	Document parseHtmlInGsoup(String urlToCheck){
		Document doc = null;
		try {
			doc = Jsoup.connect(urlToCheck).get();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}

		return doc;
	}

	private ArrayList<Event> getEvents(String urlhtml) throws IOException {
		Document document = getDocumentFromUrl(urlhtml);
		Elements elementsWithEventInformation = document.select("td.active");
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

	private String buildUrlByMonthAndDate(String year, String month) {
		return HOSTURL + "rok=" + year + "&miesiac=" + month;
	}

	private void createEventInCalendar(Event parsedEvent, ICalendar iCalendar,
		String year, String month) throws ParseException {
		VEvent event = new VEvent();
		event.setSummary(parsedEvent.getEventInfo());
		Date start = new  SimpleDateFormat("yyyy-MM-dd").parse(year + "-" + month + "-" + parsedEvent.getDay());
		event.setDateStart(start);
		Date end = new  SimpleDateFormat("yyyy-MM-dd").parse(year + "-" + month + "-" + parsedEvent.getEventInfo());
		event.setDateEnd(end);
		iCalendar.addEvent(event);
	}

	private Document getDocumentFromUrl(String urlhtml) throws IOException {
		URL url = new URL(urlhtml);
		URLConnection connection = url.openConnection();
		StringBuilder fromWebsite = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;

		while ((line = reader.readLine()) != null) {
			fromWebsite.append(line);
		}
		String htmlContent = fromWebsite.toString();
		Document document = Jsoup.parse(htmlContent);
		return document;
	}

}

