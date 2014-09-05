using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;
using System.Xml;

namespace Whistle
{
    /// <summary>
    /// 抽象类,生成XML应答报文
    /// </summary>
    public abstract class Response
    {
        /// <summary>
        /// 报文类型
        /// </summary>
        public string Type
        {
            protected set;
            get;
        }

        /// <summary>
        /// 发送者
        /// </summary>
        public string FromUsername
        {
            protected set;
            get;
        }

        /// <summary>
        /// 接收者
        /// </summary>
         public string ToUsername
        {
            protected set;
             get;
        }

        /// <summary>
        /// 创建时间
        /// </summary>
         public Int64 CreateTime
        {
            protected set;
             get;
        }

        protected Request request;

        /// <summary>
        /// 构造方法
        /// </summary>
        /// <param name="request">请求内容</param>
        public Response(Request request)
        {
            this.request = request;
        }

        /// <summary>
        /// 将XML报文写入流
        /// </summary>
        /// <param name="outputStream">要写入的流</param>
        public void WriteTo(Stream outputStream)
        {
            XmlDocument doc = new XmlDocument();
     
            doc.AppendChild(doc.CreateElement("xml"));
            encode(doc);
            doc.Save(outputStream);
        }

        /// <summary>
        /// 生成XML报文
        /// </summary>
        /// <param name="doc">XML报文</param>
        protected virtual  void  encode(XmlDocument doc)
        {
            appendNode(doc["xml"], "msgid", request.MsgId);
            appendNode(doc["xml"], "fromusername", request.FromUsername);
            appendNode(doc["xml"], "tousername", request.ToUsername);
            appendNode(doc["xml"], "createtime", request.CreateTime);
        }

        /// <summary>
        /// XML报文新增string类型节点
        /// </summary>
        /// <param name="parent">父节点</param>
        /// <param name="name">新增节点名称</param>
        /// <param name="cdata">新增节点内容</param>
        /// <returns>新增节点</returns>
        protected XmlNode appendNode(XmlNode parent, string name, string cdata = null)
        {
            XmlDocument doc = parent.OwnerDocument;
            XmlNode node = parent.AppendChild(doc.CreateElement(name));

            if (cdata != null && cdata.Length > 0)
            {
                return node.AppendChild(doc.CreateCDataSection(cdata != null ? cdata : ""));
            }

            return node;
        }

        /// <summary>
        /// XML报文新增int类型节点
        /// </summary>
        /// <param name="parent">父节点</param>
        /// <param name="name">新增节点名称</param>
        /// <param name="num">新增节点内容</param>
        protected void appendNode(XmlNode parent, string name, Int64 num)
        {
            XmlDocument doc = parent.OwnerDocument;
            parent.AppendChild(doc.CreateElement(name)).AppendChild(doc.CreateCDataSection(Convert.ToString(num)));
        }
    }

    /// <summary>
    /// 文本报文生成
    /// </summary>
    public class TextResponse : Response
    {
        /// <summary>
        /// 文本内容
        /// </summary>
        public string Content
        {
             set;
             get;
        }

        /// <summary>
        /// 构造方法
        /// </summary>
        /// <param name="request">请求报文内容</param>
        public TextResponse(Request request): base(request) {}

        /// <summary>
        /// 生成XML报文
        /// </summary>
        /// <param name="doc">XML报文</param>
        protected override void encode(XmlDocument doc)
        {
            base.encode(doc);
            appendNode(doc["xml"], "msgtype", "text");
            appendNode(doc["xml"], "content", Content);
           
        }
    }

    /// <summary>
    /// 音乐报文生成
    /// </summary>
    public class MusicResponse : Response
    {
        /// <summary>
        /// 标题
        /// </summary>
        public string Title
        {
             set;
             get;
        }

        /// <summary>
        /// 描述信息
        /// </summary>
        public string Description
        {
             set;
             get;
        }

        /// <summary>
        /// 音乐链接
        /// </summary>
        public string MusicUrl
        {
             set;
             get;
        }

        /// <summary>
        /// 高品质音乐链接
        /// </summary>
        public string HQMusicUrl
        {
             set;
             get;
        }

        /// <summary>
        /// 构造方法
        /// </summary>
        /// <param name="request">请求报文</param>
        public MusicResponse(Request request) : base(request) { }

        /// <summary>
        /// 生成XML报文
        /// </summary>
        /// <param name="doc">XML报文</param>
        protected override void encode(XmlDocument doc)
        {            
             base.encode(doc);
             appendNode(doc["xml"], "msgtype", "music");

            XmlNode node = appendNode(doc["xml"], "music");
            appendNode(node, "title", Title);
            appendNode(node, "description", Description);
            appendNode(node, "musicurl", MusicUrl);
            appendNode(node, "hqmusicurl", HQMusicUrl);
           
        }
    }

    /// <summary>
    /// 图文报文
    /// </summary>
    public class Article {
        /// <summary>
        /// 标题
        /// </summary>
        public string Title {
            get;
            set;
        }

        /// <summary>
        /// 描述信息
        /// </summary>
        public string Description {
            get;
            set;
        }

        /// <summary>
        /// 图片链接
        /// </summary>
        public string PicUrl  {
            get;
            set;
        }

        /// <summary>
        /// 跳转地址
        /// </summary>
        public string Url  {
            get;
            set;
        }

        public Article() { }

        /// <summary>
        /// 构造方法
        /// </summary>
        /// <param name="title">报文Title内容</param>
        /// <param name="description">报文Description内容</param>
        /// <param name="picUrl">报文PicUrl内容</param>
        /// <param name="url">报文Url内容</param>
        public Article(string title, string description, string picUrl, string url = null) {
            Title = title;
            Description = description;
            PicUrl = picUrl;
            Url = url;
        }
    }

    /// <summary>
    /// 图文报文生成
    /// </summary>
    public class ArticleResponse : Response
    {
        /// <summary>
        /// 图文混排列表
        /// </summary>
        public List<Article> Articles
        {
             set;
             get;
        }

        /// <summary>
        /// 构造方法
        /// </summary>
        /// <param name="request">请求报文内容</param>
        public ArticleResponse(Request request) : base(request) {
            Articles = new List<Article>();
        }

        /// <summary>
        /// XML报文生成
        /// </summary>
        /// <param name="doc">XML报文</param>
        protected override void encode(XmlDocument doc)
        {
            base.encode(doc);
            appendNode(doc["xml"], "msgtype", "news");
            appendNode(doc["xml"], "articlecount", Articles.Count);

            XmlNode node = appendNode(doc["xml"], "articles");

            for (int i = 0; i < Articles.Count; ++i)
            {
                XmlNode item = appendNode(node, "item");
                appendNode(item, "title", Articles[i].Title);
                appendNode(item, "description", Articles[i].Description);
                appendNode(item, "picurl", Articles[i].PicUrl);
                appendNode(item, "url", Articles[i].Url);
            }                    
        }
    }

    /// <summary>
    /// 错误报文生成
    /// </summary>
    public class ErrorResponse : TextResponse
    {
        /// <summary>
        /// 构造方法
        /// </summary>
        /// <param name="request">请求报文内容</param>
        /// <param name="errmsg">错误消息</param>
        public ErrorResponse(Request request, string errmsg)
            : base(request)
        {           
            Content = errmsg;
        }
    }
}