using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Whistle;


/// <summary>
/// 程序入口类
/// </summary>
public partial class _Default : System.Web.UI.Page
{
    /// <summary>
    /// 程序入口方法
    /// </summary>
    /// <param name="sender">事件对应控件的对象</param>
    /// <param name="e">事件参数</param>
    protected void Page_Load(object sender, EventArgs e)
    {
        //使用HttpAPI
        HttpApiDemo();

        //TODO:!!!appid,appsecret,apptoken由用户在微哨添加新应用时自己填写!!!
        string appid = "mvtnpjaxchcb58t4bknrw";//轻应用的：appid
        string appsecret = "odllowrlzwi0ztg2owe0ngniy2y0ogvmytlizwu4ntflztvhmtdhng";//轻应用的：appsecret
        string apptoken = "123456";//轻应用的：apptoken
        WhistleChat app = new MyLightApp(appid, appsecret, apptoken);
        app.process(this);
 
    }

    /// <summary>
    /// HttpApi使用函数
    /// </summary>
    protected void HttpApiDemo()
    {
        string appid = "1gdmtrzdjos95zuc4x50a";//接入管理的：appid
        string appsecret = "mjc5odvmm2e3ztkwyzdhzgu1odc5zje1mgvlzddhnjk3yzhiymziza";//接入管理的：appsecret
        string apptoken = "";//接入管理无apptoken
        WhistleChat app = new MyLightApp(appid, appsecret, apptoken);

        //发送应用消息
        string message = "{\"recipient\":{\"certType\":\"4\",\"certNum\":\"123456789\"},\"message\":{\"html\":\"how are you\",\"hyperlink\":\"http://www.baidu.com\"}}";
        JObject jRetMessage = app.doUrl("POST", "https://172.16.56.105:8443/api/utils/send_message", message);
        //发送应用通知
        string notify = "{\"recipient\": {\"identities\": [\"teacher\",\"student\", \"other\"]},\"message\": {\"title\": \"test\",\"priority\": \"normal\",\"expired_time\": \"2014-08-04 12:00:00\",\"hyperlink\": \"http://www.baidu.com\",\"html\":\"sorry\"}}";
        JObject jRetNotify = app.doUrl("POST", "https://172.16.56.105:8443/api/utils/send_notification", notify);
    }
}

/// <summary>
/// MyLightApp提供了一个LightApp实例
/// </summary>
class MyLightApp : WhistleChat
{
    /// <summary>
    /// MyLightApp构造方法
    /// </summary>
    /// <param name="appid">开发者添加应用时生成的AppID</param>
    /// <param name="appsecret">开发者添加应用时生成的AppSecret</param>
    /// <param name="apptoken">开发者添加应用时填写的轻应用通讯凭证</param>
    public MyLightApp(string appid, string appsecret, string apptoken) : base(appid, appsecret, apptoken) { }

    /// <summary>
    /// 响应Hello事件,生成应答报文
    /// </summary>
    /// <param name="page">接收和处理请求的页面对象</param>
    /// <param name="requset">Hello报文内容</param>
    /// <returns>返回:应答报文</returns>
    protected override Response onRecvHelloMessage(Page page, HelloRequest requset)
    {
        TextResponse response = new TextResponse(requset);
        JObject jGetUserInfo = doUrl("GET", "https://172.16.56.171:8443/api/user/get_info?student_number=" + requset.FromStudentNumber, null);
        string userName = jGetUserInfo["data"]["name"].ToString();
        string studentNumber = jGetUserInfo["data"]["student_number"].ToString();
        string sex = "";
        if (jGetUserInfo["data"]["sex"].ToString().Equals("boy"))
            sex = "男";
        else
            sex = "女";
        string organization = jGetUserInfo["data"]["organization"].ToString();
        string aid = jGetUserInfo["data"]["aid"].ToString();
        response.Content = "尊敬的:" + userName.ToString() + ",您好!这是Hello报文,以下是您的基本信息:"+"姓名:"+userName+",性别:"+sex+",学号:" + studentNumber + ",组织结构:" + organization;
        return response;
    }

