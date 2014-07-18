<?php

require 'request/request.php';
require 'response/response.php';

/**
 * 微哨开放平台 PHP SDK
 *
 */

/**
 * 微哨开放平台处理类
 */
abstract class Whistlechat {

    protected $appid;
    protected $appsecret;
    protected $apptoken;
    protected $accesstoken;

    /**
     * 初始化，判断此次请求是否为验证请求，并以数组形式保存
     *
     * @param string $appid 轻应用appid
     * @param string $appsecret 轻应用appsecret
     * @param string $apptoken 轻应用token
     */
    public function __construct($appid, $appsecret, $apptoken = NULL) {
        $this->appid = $appid;
        $this->appsecret = $appsecret;
        $this->apptoken = $apptoken;
    }

    private function valid() {
        if (!(isset($_GET['signature']) && isset($_GET['timestamp']) && isset($_GET['nonce']))) {
            return FALSE;
        }
        $echostr = $_GET['echostr'];
        $signature = $_GET['signature'];
        $timestamp = $_GET['timestamp'];
        $nonce = $_GET['nonce'];

        $signatureArray = array($this->apptoken, $timestamp, $nonce);
        sort($signatureArray, SORT_STRING);

        if (sha1(implode($signatureArray)) == $signature) {
            return $echostr;
        } else {
            return 'error';
        }
    }

    private function request($url, $data = null) {
        //$data = json_encode($data);
        error_log($url);
        error_log($data);
        $timeout = 65;
        // 构建请求句柄
        $curl = curl_init();
        curl_setopt($curl, CURLOPT_URL, $url);
        curl_setopt($curl, CURLOPT_HEADER, 0);
        curl_setopt($curl, CURLOPT_TIMEOUT, $timeout);
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);

        // 设置ssl
        if (strpos($url, 'https') !== false) {
            curl_setopt($curl, CURLOPT_SSLVERSION, 3);
            curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, 0);
            curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, 0);
        }

        if ($data && trim($data)) {
            curl_setopt($curl, CURLOPT_POST, 1);
            curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
        }

        // 设置header
