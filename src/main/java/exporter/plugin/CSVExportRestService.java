/**
 * 
 */
package exporter.plugin;

import java.util.Map;
import com.pearson.nemo.api.activity.ActivityDefaultController;
import com.pearson.nemo.api.http.HttpStatus;
import com.pearson.nemo.api.http.Request;
import com.pearson.nemo.api.http.Response;
import com.pearson.workbench.services.ServiceProvider;

import exporter.plugin.service.CSVExportService;
import ro.fortsoft.pf4j.Extension;

/**
 * @author jayangani
 *
 */
@Extension
public class CSVExportRestService extends ActivityDefaultController {

	private ServiceProvider serviceProvider;
	private static final String FILE_NAME = "test.csv";

	@Override
	public String getUrl() {
		return "csvExport";
	}

	public void setServiceProvider(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	@Override
	public void post(Request req, Response res) {

		Map<String, String[]> formData = req.getBodyAsFormData();
		String ids[] = formData.get("ids");
		String errorMessage = null;
		String result = null;

		if (ids == null || ids.length == 0 || ids[0] == null || ids[0].trim().length() == 0) {
			res.setStatus(HttpStatus.NO_CONTENT);
			return;
		}

		try {
			CSVExportService service = new CSVExportService(serviceProvider);
			result = service.buildCSVFileContent(ids);
			if (result == null) {
				res.setStatus(HttpStatus.NO_CONTENT);
				return;
			} else {
				String filename = FILE_NAME;
				res.setContentType("text/" + "csv" + "; name=\"" + filename + "\"");
				res.setHeader("Content-disposition", "attachment; filename=" + filename);
				res.setStatus(HttpStatus.OK);
				res.setStatusMessage(result);
			}

		} catch (Exception e) {
			errorMessage = "Error while creating the CSV File :";
			errorMessage += e.getMessage();
			System.out.println(errorMessage);
			e.printStackTrace();
			res.setStatus(HttpStatus.BAD_REQUEST);
			res.setStatusMessage(errorMessage);
		}

	}
}
