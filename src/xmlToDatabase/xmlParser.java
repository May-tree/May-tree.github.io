package xmlToDatabase;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class xmlParser {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder=factory.newDocumentBuilder();
		Document doc=builder.parse("cora-all-id.xml");
		doc.normalize();
		NodeList publications =doc.getElementsByTagName("publication");
		for (int i=0;i<publications.getLength();i++){
			Element publication=(Element) publications.item(i);
			System.out.print("Content: ");
			System.out.println(publication.getElementsByTagName("text").item(0).getFirstChild().getNodeValue());
			System.out.print("URL: ");
			System.out.println(publication.getElementsByTagName("url").item(0).getFirstChild().getNodeValue());
			System.out.print("Author: ");
			System.out.println(publication.getElementsByTagName("author").item(0).getFirstChild().getNodeValue());
			System.out.print("Date: ");
			Element linkdate=(Element) publication.getElementsByTagName("date").item(0);
			String day=linkdate.getElementsByTagName("day").item(0).getFirstChild().getNodeValue();
			String month=linkdate.getElementsByTagName("month").item(0).getFirstChild().getNodeValue();
			String year=linkdate.getElementsByTagName("year").item(0).getFirstChild().getNodeValue();
			System.out.println(day+"-"+month+"-"+year);
			System.out.print("Description: ");
			System.out.println(publication.getElementsByTagName("description").item(0).getFirstChild().getNodeValue());
			System.out.println();
		}
	}
	

}
