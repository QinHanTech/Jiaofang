package markettracker.data;

import java.util.ArrayList;
import java.util.List;

public class Panal {

	private String caption;
	private String type;
	private String id;
	private boolean showCaption = true;
	
	private int panalType=0;
	
	private String reValue="";
	private String reDatakey="";

	public List<UIItem> itemList;

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public boolean isShowCaption() {
		return showCaption;
	}

	public void setShowCaption(boolean showCaption) {
		this.showCaption = showCaption;
	}

	public List<UIItem> getItemList() {
		
		return itemList;
	}

	public void setItemList(List<UIItem> itemList) {
		this.itemList = itemList;
	}

	public void setItem(UIItem item) {
		if (itemList == null)
			itemList = new ArrayList<UIItem>();
		this.itemList.add(item);
	}

	public UIItem getItem(int index) {
		if (index >= 0 && itemList != null && index < itemList.size())
			return itemList.get(index);
		return null;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPanalType() {
		return panalType;
	}

	public void setPanalType(int panalType) {
		this.panalType = panalType;
	}

	
			/**
			* 返回
			reValue 的值
			*@return reValue
			*/  
	
		
	public String getReValue()
	{
		return reValue;
	}

	
			/**
			* 设置
			reValue 的值
			* @param reValue
			*/
		
	public void setReValue(String reValue)
	{
		this.reValue = reValue;
	}

			
					/**
					* 返回
					reDatakey 的值
					*@return reDatakey
					*/  
			
				
			public String getReDatakey()
			{
				return reDatakey;
			}

			
					/**
					* 设置
					reDatakey 的值
					* @param reDatakey
					*/
				
			public void setReDatakey(String reDatakey)
			{
				this.reDatakey = reDatakey;
			}

}
