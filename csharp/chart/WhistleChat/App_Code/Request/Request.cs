using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Xml;

namespace Whistle
{
    /// <summary>
    /// 抽象类,HTTP请求报文
    /// </summary>
    public abstract class Request {
        /// <summary>
        /// 消息ID
        /// </summary>
        public string MsgId {
            get {               
                return Doc["xml"]["msgid"].InnerText;
            }            
        }

        /// <summary>
        /// 消息类型:
        ///<value="text">文本</value>
        ///<value="image">图片</value>
        ///<value="location">地理位置</value>
        ///<value="link">链接</value> 
        ///<value="news">链接</value> 
        /// </summary>
        public string Type {
             get {
                return Doc["xml"]["type"].InnerText;
            }            
        }

        /// <summary>
        /// 发送者学工号
        /// </summary>
        public string FromStudentNumber {
             get {
                return Doc["xml"]["fromstudentnumber"].InnerText;
            }            
        }

        /// <summary>
        /// 发送者JID
        /// </summary>
        public string FromUsername {
             get {
                return Doc["xml"]["fromusername"].InnerText;
            }            
        }

        /// <summary>
        /// 接收者
        /// </summary>
        public string ToUsername {
             get {
                return Doc["xml"]["tousername"].InnerText;
            }            
        }  

        /// <summary>
        /// 消息创建时间
        /// </summary>
        public Int64 CreateTime {
             get {
                return Convert.ToInt64(Doc["xml"]["createtime"].InnerText);
            }            
        }

        /// <summary>
        /// HTTP请求中的XML报文
        /// </summary>
        protected XmlDocument Doc {
             get;
            private set;
        }
        
        /// <summary>
        /// 构造方法
        /// </summary>
        /// <param name="Doc">HTTP请求中解析出的XML报文</param>
        public Request(XmlDocument Doc) {
            this.Doc = (XmlDocument)Doc.Clone();
        }
    }

    /// <summary>
    /// Hello报文
    /// </summary>
    public class HelloRequest: Request
    {
        /// <summary>
        /// 文本内容
        /// </summary>
        public string Content
        {
             get {
                 return Doc["xml"]["content"].InnerText;
            }   
        }
        
        /// <summary>
        /// 构造方法
        /// </summary>
        /// <param name="Doc">HTTP请求中解析出的XML报文</param>
        public HelloRequest(XmlDocument Doc): base(Doc) {}
    }

    /// <summary>
    /// 文本报文
    /// </summary>
    public class TextRequest: Request
    {
        /// <summary>
        /// 文本内容
        /// </summary>
        public string Content
        {
             get {
                return Doc["xml"]["content"].InnerText;
            }   
        }

        /// <summary>
        /// 构造方法
        /// </summary>
        /// <param name="Doc">HTTP请求中解析出的XML报文</param>
        public TextRequest(XmlDocument Doc) : base(Doc) { }
    }

    /// <summary>
    /// 音频报文
    /// </summary>
    public class VoiceRequest : Request
    {
        //TODO: 属性待确认
        public VoiceRequest(XmlDocument Doc) : base(Doc) { }
    }

    /// <summary>
    /// 图像报文
    /// </summary>
    public class ImageRequest : Request
    {
        /// <summary>
        /// 图片链接
        /// </summary>
        public string PicUrl
        {
             get {
                return Doc["xml"]["picurl"].InnerText;
            }   
        }

        /// <summary>
        /// 构造方法
        /// </summary>
        /// <param name="Doc">HTTP请求中解析出的XML报文</param>
        public ImageRequest(XmlDocument Doc) : base(Doc) { }
    }

    /// <summary>
    /// 位置报文
    /// </summary>
    public class LocationRequest: Request
    {
        /// <summary>
        /// 地理位置信息
        /// </summary>
        public string Label
        {
             get {
                return Doc["xml"]["label"].InnerText;
            }   
        }

        /// <summary>
        /// 地图缩放大小
        /// </summary>
        public string Scale
        {
             get {                
                return Doc["xml"]["scale"].InnerText;
            }   
        }

        /// <summary>
        /// 地理位置纬度
        /// </summary>
        public  string Location_X
        {
             get {                
                return Doc["xml"]["location_x"].InnerText;
            }   
        }

        /// <summary>
        /// 地理位置经度
        /// </summary>
        public  string Location_Y
        {
             get {                
                return Doc["xml"]["location_y"].InnerText;
            }   
        }

        /// <summary>
        /// 构造方法
        /// </summary>
        /// <param name="Doc">HTTP请求中解析出的XML报文</param>
        public LocationRequest(XmlDocument Doc) : base(Doc) { }
    }

    /// <summary>
    /// 链接报文
    /// </summary>
    public class LinkRequest: Request
    {
        /// <summary>
        /// 消息标题
        /// </summary>
        public string Title
        {
             get {                
                return Doc["xml"]["title"].InnerText;
            }   
        }

        /// <summary>
        /// 消息描述
        /// </summary>
        public string Description
        {
             get {                
                return Doc["xml"]["description"].InnerText;
            }   
        }

        /// <summary>
        /// 消息链接
        /// </summary>
        public string Url
        {
             get {                
                return Doc["xml"]["url"].InnerText;
            }   
        }

        /// <summary>
        /// 构造方法
        /// </summary>
        /// <param name="Doc">HTTP请求中解析出的XML报文</param>
        public LinkRequest(XmlDocument Doc) : base(Doc) { }
    }

    /// <summary>
    /// 事件报文
    /// </summary>
    public class EventRequest: Request
    {
        /// <summary>
        /// 时间类型
        /// </summary>
        public  string Event
        {
             get {                
                return Doc["xml"]["event"].InnerText;
            }   
        }

        /// <summary>
        /// 事件KEY值
        /// </summary>
        public string EventKey
        {
             get {                
                return Doc["xml"]["eventkey"].InnerText;
            }   
        }

        /// <summary>
        /// 构造方法
        /// </summary>
        /// <param name="Doc">HTTP请求中解析出的XML报文</param>
        public EventRequest(XmlDocument Doc) : base(Doc) { }
    }

    /// <summary>
    /// 未知报文
    /// </summary>
    public class UnknowRequest : Request
    {
        /// <summary>
        /// 构造方法
        /// </summary>
        /// <param name="Doc">HTTP请求中解析出的XML报文</param>
        public UnknowRequest(XmlDocument Doc) : base(Doc) { }
    }
}