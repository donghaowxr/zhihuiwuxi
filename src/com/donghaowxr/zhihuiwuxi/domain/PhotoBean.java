package com.donghaowxr.zhihuiwuxi.domain;

import java.util.ArrayList;

public class PhotoBean {
	public int retcode;
	public PhotoData data;
	public class PhotoData{
		public String more;
		public ArrayList<PhotoNews> news;
		public class PhotoNews{
			public String title;
			public String listimage;
		}
	}
}
