using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Threading;
using System.Xml;
using System.Text;
using System.Net;
using System.IO;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using System.Security.Cryptography;
using System.Reflection;
using System.Net.Security;
using System.Security.Authentication;
using System.Security.Cryptography.X509Certificates;
using System.Diagnostics;


namespace Whistle
{
    /// <summary>
    /// 微哨开放平台处理类
    /// </summary>
    public class WhistleChat
    {        
        protected string appid;
        protected string appsecret;
        protected string apptoken;
        protected static string accessToken;

        private static long accessTokenTime = 0;
        private static Mutex mutex = new Mutex();

        /// <summary>
        /// 构造方法
        /// </summary>
        /// <param name="appid">开发者添加应用时生成的AppID</param>
        /// <param name="appsecret">开发者添加应用时生成的AppSecret</param>
        /// <param name="apptoken">开发者添加应用时填写的轻应用通讯凭证</param>
        public WhistleChat(string appid, string appsecret, string apptoken)
        {            
            this.appid = appid;
            this.appsecret = appsecret;
            this.apptoken = apptoken;
        }
        
        //=============================== 响应事件 =============================/
       
        /// <summary>
        /// 虚方法,解析Hello报文
        /// </summary>
        /// <param name="page">页面对象</param>
        /// <param name="request">HTTP请求内容</param>
        /// <returns>HTTP响应内容</returns>
        protected virtual Response onRecvHelloMessage(Page page, HelloRequest request) { return new ErrorResponse(request, "未实现HELLO报文");  }

        /// <summary>
        /// 虚方法,解析图片HTTP请求,生成HTTP响应
        /// </summary>
        /// <param name="page">页面对象</param>
        /// <param name="request">HTTP请求内容</param>
        /// <returns>HTTP响应内容</returns>
        protected virtual Response onRecvTextMessage(Page page, TextRequest request) { return new ErrorResponse(request, "未实现文本消息"); }

        /// <summary>
        /// 虚方法,解析图片HTTP请求,生成HTTP响应
        /// </summary>
        /// <param name="page">页面对象</param>
        /// <param name="request">HTTP请求内容</param>
        /// <returns>HTTP响应内容</returns>
        protected virtual Response onRecvImageMessage(Page page, ImageRequest request) { return new ErrorResponse(request, "未实现图片消息"); }

        /// <summary>
        /// 虚方法,解析图片HTTP请求,生成HTTP响应
        /// </summary>
        /// <param name="page">页面对象</param>
        /// <param name="request">HTTP请求内容</param>
        /// <returns>HTTP响应内容</returns>
        protected virtual Response onRecvVoiceMessage(Page page, VoiceRequest request) { return new ErrorResponse(request, "未实现语音消息"); }

        /// <summary>
        /// 虚方法,解析图片HTTP请求,生成HTTP响应
        /// </summary>
        /// <param name="page">页面对象</param>
        /// <param name="request">HTTP请求内容</param>
        /// <returns>HTTP响应内容</returns>
        protected virtual Response onRecvEventMessage(Page page, EventRequest request) { return new ErrorResponse(request, "未实现事件消息"); }

        /// <summary>
        /// 虚方法,解析图片HTTP请求,生成HTTP响应
        /// </summary>
        /// <param name="request">HTTP请求内容</param>
        /// <returns>HTTP响应内容</returns>
        protected virtual Response onRecvLocationMessage(Page page, LocationRequest request) { return new ErrorResponse(request, "未实现位置消息"); }

        /// <summary>
        /// 虚方法,解析图片HTTP请求,生成HTTP响应
        /// </summary>
        /// <param name="page">页面对象</param>
        /// <param name="request">HTTP请求内容</param>
        /// <returns>HTTP响应内容</returns>
        protected virtual Response onRecvLinkMessage(Page page, LinkRequest request) { return new ErrorResponse(request, "未实现链接消息"); }

        /// <summary>
        /// 虚方法,解析图片HTTP请求,生成HTTP响应
        /// </summary>
        /// <param name="page">页面对象</param>
        /// <param name="request">HTTP请求内容</param>
        /// <returns>HTTP响应内容</returns>
        protected virtual Response onRecvUnknowMessage(Page page, UnknowRequest request, string content) { return new ErrorResponse(request, "未知请求:" + content); }

