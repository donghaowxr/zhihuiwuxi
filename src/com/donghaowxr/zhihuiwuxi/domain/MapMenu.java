package com.donghaowxr.zhihuiwuxi.domain;

import java.util.ArrayList;

public class MapMenu {
	public String retcode;
	public ArrayList<MapData> data;

	public class MapData {
		public int id;
		public String title;
		public int type;
		public String url;
	}
}
