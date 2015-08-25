/**
 * 
 */
package exporter.plugin.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.pearson.nemo.api.activity.Activity;
import com.pearson.workbench.model.EpsInstitution;
import com.pearson.workbench.services.ServiceProvider;

/**
 * @author jayangani
 *
 */
public class CSVExportService {
	private ServiceProvider serviceProvider;
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";

	public CSVExportService(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public String buildCSVFileContent(String ids[]) {
		StringBuilder sb = new StringBuilder();
		String result = null;
		sb.append("Nemo GUID,");
		sb.append("EPS GUID,");
		sb.append("URI,");
		sb.append("Name/Title");
		sb.append(NEW_LINE_SEPARATOR);

		ArrayList<String> idList = new ArrayList(Arrays.asList(ids[0].split(",")));
		ArrayList<String> idListDesendent = new ArrayList();

		for (int i = 0; i < idList.size(); i++) {
			String id = idList.get(i); // item ID
			idListDesendent.add(id);
			int startPoint = idListDesendent.size() - 1;
			for (int x = startPoint; x < idListDesendent.size(); x++) {
				String idDes = idListDesendent.get(x);
				Activity item = serviceProvider.getActivityDao().get(idDes);
				List<String> itemChildrenGuids = item.getChildren();
				for (String itemChildGuid : itemChildrenGuids) {
					Activity itemChild = serviceProvider.getActivityDao().getByGuid(itemChildGuid);
					idListDesendent.add(itemChild.getId());
				}
			}

		}

		for (String id : idListDesendent) {
			Activity activity = serviceProvider.getActivityDao().get(id);
			EpsInstitution epsInstitution = serviceProvider.getCurrentContext().getWorkspace().getInstitutions().get(0);

			sb.append(activity.getGuid().toString());
			sb.append(COMMA_DELIMITER);
			sb.append(epsInstitution.getId());
			sb.append(COMMA_DELIMITER);
			sb.append(epsInstitution.getUrl());
			sb.append(COMMA_DELIMITER);
			sb.append(activity.getName());
			sb.append(NEW_LINE_SEPARATOR);
		}
		result = sb.toString();
		System.out.println("CSV file was created successfully !!!");
		return result;

	}

}
