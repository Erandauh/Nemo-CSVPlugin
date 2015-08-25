/**
 * 
 */
package exporter.plugin.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.pearson.nemo.api.activity.Activity;
import com.pearson.nemo.api.activity.ActivityDao;
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
		ActivityDao ActivityDao = serviceProvider.getActivityDao();
		String result = null;
		sb.append("Nemo GUID,");
		sb.append("EPS GUID,");
		sb.append("URI,");
		sb.append("Name/Title");
		sb.append(NEW_LINE_SEPARATOR);

		ArrayList<String> idList = new ArrayList(Arrays.asList(ids[0].split(",")));

		for (String id : getAllItemIds(idList)) {
			Activity activity = ActivityDao.get(id);
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

	public ArrayList<String> getAllItemIds(ArrayList<String> idList) {

		ArrayList<String> allIdList = new ArrayList<>();
		ActivityDao ActivityDao = serviceProvider.getActivityDao();
		for (int i = 0; i < idList.size(); i++) {
			String id = idList.get(i); // item ID
			allIdList.add(id);
			int startPoint = allIdList.size() - 1;
			for (int x = startPoint; x < allIdList.size(); x++) {
				String idDes = allIdList.get(x);
				Activity item = ActivityDao.get(idDes);
				List<String> itemChildrenGuids = item.getChildren();
				for (String itemChildGuid : itemChildrenGuids) {
					Activity itemChild = ActivityDao.getByGuid(itemChildGuid);
					allIdList.add(itemChild.getId());
				}
			}

		}
		return allIdList;
	}

}
