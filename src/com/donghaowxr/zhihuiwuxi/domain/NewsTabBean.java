package com.donghaowxr.zhihuiwuxi.domain;

import java.util.ArrayList;

public class NewsTabBean {
	public int retcode;
	public TabData data;
	
	public class TabData{
		public String more;
		public ArrayList<TabNews> news;
		public ArrayList<TopNews> topnews;
		
		public class TabNews{
			public int id;
			public String listimage;
			public String pubdate;
			public String title;
			public String type;
			public String url;
		}
		
		public class TopNews{
			public int id;
			public String topimage;
			public String pubdate;
			public String title;
			public String type;
			public String url;
		}
	}
}
