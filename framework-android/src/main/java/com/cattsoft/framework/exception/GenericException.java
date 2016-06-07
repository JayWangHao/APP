package com.cattsoft.framework.exception;

public class GenericException
    extends java.lang.Exception { 
	
	private static final long serialVersionUID = 1L;
	/**
	   * 错误级别，分为应用错误级别和系统错误级别
	   */
	  public static int EXCEPTION_LEVEL_SYS = 1;
	  /**
	   * 错误级别，分为应用错误级别和系统错误级别
	   */
	  public static int EXCEPTION_LEVEL_APP = 2;
	  
	  /**
	   * 错误号
	   */

	  String errId;

	  /**
	   * 错误信息，当前错误的信息。
	   */
	  String errMsg;

	  /**
	   * 原错误信息。捕捉到原发错误时候，记录下原发错误的信息。
	   */
	  String errMsgOri;
	  /**
	   * 原发错误对象。
	   */
	  Exception errOri;
	  public GenericException() {
	  }
	  public GenericException(Exception e) {
	    super(e);
	  }
	  /**
	   *
	   * 错误信息记录日志。
	   * @param level int 错误级别。应用级别一般不记录日志，如果记录，级别定为error级别。系统错误要记录日志，级别为FATAL
	   */
	  public void writeLog(int level) {
	    
		//记录总体错误日志信息，包括错误号对应的简单信息。
	    if (level == EXCEPTION_LEVEL_SYS) {
	    }
	    else if (level == EXCEPTION_LEVEL_APP) {
	    }
	  }

	  public String getErrId() {
	    return errId;
	  }

	  public void setErrId(String errId) {
	    this.errId = errId;
	  }
	  public String getErrMsg() {
	    return errMsg;
	  }
	  
	  
	  public String getMessage() {
		  return errMsg;
	 }
	public String getErrMsgOri() {
	    return errMsgOri;
	  }

	  public Exception getErrOri() {
	    return errOri;
	  }

	  public void setErrMsg(String errMsg) {
	    this.errMsg = errMsg;
	  }
	  public void setErrMsgOri(String errMsgOri) {
	    this.errMsgOri = errMsgOri;
	  }

	  public void setErrOri(Exception errOri) {
	    this.errOri = errOri;
	  }


}
