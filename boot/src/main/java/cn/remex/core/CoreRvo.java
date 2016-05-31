package cn.remex.core;


public class CoreRvo implements Rvo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1531284911927498753L;
	private String msg;
	private boolean status;
	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String getMsg() {
		// TODO Auto-generated method stub
		return msg;
	}

	@Override
	public boolean getStatus() {
		// TODO Auto-generated method stub
		return status;
	}

	public void setMsg(final String msg) {
		this.msg = msg;
	}

}
