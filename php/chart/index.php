<?php

/**
 * 微哨开放平台 PHP SDK 示例文件
 *
 */
require('src/Whistlechat.php');

/**
 * 微哨开放平台演示类
 */
class MyLightApp extends Whistlechat {

    /**
     * Note:处理Hello报文,用户开发LightApp需要实现此方法 <br> <br>
     * 	protected abstract Response onRecvHelloMessage(HttpServletResponse response, HelloRequest request);
     * @param request 表示Hello请求报文
     * @return 响应对象
     */
    protected function onRecvHelloMessage(HelloRequest $request) {
        $helloResponse = new TextResponse($request);
        $fromStudentNumber = $request->getFromStudentNumber();
        $url = 'https://172.16.56.105:8443/api/user/get_info?student_number=' . $fromStudentNumber;
        $content = $this->doUrl('GET', $url, NULL);

        $to_arr = json_decode($content, true);

        $content = '尊敬的:' . $to_arr['data']['name'] . ',您好!这是Hello报文,以下是您的基本信息:' . '姓名：' . $to_arr['data']['name'] . ',性别:' . $to_arr['data']['sex'] . ',学号：' . $to_arr['data']['student_number'] . ',组织架构：' . $to_arr['data']['organization'] . ',输入1返回图文消息, 输入2返回音乐消息,输入3返回文本信息输入1返回图文消息, 输入2返回音乐消息,输入3返回文本信息';
        $helloResponse->setContent($content);
        return $helloResponse;
    }

    /**
     * Note:处理文本请求报文,用户开发LightApp需要实现此方法 <br> <br>
     * 	protected abstract Response onRecvTextMessage(HttpServletResponse response, TextRequest request);
     * @param request 表示文本请求报文
     * @return 响应对象
     */
    protected function onRecvTextMessage(TextRequest $request) {
        $response = NULL;
        $content = $request->getContent();
        switch ($content) {
            case '1':
                $articles = new ArticleResponse($request);
                $arr = array();
                $arr[] = new Article("这是图文消息", "不要点我", "http://www.weishao.com.cn/upload/image/1385434253.png", "");
                $arr[] = new Article("第一点", "点我跳到微哨", "http://www.weishao.com.cn/upload/image/1385434408.png", "http://www.weishao.com.cn");
                $arr[] = new Article("第二点", "点我跳到百度", "http://www.weishao.com.cn/upload/image/1385693782.png", "http://www.baidu.com");
                $articles->setArticles($arr);
                $response = $articles;
                break;
            case '2':
                $music = new MusicResponse($request);
                $music->setTitle("忐忑");
                $music->setDescription("这是一首奇妙的乐曲");
                $music->setMusicUrl("http://www.baidu.com/mp3/1231233.mp3");
                $music->setHQMusicUrl("http://www.baidu.com/mp3/hq1231233.mp3");
                $response = $music;
                break;
            case '3':
                $text = new TextResponse($request);
                $textContent = "您好,按键3的功能就留给您实现吧!";
                $text->setContent($textContent);
                $response = $text;
                break;
            default;
                $help = new TextResponse($request);
                $helpContent = "您好,这是文本消息, 输入1返回图文消息, 输入2返回音乐消息,输入3返回文本信息, 输入其他内容, 显示本消息!";
                $help->setContent($helpContent);
                $response = $help;
                break;
        }
        return $response;
    }

    /**
     * Note:处理图片请求报文,用户开发LightApp需要实现此方法 <br> <br>
     * 	protected abstract Response onRecvImageMessage(HttpServletResponse response, ImageRequest request);
     * @param request 表示图片请求报文
     * @return 响应对象
     */
    protected function onRecvImageMessage($request) {
        $textResponse = new TextResponse($request);
        $content = '这是图片消息';
        $textResponse->setContent($content);
        return $textResponse;
    }

    /**
     * Note:处理音频请求报文,用户开发LightApp需要实现此方法 <br> <br>
     * 	protected abstract Response onRecvVoiceMessage(HttpServletResponse response, VoiceRequest request);
     * @param request 表示音频请求报文
     * @return 响应对象
     */
    protected function onRecvVoiceMessage($request) {
        $textResponse = new TextResponse($request);
        $content = '这是音频';
        $textResponse->setContent($content);
        return $textResponse;
    }

    /**
     * Note:处理事件请求报文,用户开发LightApp需要实现此方法 <br> <br>
     * 	protected abstract Response onRecvEventMessage(HttpServletResponse response, EventRequest request);
     * @param request 表示事件请求报文
     * @return 响应对象
     */
    protected function onRecvEventMessage($request) {
        $textResponse = new TextResponse($request);
        $content = '这是事件消息';
        $textResponse->setContent($content);
        return $textResponse;
    }