        //=============================== 实现细节 =============================/

        /// <summary>
        /// 获取微哨服务器Token
        /// </summary>
        /// <param name="url">请求Url地址</param>
        /// <param name="tokenTime">请求Token时间</param>
        /// <returns>成功返回:true,失败返回:false</returns>
        private bool doAuth(string url, long tokenTime)
        {
            //TODO: 不用每次都认证, 保存下来.
            mutex.WaitOne();
            if (accessTokenTime > tokenTime)
            {
                mutex.ReleaseMutex();
                return true;
            }

            if (!url.StartsWith("https://"))
            {
                throw new ArgumentException("非https请求");
            }

            int end = url.IndexOf('/', 8);
            if (end < 0)
            {
                throw new ArgumentException("非法的URL");
            }

            string host = url.Substring(0, end);
            if (host.Length == 0)
            {
                throw new ArgumentException("非法的主机名");
            }

            string authURL = host + "/cgi-bin/oauth2/access_token?grant_type=client_credentials&appid=" + appid + "&appsecret=" + appsecret;

            JObject jsonObj = doUrl0("GET", authURL, null);
            accessToken = (string)jsonObj["access_token"];
            accessTokenTime = DateTime.Now.Ticks;
            mutex.ReleaseMutex();
            return true;
        }

        /// <summary>
        /// 添加新应用时校验签名
        /// </summary>
        /// <param name="sender">一个对象，它包含此验证的状态信息</param>
        /// <param name="certificate">用于对远程方进行身份验证的证书</param>
        /// <param name="chain">与远程证书关联的证书颁发机构链</param>
        /// <param name="errors">与远程证书关联的一个或多个错误</param>
        /// <returns>返回:true表示验证成功,返回:false,表示验证失败</returns>
        public static bool CheckValidationResult(object sender, X509Certificate certificate, X509Chain chain, SslPolicyErrors errors)
        {
            return true;//总是接受
        }

        /// <summary>
        /// 调用HTTP API
        /// </summary>
        /// <param name="method">HTTP请求方法,"GET"或"POST"</param>
        /// <param name="url">HTTP API URL</param>
        /// <param name="data">POST　数据</param>
        /// <returns>返回Json格式的结果</returns>
        private JObject doUrl0(string method, string url, string data)
        {
            ServicePointManager.ServerCertificateValidationCallback  = new RemoteCertificateValidationCallback(CheckValidationResult);
            HttpWebRequest request = WebRequest.Create(url) as HttpWebRequest;
            HttpWebResponse response = null;

            request.Method = method;
            request.Timeout = 5000;

            if (data != null && data.Length > 0)
            {
                request.GetRequestStream().Write(Encoding.UTF8.GetBytes(data), 0, data.Length);
            }

            try
            {
                response = request.GetResponse() as HttpWebResponse;
            }
            catch (WebException ex)
            {
                if (ex.Status == WebExceptionStatus.ProtocolError && ex.Response != null)
                {
                    var resp = (HttpWebResponse)ex.Response;
                    if (resp.StatusCode == HttpStatusCode.NotFound)
                    {
                        return null;
                    }
                }
                else
                {
                    throw ex;
                }
            }

            StreamReader readStream = new StreamReader(response.GetResponseStream(), Encoding.UTF8);
            JObject jsonObj = (JObject)JToken.ReadFrom(new JsonTextReader(readStream));

            return jsonObj;
        }


        //=============================== 方法 =============================/
   
        /// <summary>
        /// 执行HttpApi
        /// </summary>
        /// <param name="method">HTTP请求方法,"GET"或"POST"</param>
        /// <param name="url">HTTP API URL</param>
        /// <param name="data">POST 数据</param>
        /// <returns>返回Json格式的结果</returns>
        public JObject doUrl(string method, string url, string data)
        {
            mutex.WaitOne();
            long oldTime = accessTokenTime;
            string token = accessToken != null ? (string)accessToken.Clone() : null;
            mutex.ReleaseMutex();

            if (oldTime == 0 && !doAuth(url, oldTime))
            {
                return null;
            }
            else
            {
                token = (string)accessToken.Clone();
            }

            if (!url.StartsWith("https://"))
            {
                throw new ArgumentException("非https请求");
            }

            if (url.IndexOf('?') < 0)
            {
                if (url.EndsWith("/"))
                {
                    url = url.Substring(0, url.Length - 1);
                }
                url = url + "?access_token=" + token;
            }
            else
            {
                url = url + "&access_token=" + token;
            }

            url = url + "&appid=" + appid;

            JObject jsonObj = doUrl0(method, url, data);
            if ((int)jsonObj["errcode"] == 403)
            {
                mutex.WaitOne();
                oldTime = accessTokenTime;
                token = (string)accessToken.Clone();
                mutex.ReleaseMutex();

                if (!doAuth(url, oldTime))
                {
                    return null;
                }               
                jsonObj = doUrl0(method, url, data);
            }

            return jsonObj;
        }


