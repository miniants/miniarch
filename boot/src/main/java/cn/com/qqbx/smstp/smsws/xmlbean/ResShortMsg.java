package cn.com.qqbx.smstp.smsws.xmlbean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class ResShortMsg {
		private String errorCode;
		@XStreamAlias("ShortMsgError")
		private ResShortMsgError shortMsgError;
		public ResShortMsgError getShortMsgError() {
			return shortMsgError;
		}
		public void setShortMsgError(ResShortMsgError shortMsgError) {
			this.shortMsgError = shortMsgError;
		}
		public String getErrorCode() {
			return errorCode;
		}
		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}
		
		

}
