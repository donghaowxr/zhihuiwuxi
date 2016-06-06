package com.donghaowxr.zhihuiwuxi.domain;

import java.util.ArrayList;

public class NewsMenu {
	public int retcode;
	public ArrayList<Integer>extend;
	public ArrayList<DataArray> data;
	
	public class DataArray{
		public int id;
		public  String title;
		public int type;
		public ArrayList<ChildrenArray> children;
		
		public class ChildrenArray{
			public int id;
			public String title;
			public int type;
			public String url;
		}
	}
}