    /// <summary>
    /// 响应文本事件,生成应答报文
    /// </summary>
    /// <param name="page">接收和处理请求的页面对象</param>
    /// <param name="request">文本报文内容</param>
    /// <returns>返回:应答报文</returns>
    protected override Response onRecvTextMessage(Page page, TextRequest request)
    {
        Response response;

        switch (request.Content)
        {
            case "1":
                {
                    ArticleResponse res = new ArticleResponse(request);
                    response = res;
                    res.Articles.Add(new Article("这是图文消息", "不要点我", "http://www.weishao.com.cn/upload/image/1385434253.png"));
                    res.Articles.Add(new Article("第一点", "点我跳到微哨", "http://www.weishao.com.cn/upload/image/1385434408.png", "http://www.weishao.com.cn"));
                    res.Articles.Add(new Article("第二点", "点我跳到百度", "http://www.weishao.com.cn/upload/image/1385693782.png", "http://www.baidu.com"));
                }
                break;
            case "2":
                {
                    MusicResponse res = new MusicResponse(request);
                    response = res;
                    res.Title = "忐忑";
                    res.Description = "这是一首奇妙的乐曲";
                    res.MusicUrl = "http://www.baidu.com/mp3/1231233.mp3";
                    res.HQMusicUrl = "http://www.baidu.com/mp3/hq1231233.mp3";
                }
                break;
            case "3":
                {
                    TextResponse res = new TextResponse(request);
                    response = res;
                    res.Content = "您好,按键3的功能就留给您实现吧!";
                }
                break;
            default:
                {
                    TextResponse res = new TextResponse(request);
                    response = res;
                    res.Content = "这是文本消息, 输入1返回图文消息, 输入2返回音乐消息,输入3返回文本信息, 输入其他内容, 显示本消息";
                }
                break;
        }
        return response;
    }

    /// <summary>
    /// 响应图片事件,生成应答报文
    /// </summary>
    /// <param name="page">接收和处理请求的页面对象</param>
    /// <param name="request">图片报文内容</param>
    /// <returns>返回:应答报文</returns>
    protected override Response onRecvImageMessage(Page page, ImageRequest request)
    {
        TextResponse response = new TextResponse(request);
        response.Content = "这是图片消息";
        return response;
    }

    /// <summary>
    /// 响应音频事件,生成应答报文
    /// </summary>
    /// <param name="page">接收和处理请求的页面对象</param>
    /// <param name="request">音频报文内容</param>
    /// <returns>返回:应答报文</returns>
    protected override Response onRecvVoiceMessage(Page page, VoiceRequest request)
    {
        TextResponse response = new TextResponse(request);
        response.Content = "这是语音消息";
        return response;
    }

    /// <summary>
    /// 响应事件(如:菜单操作事件),生成应答报文
    /// </summary>
    /// <param name="page">接收和处理请求的页面对象</param>
    /// <param name="request">事件报文内容</param>
    /// <returns>返回:应答报文</returns>
    protected override Response onRecvEventMessage(Page page, EventRequest request)
    {
        TextResponse response = new TextResponse(request);
        response.Content = "这是菜单事件";
        return response;
    }

    /// <summary>
    /// 响应位置事件,生成应答报文
    /// </summary>
    /// <param name="page">接收和处理请求的页面对象</param>
    /// <param name="request">位置报文内容</param>
    /// <returns>返回:应答报文</returns>
    protected override Response onRecvLocationMessage(Page page, LocationRequest request)
    {
        TextResponse response = new TextResponse(request);
        response.Content = "这是位置消息";
        return response;
    }

    /// <summary>
    /// 响应链接事件,生成应答报文
    /// </summary>
    /// <param name="page">接收和处理请求的页面对象</param>
    /// <param name="request">链接报文内容</param>
    /// <returns>返回:应答报文</returns>
    protected override Response onRecvLinkMessage(Page page, LinkRequest request)
    {
        TextResponse response = new TextResponse(request);
        response.Content = "这是链接消息";
        return response;
    }

    /// <summary>
    /// 响应位置事件,生成应答报文
    /// </summary>
    /// <param name="page"></param>
    /// <param name="request"></param>
    /// <returns></returns>
    protected override Response onRecvUnknowMessage(Page page, UnknowRequest request, string content)
    {
        TextResponse response = new TextResponse(request);
        response.Content = content;
        return response;
    }
}

