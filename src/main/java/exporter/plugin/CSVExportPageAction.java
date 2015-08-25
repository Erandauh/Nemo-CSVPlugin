package exporter.plugin;

import com.pearson.nemo.api.extension.PageAction;

import ro.fortsoft.pf4j.Extension;

/**
 * @author jayangani
 *
 */
@Extension
public class CSVExportPageAction implements PageAction {

    @Override
    public String getDisplayName() {
        return "CSV Export";
    }

    @Override
    public String getIcon() {
        return "icon-file";
    }

    @Override
    public String getLink() {
        return "/plugins/csvExport";
    }

    @Override
    public String getMethod() {
        return "POST";
    }
}