//        if ($headers) {
//            curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
//        }
        // 获取返回数据
        $res = curl_exec($curl);

        // 如果执行过程中错误信息
        if (curl_errno($curl)) {
            return array('error' => curl_error($curl));
        }

        // 关闭请求
        curl_close($curl);
        return $res;
    }

    protected function doAuth($url) {
        try {
            if (stripos($url, 'https://') != 0) {
                throw new Exception('参数非法！非https请求！');
            }
            $sub_url = substr($url, 8);
            $end = stripos($sub_url, '/');
            if ($end < 0) {
                throw new Exception('非法的URL！');
            }

            $host = substr($url, 0, 8 + $end);

            if (strlen($host) <= 0) {
                throw new Exception('无效的主机名');
            }

            $auth_url = $host . "/cgi-bin/oauth2/access_token?grant_type=client_credentials&appid=" . $this->appid . "&appsecret=" . $this->appsecret;
            $json = $this->doUrl0('get', $auth_url);
            $to_arr = json_decode($json, true);
            $this->accesstoken = $to_arr['access_token'];
        } catch (Exception $exc) {
            echo $exc->getTraceAsString();
        }
        return true;
    }

    private function doUrl0($method, $url, $data = NULL) {

        if (strtolower($method) == 'get') {
            return $this->request($url);
        }

        if (strtolower($method) == 'post') {
            return $this->request($url, $data);
        }

        return 'false';
    }

    /**
     * 
     * @param string $method ‘get’或‘post’
     * @param string $url 请求的url
     * @param string $data 字符串格式数据
     * @return string josn字符串结果
     * @throws Exception 非法url
     */
    public function doUrl($method, $url, $data = NULL) {
        if (!$this->doAuth($url)) {
            exit('get accesstoken error!');
        }

        try {
            if (stripos($url, 'https://') != 0) {
                throw new Exception('参数非法！非https请求！');
            }
            if (stripos($url, '?') == FALSE) {
                if (($pos = strripos($url, '/')) !== FALSE && $pos == strlen($url) - strlen('/')) {
                    $url = substr($url, 0, strlen($url) - 1);
                }
                $url = $url . "?access_token=" . $this->accesstoken;
            } else {
                $url = $url . "&access_token=" . $this->accesstoken;
            }
            $url = $url . "&appid=" . $this->appid;

//            $arr_data = json_decode($data, true);
            $json_ret = $this->doUrl0($method, $url, $data);
            $arr_ret = json_decode($json_ret, true);
            if ($arr_ret["errcode"] == 403) {
                if (!doAuth($url)) {
                    exit('get accesstoken error!');
                }
//                $arr_data = json_decode($data, true);
                $json_ret = $this->doUrl0($method, $url, $data);
            }
        } catch (Exception $exc) {
            echo $exc->getTraceAsString();
        }

        return $json_ret;
    }

    /**
     *  Note:向指定用户推送文本消息
     * @param string $url 请求的url
     * @param string $username 接收者用户名
     * @param string $message 消息体
     * @param int $valid_time 有效期时间：单位：ms
     * @return string 执行结果
     */
    public function pushLightappMessage($url, $username, $message, $valid_time) {
        $textMessage = new PushTextMessage();
        $textMessage->setCreateTime(time());
        $textMessage->setExpirationTime($valid_time);
        $textMessage->setContent($message);
        $textMessage->setToUserName($username);
        $textMessage->setFromUserName($this->appid);
        $xml = new SimpleXMLElement('<xml> </xml>');
        $textMessage->encode($xml);
        $url = $url . '?type=user&from=' . $this->appid . '&to=' . $username;
        if (!$this->doAuth($url)) {
            exit('get accesstoken error!');
        }
        $url = $url . "&access_token=" . $this->accesstoken;
        return $this->doUrl0('post', $url, $xml->asXML());
    }

    /**
     *  Note:向订阅者推送文本广播消息
     * @param string $url 请求的url
     * @param string $message 消息体
     * @param int $valid_time 有效期时间：单位：ms
     * @return string 执行结果
     */
    public function broadcastLightappMessage($url, $message, $valid_time) {
        $textMessage = new PushTextMessage();
        $textMessage->setCreateTime(time());
        $textMessage->setExpirationTime($valid_time);
        $textMessage->setContent($message);
        $textMessage->setToUserName('');
        $textMessage->setFromUserName($this->appid);
        $xml = new SimpleXMLElement('<xml> </xml>');
        $textMessage->encode($xml);
        $url = $url . '?type=broadcast&from=' . $this->appid;
        if (!$this->doAuth($url)) {
            exit('get accesstoken error!');
        }
        $url = $url . "&access_token=" . $this->accesstoken;
        return $this->doUrl0('post', $url, $xml->asXML());
    }

    /**
     *  Note:向指定用户推送图文混排消息
     * @param string $url 请求的url
     * @param string $username 接收者用户名
     * @param string $articles 图文混排数据
     * @param int $valid_time 有效期时间：单位：ms
     * @return string 执行结果
     */
    public function pushLightappArticle($url, $username, $articles, $valid_time) {
        $articlesMessage = new PushArticleMessage();
        $articlesMessage->setCreateTime(time());
        $articlesMessage->setExpirationTime($valid_time);
        $articlesMessage->setToUserName($username);
        $articlesMessage->setFromUserName($this->appid);
        $articlesMessage->setArticleArray($articles);
        $xml = new SimpleXMLElement('<xml> </xml>');
        $articlesMessage->encode($xml);
        $url = $url . '?type=user&from=' . $this->appid . '&to=' . $username;
        if (!$this->doAuth($url)) {
            exit('get accesstoken error!');
        }
        $url = $url . "&access_token=" . $this->accesstoken;
        return $this->doUrl0('post', $url, $xml->asXML());
    }

    /**
     *  Note:向订阅者推送图文混排广播消息
     * @param string $url 请求地址
     * @param array $articles 图文混排数据
     * @param int $valid_time 有效期时间：单位：ms
     * @return string 执行结果
     */
    public function broadcastLightappArticle($url, $articles, $valid_time) {
        $articlesMessage = new PushArticleMessage();
        $articlesMessage->setCreateTime(time());
        $articlesMessage->setExpirationTime($valid_time);
        $articlesMessage->setToUserName('');
        $articlesMessage->setFromUserName($this->appid);
        $articlesMessage->setArticleArray($articles);
        $xml = new SimpleXMLElement('<xml> </xml>');
        $articlesMessage->encode($xml);
        $url = $url . '?type=broadcast&from=' . $this->appid;
        if (!$this->doAuth($url)) {
            exit('get accesstoken error!');
        }
        $url = $url . "&access_token=" . $this->accesstoken;
        return $this->doUrl0('post', $url, $xml->asXML());
    }

    /**
     *  Note:处理轻应用请求
     */
    public function process() {
        if ($_SERVER["REQUEST_METHOD"] == "GET") {
            exit($this->valid());
        }

        if ($_SERVER["REQUEST_METHOD"] == "POST") {
            if (!isset($GLOBALS['HTTP_RAW_POST_DATA'])) {
                exit('缺少数据');
            }

            $xml = simplexml_load_string($GLOBALS["HTTP_RAW_POST_DATA"], 'SimpleXMLElement', LIBXML_NOCDATA);
            $msgType = $xml->msgtype;
            try {
                switch ($msgType) {
                    case "hello":
                        $helloRequest = new HelloRequest($xml);
                        $helloResponse = $this->onRecvHelloMessage($helloRequest);
                        $helloResponse->writeTo();
                        break;
                    case "text":
                        $textRequest = new TextRequest($xml);
                        $textResponse = $this->onRecvTextMessage($textRequest);
                        $textResponse->writeTo();
                        break;
                    case "music":
                        $voiceRequest = new VoiceRequest($xml);
                        $voiceResponse = $this->onRecvVoiceMessage($voiceRequest);
                        $voiceResponse->writeTo();
                        break;
                    case "image":
                        $imageRequest = new ImageRequest($xml);
                        $imageResponse = $this->onRecvImageMessage($imageRequest);
                        $imageResponse->writeTo();
                        break;
                    case "location":
                        $locationRequest = new LocationRequest($xml);
                        $locationResponse = $this->onRecvLocationMessage($locationRequest);
                        $locationResponse->writeTo();
                        break;
                    case "link":
                        $urlRequest = new LinkRequest($xml);
                        $urlResponse = $this->onRecvLinkMessage($urlRequest);
                        $urlResponse->writeTo();
                        break;
                    case "event":
                        $eventRequest = new EventRequest($xml);
                        $eventResponse = $this->onRecvEventMessage($eventRequest);
                        $eventResponse->writeTo();
                        break;
                    default:

                        break;
                }
            } catch (Exception $ex) {
                exit($ex);
            }
        }
    }

    /**
     * Note:处理Hello报文,用户开发LightApp需要实现此方法 <br> <br>
     * 	protected abstract Response onRecvHelloMessage(HttpServletResponse response, HelloRequest request);
     * @param request 表示Hello请求报文
     * @return 响应对象
     */
    protected abstract function onRecvHelloMessage(HelloRequest $request);

    /**
     * Note:处理文本请求报文,用户开发LightApp需要实现此方法 <br> <br>
     * 	protected abstract Response onRecvTextMessage(HttpServletResponse response, TextRequest request);
     * @param request 表示文本请求报文
     * @return 响应对象
     */
    protected abstract function onRecvTextMessage(TextRequest $request);

    /**
     * Note:处理图片请求报文,用户开发LightApp需要实现此方法 <br> <br>
     * 	protected abstract Response onRecvImageMessage(HttpServletResponse response, ImageRequest request);
     * @param request 表示图片请求报文
     * @return 响应对象
     */
    protected abstract function onRecvImageMessage($request);

    /**
     * Note:处理音频请求报文,用户开发LightApp需要实现此方法 <br> <br>
     * 	protected abstract Response onRecvVoiceMessage(HttpServletResponse response, VoiceRequest request);
     * @param request 表示音频请求报文
     * @return 响应对象
     */
    protected abstract function onRecvVoiceMessage($request);

    /**
     * Note:处理事件请求报文,用户开发LightApp需要实现此方法 <br> <br>
     * 	protected abstract Response onRecvEventMessage(HttpServletResponse response, EventRequest request);
     * @param request 表示事件请求报文
     * @return 响应对象
     */
    protected abstract function onRecvEventMessage($request);

    /**
     * Note:处理位置请求报文,用户开发LightApp需要实现此方法 <br> <br>
     * 	protected abstract Response onRecvLocationMessage(HttpServletResponse response, LocationRequest request);
     * @param request 表示位置请求报文
     * @return 响应对象
     */
    protected abstract function onRecvLocationMessage($request);

    /**
     * Note:处理链接请求报文,用户开发LightApp需要实现此方法 <br> <br>
     * 	protected abstract Response onRecvHelloMessage(HttpServletResponse response, HelloRequest request);
     * @param request 表示链接请求报文
     * @return 响应对象
     */
    protected abstract function onRecvLinkMessage($request);

    /**
     * Note:处理未知请求报文,用户开发LightApp需要实现此方法 <br> <br>
     * 	protected abstract Response onRecvUnknowMessage(HttpServletResponse response, UnknowRequest request, String context);
     * @param request 表示未知报文
     * @param context 错误提示信息内容
     * @return 响应对象
     */
    protected abstract function onRecvUnknowMessage($request);
}
