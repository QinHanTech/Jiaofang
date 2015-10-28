package markettracker.data;

import java.util.*;


public class Louceng {

	private String type;
	private String version;
	private String name;

	private List<Fields> fieldsList;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Fields> getFieldsList() {
		return fieldsList;
	}

	public void setFieldsList(List<Fields> fieldsList) {
		this.fieldsList = fieldsList;
	}

	public void addFields(Fields fields) {
		if (this.fieldsList == null)
			this.fieldsList = new ArrayList<Fields>();
		this.fieldsList.add(fields);
	}

	public Fields getFields(int index) {
		return fieldsList.get(index);
	}

	public int size() {
		return fieldsList==null?0:fieldsList.size();
	}
	
}
