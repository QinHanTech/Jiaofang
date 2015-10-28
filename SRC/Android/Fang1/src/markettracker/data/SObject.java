package markettracker.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import markettracker.util.Constants.TableInfo;

public class SObject
{
	private static final String TERMINAL = "clientId";
	private static final String CALLDATE = "ReportDate";
	private static final String TEMPLATEID = "templateid";
	
	private String type;
	private String id;
	private Fields fields;
	private FieldsList detailfields;
	private FieldsList attfields;
	private Template template;
	
	private boolean saved = false;
	
	public SObject(Template temp)
	{
		super();
		setTemplate(temp);
		setTemplateId(temp.getType());
		
		// if(temp.getType().equals("-100"))
		// addDetaildata();
		
		// setType(temp.getType());
	}
	
	// public void addDetaildata() {
	// if (detailfields == null)
	// detailfields = new FieldsList();
	// Fields data = null;
	// for (Panal panal : template.getPanalList()) {
	// data = new Fields();
	// data.put("str1", template.getVersion());
	// data.put("str2", panal.getId());
	// // data.put("str1", template.getVersion());
	// this.detailfields.setFields(data);
	// }
	// }
	
	public SObject()
	{
		super();
	}
	
	public String getType()
	{
		return type;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public Fields getFields()
	{
		return fields;
	}
	
	public void setFields(Fields fields)
	{
		this.fields = fields;
	}
	
	public String getField(String fieldName)
	{
		if (fields != null)
			return this.fields.getStrValue(fieldName);
		return "";
	}
	
	public void setField(String fieldName, String fieldValue)
	{
		if (fields == null)
			fields = new Fields();
		this.fields.put(fieldName, fieldValue);
	}
	
	public void set(String fieldName, String fieldValue)
	{
		if (fields == null)
			fields = new Fields();
		this.fields.Set(fieldName, fieldValue);
	}
	
	public String getSValue(String fieldName)
	{
		return this.fields.getSValue(fieldName);
	}
	
	public int getKeyId()
	{
		if (fields != null)
			return this.fields.getIntValue(TableInfo.TABLE_KEY);
		return -1;
	}
	
	public void setKeyId(String fieldValue)
	{
		if (fields == null)
			fields = new Fields();
		this.fields.put(TableInfo.TABLE_KEY, fieldValue);
	}
	
	public String getTemplateId()
	{
		if (fields != null)
			return this.fields.getStrValue(TEMPLATEID);
		return "";
	}
	
	public void setTemplateId(String fieldValue)
	{
		if (fields == null)
			fields = new Fields();
		this.fields.put(TEMPLATEID, fieldValue);
	}
	
	public String getCallDate()
	{
		if (fields != null)
			return this.fields.getStrValue(CALLDATE);
		return "";
	}
	
	public void setCallDate(String fieldValue)
	{
		if (fields == null)
			fields = new Fields();
		this.fields.put(CALLDATE, fieldValue);
	}
	
	public String getTerminalCode()
	{
		if (fields != null)
			return this.fields.getStrValue(TERMINAL);
		return "";
	}
	
	public void setTerminalCode(String fieldValue)
	{
		if (fields == null)
			fields = new Fields();
		this.fields.put(TERMINAL, fieldValue);
	}
	
	public Iterator<Fields> iteratorDetail()
	{
		return detailfields.iterator();
	}
	
	public Iterator<Fields> iteratorAtt()
	{
		
		return attfields.iterator();
	}
	
	public FieldsList getDetailfields()
	{
		return detailfields;
	}
	
	public void setDetailfields(FieldsList detailfields)
	{
		this.detailfields = detailfields;
	}
	
	public Fields getDetailfield(int index)
	{
		if (index >= 0 && attfields != null && index < attfields.size())
			return this.detailfields.getFields(index);
		return null;
	}
	
	public void setDetailfield(Fields value)
	{
		if (detailfields == null)
			detailfields = new FieldsList();
		this.detailfields.setFields(value);
	}
	
	public FieldsList getAttfields()
	{
		return attfields;
	}
	
	public int getAttCount()
	{
		if (attfields == null)
			return 0;
		return attfields.size();
	}
	
	public int getDetailCount()
	{
		if (detailfields == null)
			return 0;
		return detailfields.size();
	}
	
	public void setAttfields(FieldsList attfields)
	{
		this.attfields = attfields;
	}
	
	public void setAttfield(HashMap<Integer, Fields> photo)
	{
		if (attfields == null)
			attfields = new FieldsList();
		this.attfields.removeAllFields();
		Set<Integer> keyList = photo.keySet();
		Iterator<Integer> key = keyList.iterator();
		
		Fields data;
		while (key.hasNext())
		{
			data = photo.get(key.next());
			if (data != null && !data.getStrValue("shotTime").equals(""))
				this.attfields.setFields(data);
		}
		// if (index >= 0 && attfields != null && index < attfields.size())
		// this.attfields.removeFields(index);
	}
	
	public void setAttfield1(HashMap<Integer, Fields> photo)
	{
		if (attfields == null)
			attfields = new FieldsList();
		this.attfields.removeAllFields();
		Set<Integer> keyList = photo.keySet();
		Iterator<Integer> key = keyList.iterator();
		
		Fields data;
		while (key.hasNext())
		{
			data = photo.get(key.next());
			if (data != null && !data.getStrValue("shotTime").equals(""))
				this.attfields.setFields(data);
		}
		// if (index >= 0 && attfields != null && index < attfields.size())
		// this.attfields.removeFields(index);
	}
	
	public Fields getAttfield(int index)
	{
		if (index >= 0 && attfields != null && index < attfields.size())
			return this.attfields.getFields(index);
		return null;
	}
	
	public void setAttfield(Fields value)
	{
		if (attfields == null)
			attfields = new FieldsList();
		this.attfields.setFields(value);
	}
	
	public void setAttfield(int index, Fields value)
	{
		if (attfields == null)
			attfields = new FieldsList();
		this.attfields.setFields(index, value);
	}
	
	public void removeAttfield(int index)
	{
		if (index >= 0 && attfields != null && index < attfields.size())
			this.attfields.removeFields(index);
	}
	
	public boolean isHavePhoto(int index)
	{
		if (this.attfields == null || this.attfields.size() <= index)
			return false;
		else
			return true;
	}
	
	public Template getTemplate()
	{
		return template;
	}
	
	public void setTemplate(Template template)
	{
		setType(template.getType());
		this.template = template;
	}
	
	public void resetAttfield(Fields value)
	{
		attfields = new FieldsList();
		this.attfields.setFields(value);
	}
	
	public String checkData()
	{
		if (template.getPanalList() != null)
		{
			for (Panal panal : template.getPanalList())
			{
				if (panal.getType().equals("panel"))
				{
					for (UIItem item : panal.getItemList())
					{
						if (item.isMustInput())
						{
							if (fields.getStrValue(item.getDataKey()).equals(""))
								return item.getCaption() + "未填写！";
						}
					}
				}
			}
		}
		if (template.isPhoto() && this.getAttCount() <= 0)
			return "照片未拍摄";
		
		if (fields.getDValue("str3") > fields.getDValue("cost"))
			return "费用不能超过" + fields.getDValue("cost");
		
		return "";
	}
	
	/**
	 * 返回 saved 的值
	 * 
	 * @return saved
	 */
	
	public boolean isSaved()
	{
		return saved;
	}
	
	/**
	 * 设置 saved 的值
	 * 
	 * @param saved
	 */
	
	public void setSaved(boolean saved)
	{
		this.saved = saved;
	}
	
}
