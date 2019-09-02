package com.x.sdk.constant;

/**
 * Description: 异常编码定义 <br>
 */
public final class ExceptCodeConstants {
	private ExceptCodeConstants() {
	}

	/**
	 * Description: 特定异常<br>
	 */
	public static final class Special {
		private Special() {
		}

		/**
		 * 处理成功
		 */
		public static final String SUCCESS = "000000";
		
		/**
		 * 其它业务异常
		 */
		public static final String BUSINESS_ERROR = "111111";

		/**
		 * 其他系统异常
		 */
		public static final String SYSTEM_ERROR = "999999";

		/**
		 * 传入的参数为空
		 */
		public static final String PARAM_IS_NULL = "888888";

		/**
		 * 未查询到记录[查询无记录]
		 */
		public static final String NO_RESULT = "000001";

		/**
		 * 参数类型不正确
		 */
		public static final String PARAM_TYPE_NOT_RIGHT = "000002";

		/**
		 * 未配置系统参数或未刷新缓存
		 */
		public static final String NO_DATA_OR_CACAE_ERROR = "000003";
	}

}
