<?php
/**
 * 微哨开放平台 PHP SDK
 *
 */

  /**
   * 微哨开放平台处理类
   */
  class Whistlechat {

    /**
     * 调试模式，将错误通过文本消息回复显示
     *
     * @var boolean
     */
    private $debug;

    /**
     * 以数组的形式保存微哨服务器每次发来的请求
     *
     * @var array
     */
    private $request;

    /**
     * 初始化，判断此次请求是否为验证请求，并以数组形式保存
     *
     * @param string $token 验证信息
     * @param boolean $debug 调试模式，默认为关闭
     */
    public function __construct($token, $debug = FALSE) {
      if (!$this->validateSignature($token)) {
        exit('签名验证失败');
      }
      
      if ($this->isValid()) {
        // 网址接入验证
        exit($_GET['echostr']);
      }

      if (!isset($GLOBALS['HTTP_RAW_POST_DATA'])) {
        exit('缺少数据');
      }

      $this->debug = $debug;
      set_error_handler(array(&$this, 'errorHandler'));
      // 设置错误处理函数，将错误通过文本消息回复显示

      $xml = (array) simplexml_load_string($GLOBALS["HTTP_RAW_POST_DATA"], 'SimpleXMLElement', LIBXML_NOCDATA);

      $this->request = array_change_key_case($xml, CASE_LOWER);
      // 将数组键名转换为小写，提高健壮性，减少因大小写不同而出现的问题
    }

    /**
     * 判断此次请求是否为验证请求
     *
     * @return boolean
     */
    private function isValid() {
      return isset($_GET['echostr']);
    }

    /**
     * 验证此次请求的签名信息
     *
     * @param  string $token 验证信息
     * @return boolean
     */
    private function validateSignature($token) {
      if ( ! (isset($_GET['signature']) && isset($_GET['timestamp']) && isset($_GET['nonce']))) {
        return FALSE;
      }
      
      $signature = $_GET['signature'];
      $timestamp = $_GET['timestamp'];
      $nonce = $_GET['nonce'];

      $signatureArray = array($token, $timestamp, $nonce);
      sort($signatureArray);

      return sha1(implode($signatureArray)) == $signature;
    }

    /**
     * 获取本次请求中的参数，不区分大小
     *
     * @param  string $param 参数名，默认为无参
     * @return mixed
     */
    protected function getRequest($param = FALSE) {
      if ($param === FALSE) {
        return $this->request;
      }

      $param = strtolower($param);

      if (isset($this->request[$param])) {
        return $this->request[$param];
      }

      return NULL;
    }

	/**
     * 收到hello报文消息时触发，用于子类重写
     *
     * @return void
     */
    protected function onHello() {}

	 /**
     * 收到文本消息时触发，用于子类重写
     *
     * @return void
     */
    protected function onText() {}

    /**
     * 收到图片消息时触发，用于子类重写
     *
     * @return void
     */
    protected function onImage() {}

    /**
     * 收到地理位置消息时触发，用于子类重写
     *
     * @return void
     */
	protected function onVoice() {}

    /**
     * 收到地理位置消息时触发，用于子类重写
     *
     * @return void
     */
    protected function onEvent() {}
	/**
     * 收到地理位置消息时触发，用于子类重写
     *
     * @return void
     */
    protected function onLocation() {}
    /**
     * 收到链接消息时触发，用于子类重写
     *
     * @return void
     */
    protected function onLink() {}

    /**
     * 收到未知类型消息时触发，用于子类重写
     *
     * @return void
     */
    protected function onUnknown() {}

    /**
     * 回复文本消息
     *
     * @param  string  $content  消息内容
     
     * @return void
     */
    protected function responseText($content) {
      exit(new TextResponse($this->getRequest('fromusername'), $this->getRequest('tousername'), $content));
    }

    /**
     * 回复音乐消息
     *
     * @param  string  $title       音乐标题
     * @param  string  $description 音乐描述
     * @param  string  $musicUrl    音乐链接
     * @param  string  $hqMusicUrl  高质量音乐链接，Wi-Fi 环境下优先使用
     * @return void
     */
    protected function responseMusic($title, $description, $musicUrl, $hqMusicUrl) {
      exit(new MusicResponse($this->getRequest('fromusername'), $this->getRequest('tousername'), $title, $description, $musicUrl, $hqMusicUrl));
    }

    /**
     * 回复图文消息
     * @param  array   $items    由单条图文消息类型 NewsResponseItem() 组成的数组
     * @return void
     */
    protected function responseNews($items) {
      exit(new NewsResponse($this->getRequest('fromusername'), $this->getRequest('tousername'), $items));
    }

    /**
     * 分析消息类型，并分发给对应的函数
     *
     * @return void
     */
    public function run() {
      switch ($this->getRequest('msgtype')) {

        case 'text':
          $this->onText();
          break;

		case 'hello':
          $this->onHello();
          break;

        case 'image':
          $this->onImage();
          break;
		  
		case 'voice':
          $this->onVoice();
          break;

		case 'event':
          $this->onEvent();
          break;

        case 'location':
          $this->onLocation();
          break;

        case 'link':
          $this->onLink();
          break;

        default:
          $this->onUnknown();
          break;

      }
    }

    /**
     * 自定义的错误处理函数，将 PHP 错误通过文本消息回复显示
     * @param  int $level   错误代码
     * @param  string $msg  错误内容
     * @param  string $file 产生错误的文件
     * @param  int $line    产生错误的行数
     * @return void
     */
    protected function errorHandler($level, $msg, $file, $line) {
      if ( ! $this->debug) {
        return;
      }

      $error_type = array(
        // E_ERROR             => 'Error',
        E_WARNING           => 'Warning',
        // E_PARSE             => 'Parse Error',
        E_NOTICE            => 'Notice',
        // E_CORE_ERROR        => 'Core Error',
        // E_CORE_WARNING      => 'Core Warning',
        // E_COMPILE_ERROR     => 'Compile Error',
        // E_COMPILE_WARNING   => 'Compile Warning',
        E_USER_ERROR        => 'User Error',
        E_USER_WARNING      => 'User Warning',
        E_USER_NOTICE       => 'User Notice',
        E_STRICT            => 'Strict',
        E_RECOVERABLE_ERROR => 'Recoverable Error',
        E_DEPRECATED        => 'Deprecated',
        E_USER_DEPRECATED   => 'User Deprecated',
      );

      $template = <<<ERR
PHP 报错啦！

%s: %s
File: %s
Line: %s
ERR;

      $this->responseText(sprintf($template,
        $error_type[$level],
        $msg,
        $file,
        $line
      ));
    }

  }

  /**
   * 用于回复的基本消息类型
   */
  abstract class WhistlechatResponse {

    protected $toUserName;
    protected $fromUserName;
    protected $template;

    public function __construct($toUserName, $fromUserName) {
      $this->toUserName = $toUserName;
      $this->fromUserName = $fromUserName;
    }

    abstract public function __toString();

  }

  /**
   * 用于回复的文本消息类型
   */
  class TextResponse extends WhistlechatResponse {

    protected $content;

    public function __construct($toUserName, $fromUserName, $content) {
      parent::__construct($toUserName, $fromUserName);

      $this->content = $content;
      $this->template = <<<XML
<xml>
  <tousername><![CDATA[%s]]></tousername>
  <fromusername><![CDATA[%s]]></fromusername>
  <createtime>%s</createtime>
  <msgtype><![CDATA[text]]></msgtype>
  <content><![CDATA[%s]]></content>
</xml>
XML;
    }

    public function __toString() {
      return sprintf($this->template,
        $this->toUserName,
        $this->fromUserName,
        time(),
        $this->content
      );
    }

  }

  /**
   * 用于回复的音乐消息类型
   */
  class MusicResponse extends WhistlechatResponse {

    protected $title;
    protected $description;
    protected $musicUrl;
    protected $hqMusicUrl;

    public function __construct($toUserName, $fromUserName, $title, $description, $musicUrl, $hqMusicUrl) {
      parent::__construct($toUserName, $fromUserName);

      $this->title = $title;
      $this->description = $description;
      $this->musicUrl = $musicUrl;
      $this->hqMusicUrl = $hqMusicUrl;
      $this->template = <<<XML
<xml>
  <tousername><![CDATA[%s]]></tousername>
  <fromusername><![CDATA[%s]]></fromusername>
  <createtime>%s</createtime>
  <msgtype><![CDATA[music]]></msgtype>
  <music>
    <title><![CDATA[%s]]></title>
    <description><![CDATA[%s]]></description>
    <musicurl><![CDATA[%s]]></musicurl>
    <hqmusicurl><![CDATA[%s]]></hqmusicurl>
  </music>
</xml>
XML;
    }

    public function __toString() {
      return sprintf($this->template,
        $this->toUserName,
        $this->fromUserName,
        time(),
        $this->title,
        $this->description,
        $this->musicUrl,
        $this->hqMusicUrl
      );
    }

  }

  /**
   * 用于回复的图文消息类型
   */
  class NewsResponse extends WhistlechatResponse {

    protected $items = array();

    public function __construct($toUserName, $fromUserName, $items) {
      parent::__construct($toUserName, $fromUserName);

      $this->items = $items;
      $this->template = <<<XML
<xml>
  <tousername><![CDATA[%s]]></tousername>
  <fromusername><![CDATA[%s]]></fromusername>
  <createtime>%s</createtime>
  <msgtype><![CDATA[news]]></msgtype>
  <articlecount>%s</articlecount>
  <articles>
    %s
  </articles>
</xml>
XML;
    }

    public function __toString() {
      return sprintf($this->template,
        $this->toUserName,
        $this->fromUserName,
        time(),
        count($this->items),
        implode($this->items)
      );
    }

  }

  /**
   * 单条图文消息类型
   */
  class NewsResponseItem {

    protected $title;
    protected $description;
    protected $picUrl;
    protected $url;
    protected $template;

    public function __construct($title, $description, $picUrl, $url) {
      $this->title = $title;
      $this->description = $description;
      $this->picUrl = $picUrl;
      $this->url = $url;
      $this->template = <<<XML
<item>
  <title><![CDATA[%s]]></title>
  <description><![CDATA[%s]]></description>
  <picurl><![CDATA[%s]]></picurl>
  <url><![CDATA[%s]]></url>
</item>
XML;
    }

    public function __toString() {
      return sprintf($this->template,
        $this->title,
        $this->description,
		$this->picUrl,
        $this->url
      );
    }

  }