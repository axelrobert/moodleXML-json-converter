package dcll_project.mdrlv.jsontoxml;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.transform.TransformerException;

import dcll_project.mdrlv.WebStandardConverter;

public class JsonToXmlConverter extends WebStandardConverter {

    @Override
    public final boolean accordanceWithMoodleStandard(final File file) {
    	return false;
    }

	@Override
	public final boolean accordanceWithStandard(final File file) {
		return false;
	}

	@Override
	public final int convert(final String inputFileUri,
			final String outputFileUri)
			throws IOException, URISyntaxException,
			TransformerException {
		return 0;
	}


}
