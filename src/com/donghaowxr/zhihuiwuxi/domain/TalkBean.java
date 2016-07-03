package com.donghaowxr.zhihuiwuxi.domain;

import java.util.ArrayList;

public class TalkBean {
	public ArrayList<WsBean> ws;

	public class WsBean {
		public ArrayList<CwBean> cw;

		public class CwBean {
			public String w;
		}
	}
}