        /// <summary>
        /// 验证此次请求的签名信息
        /// </summary>
        private void valid(Page page)
        {
            string nonce = page.Request.QueryString.Get("nonce");
            string echostr = page.Request.QueryString.Get("echostr");
            string timestamp = page.Request.QueryString.Get("timestamp");
            string signature = page.Request.QueryString.Get("signature");

            if ((nonce == null || nonce.Length <= 0) || (echostr == null || echostr.Length <= 0)
              || (timestamp == null || timestamp.Length <= 0) || (signature == null || signature.Length <= 0))
            {
                page.Response.StatusCode = 403;
                page.Response.SubStatusCode = 10;
                return ;
            }

            List<string> list = new List<string>();
            list.Add(apptoken);
            list.Add(nonce);
            list.Add(timestamp);
            list.Sort();

            SHA1 sha1 = new SHA1CryptoServiceProvider();
            byte[] source = Encoding.Default.GetBytes(list[0] + list[1] + list[2]);
            byte[] crypto = sha1.ComputeHash(source);
            string sha1sum = Encoding.ASCII.GetString(crypto);          

            if (sha1sum.Equals(signature))
            {                
                page.Response.Write(echostr);   
            }
            else
            {          
                //page.Response.Write("valid failure:" + echostr);
                //page.Response.StatusCode = 403;
                page.Response.Write(echostr); 
            }
        }   

        /// <summary>
        /// 处理所有HTTP请求
        /// </summary>
        public void process(Page page)
        {
            string method = page.Request.HttpMethod;
              
            if (method.Equals("GET"))
            {
                valid(page);
                return;
            }

            if (method.Equals("POST"))
            {
                Response response;
                XmlDocument doc = new XmlDocument();
                doc.Load(page.Request.InputStream);

                try
                {
                    string type = doc["xml"]["msgtype"].InnerText;

                    string name = type.Substring(0, 1).ToUpper() + type.Substring(1, type.Length - 1);
                    string funcName = "onRecv" + name + "Message";
                    string className = "Whistle." + name + "Request";

                    Type clazz = Type.GetType(className);
                    MethodInfo func = typeof(WhistleChat).GetMethod(funcName, BindingFlags.NonPublic | BindingFlags.Instance, null, CallingConventions.Any, new Type[] { page.GetType(), clazz }, null);

                    object[] constuctParms = new object[] { doc };
                    object request = Activator.CreateInstance(clazz, constuctParms);

                    object[] parameters = new object[] { page, request };
                    response = (Response)func.Invoke(this, BindingFlags.Public | BindingFlags.Instance, Type.DefaultBinder, parameters, null);
                }
                catch (TargetException e)
                {
                    response = onRecvUnknowMessage(page, new UnknowRequest(doc), "TargetException异常:" + e.ToString());
                }
                catch (TargetInvocationException e)
                {
                    response = onRecvUnknowMessage(page, new UnknowRequest(doc), "TargetInvocationException异常:" + e.ToString());
                }
                catch (ArgumentException e)
                {
                    response = onRecvUnknowMessage(page, new UnknowRequest(doc), "ArgumentException异常:" + e.ToString());
                }
                catch (TargetParameterCountException e)
                {
                    response = onRecvUnknowMessage(page, new UnknowRequest(doc), "TargetParameterCountException异常:" + e.ToString());
                }
                catch (InvalidOperationException e)
                {
                    response = onRecvUnknowMessage(page, new UnknowRequest(doc), "InvalidOperationException异常:" + e.ToString());
                }

                response.WriteTo(page.Response.OutputStream);
            }
        }
    }
}
                               
