package com.donghaowxr.zhihuiwuxi.domain;

import java.util.ArrayList;

public class AnimBean {
	public int retcode;
	public AnimData data;

	public class AnimData {
		public String title;
		public String more;
		public ArrayList<AnimList> animlist;
		public ArrayList<TopAnim> topanim;

		public class AnimList {
			public int id;
			public String title;
			public String listimage;
			public String url;
			public ArrayList<DesBean> description;
		}

		public class DesBean {
			public String item;
		}

		public class TopAnim {
			public int id;
			public String title;
			public String topimage;
			public String url;
		}
	}
}