    /**
     * Note:处理位置请求报文,用户开发LightApp需要实现此方法 <br> <br>
     * 	protected abstract Response onRecvLocationMessage(HttpServletResponse response, LocationRequest request);
     * @param request 表示位置请求报文
     * @return 响应对象
     */
    protected function onRecvLocationMessage($request) {
        $textResponse = new TextResponse($request);
        $content = '这是位置消息';
        $textResponse->setContent($content);
        return $textResponse;
    }

    /**
     * Note:处理链接请求报文,用户开发LightApp需要实现此方法 <br> <br>
     * 	protected abstract Response onRecvHelloMessage(HttpServletResponse response, HelloRequest request);
     * @param request 表示链接请求报文
     * @return 响应对象
     */
    protected function onRecvLinkMessage($request) {
        $textResponse = new TextResponse($request);
        $content = '这是连接消息';
        $textResponse->setContent($content);
        return $textResponse;
    }

    /**
     * Note:处理未知请求报文,用户开发LightApp需要实现此方法 <br> <br>
     * 	protected abstract Response onRecvUnknowMessage(HttpServletResponse response, UnknowRequest request, String context);
     * @param request 表示未知报文
     * @param context 错误提示信息内容
     * @return 响应对象
     */
    protected function onRecvUnknowMessage($request) {
        $textResponse = new TextResponse($request);
        $content = '这是未知消息，请检查消息类型';
        $textResponse->setContent($content);
        return $textResponse;
    }

} 

{
    //此段代码为测试接入管理系统发送应用消息和应用通知代码
    $join_appid = '1gdmtrzdjos95zuc4x50a'; //接入管理的appid
    $join_appsecret = 'mjc5odvmm2e3ztkwyzdhzgu1odc5zje1mgvlzddhnjk3yzhiymziza'; //接入管理的appsecret
    $join_app = new MyLightApp($join_appid, $join_appsecret);
    $message = "{\"recipient\":{\"certType\":\"4\",\"certNum\":\"lijian\"},\"message\":{\"html\":\"测试\",\"hyperlink\":\"www.baidu.com\"}}";
    $join_app->doUrl('POST', 'https://172.16.56.105:8443/api/utils/send_message', $message);
    $notify = "{\"recipient\": {\"identities\": [\"teacher\",\"student\", \"other\"]},\"message\": {\"title\": \"test\",\"priority\": \"normal\",\"expired_time\": \"2014-08-04 12:00:00\",\"hyperlink\": \"http://www.baidu.com\",\"html\":\"sorry\"}}";
    $join_app->doUrl('POST', 'https://172.16.56.105:8443/api/utils/send_notification', $notify);
} 

{
    //此段代码为轻应用SDK实例代码
    $apptoken = 'test'; //轻应用的token
    $appid = 'h5qp1ebxwpjff12qfjhkjq'; //轻应用的appid
    $appsecret = 'ota1otqzztnkm2izztzmzjcwmmjhngviymnlmdhiowe5yji4njq2nw'; //轻应用的appsecret
    $app = new MyLightApp($appid, $appsecret, $apptoken);
    $username = '41771@luye.ruijie.com.cn';
    $valid_time = 3600000000;
    $url = 'https://172.16.56.105:8443/lightapp/push';
    $app->pushLightappMessage($url, $username, '主动推送消息给41771@luye.ruijie.com.cn！', $valid_time);
    $app->broadcastLightappMessage($url, '主动推送消息给所有人！', $valid_time);


    $articles = array();
    $articles[] = new Article("这是发送给41771@luye.ruijie.com.cn图文消息", "不要点我", "http://www.weishao.com.cn/upload/image/1385434253.png", "");
    $articles[] = new Article("第一点", "点我跳到微哨", "http://www.weishao.com.cn/upload/image/1385434408.png", "http://www.weishao.com.cn");
    $app->pushLightappArticle($url, $username, $articles, $valid_time);
    unset($articles);
    $articles = array();
    $articles[] = new Article("这是推送给所有人的图文消息", "不要点我", "http://www.weishao.com.cn/upload/image/1385434253.png", "");
    $articles[] = new Article("第一点", "点我跳到百度", "http://www.weishao.com.cn/upload/image/1385693782.png", "http://www.baidu.com");
    $app->broadcastLightappArticle($url, $articles, $valid_time);


    $app->process();
}