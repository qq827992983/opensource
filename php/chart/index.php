<?php
/**
 * 微哨开放平台 PHP SDK 示例文件
 *
 */

  require('src/Whistlechat.php');

  /**
   * 微哨开放平台演示类
   */
  class MyWhistlechat extends Whistlechat {

    /**
     * 收到hello报文消息时触发，用户打开应用时会触发此消息
     *
     * @return void
     */
    protected function onHello() {
	  $this->responseText('收到了hello报文消息：' . $this->getRequest('helloid'));
      
    }

    /**
     * 收到文本消息时触发，回复收到的文本消息内容
     *
     * @return void
     */
    protected function onText() {
	  $this->responseText('收到了文本消息：' . $this->getRequest('content'));
      
    }

    /**
     * 收到图片消息时触发，回复由收到的图片组成的图文消息
     *
     * @return void
     */
    protected function onImage() {
      $items = array(
        new NewsResponseItem('标题一', '描述一', $this->getRequest('picurl'), $this->getRequest('picurl')),
        new NewsResponseItem('标题二', '描述二', $this->getRequest('picurl'), $this->getRequest('picurl')),
      );

      $this->responseNews($items);
    }

	/**
     * 收到声音消息时触发
     *
     * @return void
     */
	protected function onVoice() {

      $this->responseText('收到了声音消息！');
    }

    /**
     * 收到地理位置消息时触发，回复收到的地理位置
     *
     * @return void
     */
    protected function onLocation() {
      $num = 1 / 0;
      // 故意触发错误，用于演示调试功能

      $this->responseText('收到了位置消息：' . $this->getRequest('location_x') . ',' . $this->getRequest('location_y'));
    }

	/**
     * 收到菜单消息时触发
     *
     * @return void
     */
    protected function onEvent() {
	  $this->responseText('收到了菜单事件：'. $this->getRequest('eventkey'));
    }
    /**
     * 收到链接消息时触发，回复收到的链接地址
     *
     * @return void
     */
    protected function onLink() {
      $this->responseText('收到了链接：' . $this->getRequest('url'));
    }

    /**
     * 收到未知类型消息时触发，回复收到的消息类型
     *
     * @return void
     */
    protected function onUnknown() {
      $this->responseText('收到了未知类型消息：' . $this->getRequest('msgtype'));
    }

  }

  $Whistlechat = new MyWhistlechat(/*轻应用通讯凭证*/, TRUE);
  //轻应用通讯凭证是在登陆微哨开放平台后，创建应用时开发者自己填写的内容，必须保证代码中的轻应用通讯凭证与开放平台的一致。并且限制为英文字母和阿拉伯数字，长度限制为0-32位。

  $Whistlechat->run();
