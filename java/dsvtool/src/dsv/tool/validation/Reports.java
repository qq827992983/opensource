package dsv.tool.validation;

public interface Reports {
	public final static String success = "{\"ret\":0,\"errcode\":0,\"msg\":\"操作成功\"}";
	public final static String failed = "{\"ret\":-1,\"errcode\":-1,\"msg\":\"操作失败\"}";
	public final static String pluginNotExist = "{\"ret\":-1,\"errcode\":-2,\"msg\":\"插件不存在\"}";
	public final static String pluginEnvError = "{\"ret\":-1,\"errcode\":-3,\"msg\":\"插件环境错误\"}";
	public final static String connectMysqlError = "{\"ret\":-1,\"errcode\":-4,\"msg\":\"连接mysql失败\"}";
	public final static String connectMongoError = "{\"ret\":-1,\"errcode\":-5,\"msg\":\"连接mongo失败\"}";
	public final static String personInfoError = "{\"ret\":-1,\"errcode\":-6,\"msg\":\"用户个人信息错误\"}";
	public final static String orgnizationTreeError = "{\"ret\":-1,\"errcode\":-7,\"msg\":\"用户组织结构树错误\"}";
	public final static String orgnizationRelationshipError = "{\"ret\":-1,\"errcode\":-8,\"msg\":\"用户组织结构关系错误\"}";
}
