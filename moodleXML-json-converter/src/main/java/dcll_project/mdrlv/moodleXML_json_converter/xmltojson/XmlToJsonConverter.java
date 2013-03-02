package dcll_project.mdrlv.moodleXML_json_converter.xmltojson;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.transform.TransformerException;

import dcll_project.mdrlv.moodleXML_json_converter.WebStandardConverter;

public class XmlToJsonConverter extends WebStandardConverter {

	@Override
	public boolean accordanceWithMoodleStandard(File f) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean accordanceWithStandard(File f) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int convert(String inputFileUri, String outputFileUri)
			throws IOException, URISyntaxException, TransformerException {
		// TODO Auto-generated method stub
		return 0;
	}

}
