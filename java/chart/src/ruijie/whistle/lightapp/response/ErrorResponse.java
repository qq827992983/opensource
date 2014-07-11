package ruijie.whistle.lightapp.response;

import ruijie.whistle.lightapp.request.Request;

/**
* Note:错误响应报文<br>
*/
public class ErrorResponse extends TextResponse {
	/**
	* Note:构造方法 <br>
	* 	public ErrorResponse(Request request, String errmsg) ;
	* 
	* @param request 请求对象
	* @param errmsg 返回给用户的错误信息
	*/ 
	public ErrorResponse(Request request, String errmsg) {
		super(request);
		setContent(errmsg);
	}
}
