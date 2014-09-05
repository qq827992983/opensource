<?php

require_once dirname(__DIR__) . '/request/request.php';

class Response {

    protected $request;
    private $fromUserName;
    private $msgType;
    private $toUserName;
    private $createTime;

    function __construct(Request $request = null) {
        $this->request = $request;
    }

    public function getType() {
        return $this->type;
    }

    public function getFromUserName() {
        return $this->fromUserName;
    }

    public function getToUserName() {
        return $this->toUserName;
    }

    public function getCreateTime() {
        return $this->createTime;
    }

    public function setType($type) {
        $this->msgtype = $type;
    }

    public function setFromUserName($username) {
        $this->fromUserName = $username;
    }

    public function setToUserName($username) {
        $this->toUserName = $username;
    }

    public function setCreateTime($createtiime) {
        $this->createTime = $createtiime;
    }

    protected function encode(SimpleXMLElement $doc) {
        $request = $this->request;
        try {
//            $this->appendCdataNode($doc, "msgid", $this->toCdata($request->getMsgId()));
            $this->appendCdataNode($doc, "fromusername", $request->getFromUsername());
            $this->appendCdataNode($doc, "tousername", $request->getToUsername());
            $this->appendDataNode($doc, "createtime", $request->getCreateTime());
        } catch (Exception $exc) {
            echo $exc->getTraceAsString();
        }
    }

    public function addCData(SimpleXMLElement $doc, $cdata_text) {
        $node = dom_import_simplexml($doc);
        $no = $node->ownerDocument;
        $node->appendChild($no->createCDATASection($cdata_text));
    }

    protected function appendCdataNode(SimpleXMLElement $parent, $name, $cdata) {
        try {
            $tmp = $parent->addChild($name);
            $this->addCData($tmp, $cdata);
        } catch (Exception $ex) {
            echo $ex->getTraceAsString();
        }
    }

    protected function appendDataNode(SimpleXMLElement $parent, $name, $cdata) {
        try {
            $tmp = $parent->addChild($name, $cdata);
        } catch (Exception $ex) {
            echo $ex->getTraceAsString();
        }
    }

    protected function appendNode(SimpleXMLElement $parent, $name) {
        try {
            if ($parent instanceof SimpleXMLElement) {
                $node = $parent->addChild($name);
                return $node;
            }
        } catch (Exception $ex) {
            echo $ex->getTraceAsString();
        }
    }

    public function writeTo() {
        $xml = '<xml></xml>';
        $doc = new SimpleXMLElement($xml);
        $this->encode($doc);
//        $doc->asXML('D:/doc.xml');
        exit($doc->asXML());
    }

}

class TextResponse extends Response {

    private $content;

    function __construct(Request $request = null) {
        parent::__construct($request);
    }

    public function getContent() {
        return $this->content;
    }

    public function setContent($content) {
        $this->content = $content;
    }

    protected function encode(SimpleXMLElement $doc) {
        $request = $this->request;
        try {
            parent::encode($doc);
            $this->appendCdataNode($doc, "msgtype", "text");
            $this->appendCdataNode($doc, "content", $this->getContent());
        } catch (Exception $exc) {
            echo $exc->getTraceAsString();
        }
    }

}

class MusicResponse extends Response {

    private $title;
    private $description;
    private $musicurl;
    private $hqmusicurl;

    function __construct($request) {
        parent::__construct($request);
    }

    public function getTitle() {
        return $this->title;
    }

    public function setTitle($content) {
        $this->title = $content;
    }

    public function getDescription() {
        return $this->description;
    }

    public function setDescription($description) {
        $this->description = $description;
    }

    public function getMusicUrl() {
        return $this->musicurl;
    }

    public function setMusicUrl($musicurl) {
        $this->musicurl = $musicurl;
    }

    public function getHQMusicUrl() {
        return $this->hqmusicurl;
    }

    public function setHQMusicUrl($hqmusicurl) {
        $this->hqmusicurl = $hqmusicurl;
    }

    protected function encode($doc) {
        $request = $this->request;
        try {
            if ($doc instanceof SimpleXMLElement) {
                $this->appendCdataNode($doc, "msgtype", "music");
                $node = $this->appendNode($doc, "music");
                $this->appendCdataNode($node, "title", $this->getTitle());
                $this->appendCdataNode($node, "description", $this->getDescription());
                $this->appendCdataNode($node, "musicurl", $this->getMusicUrl());
                $this->appendCdataNode($node, "hqmusicurl", $this->getHQMusicUrl());
            }
        } catch (Exception $exc) {
            echo $exc->getTraceAsString();
        }
    }

}

class Article {

    private $title;
    private $description;
    private $url;
    private $picurl;

    function __construct($title, $description, $picUrl, $url = null) {
        $this->title = $title;
        $this->description = $description;
        $this->url = $url;
        $this->picurl = $picUrl;
    }

    public function getTitle() {
        return $this->title;
    }

    public function setTitle($content) {
        $this->title = $content;
    }

    public function getDescription() {
        return $this->description;
    }

    public function getUrl() {
        return $this->url;
    }

    public function setDescription($description) {
        $this->description = $description;
    }

    public function setPicUrl($picurl) {
        $this->picurl = $picurl;
    }

    public function setUrl($url) {
        return $this->url = $url;
    }

    public function getPicUrl() {
        return $this->picurl;
    }

}

class ArticleResponse extends Response {

    public $articles;

    function __construct($request) {
        parent::__construct($request);
        $this->articles = array();
    }

    public function getArticles() {
        return $this->articles;
    }

    public function setArticles($articles) {
        $this->articles = $articles;
    }

    protected function encode(SimpleXMLElement $doc) {
        $request = $this->request;
        try {
            parent::encode($doc);
            $this->appendCdataNode($doc, "msgtype", "news");
            $this->appendCdataNode($doc, "articlecount", count($this->articles));
            $node = $this->appendNode($doc, "articles");
            for ($i = 0; $i < count($this->articles); $i++) {
                $node = $this->appendNode($node, "item");
                $this->appendCdataNode($node, "title", $this->articles[$i]->getTitle());
                $this->appendCdataNode($node, "description", $this->articles[$i]->getDescription());
                $this->appendCdataNode($node, "picurl", $this->articles[$i]->getPicUrl());
                $this->appendCdataNode($node, "url", $this->articles[$i]->getUrl());
            }
        } catch (Exception $exc) {
            echo $exc->getTraceAsString();
        }
    }

}

class PushTextMessage extends TextResponse {

    private $expirationtime;

    public function __construct() {
        parent::__construct(null);
    }

    public function getExpirationTime() {
        return $this->expirationtime;
    }

    public function setExpirationTime($expirationtime) {
        return $this->expirationtime = $expirationtime;
    }

    public function encode(SimpleXMLElement $doc) {
        try {
            $this->appendCdataNode($doc, "fromusername", parent::getFromUserName());
            $this->appendCdataNode($doc, "tousername", parent::getToUsername());
            $this->appendDataNode($doc, "createtime", parent::getCreateTime());
            $this->appendCdataNode($doc, "msgtype", "text");
            $this->appendCdataNode($doc, "content", parent::getContent());
            $this->appendDataNode($doc, "expirationtime", $this->getExpirationTime());
        } catch (Exception $exc) {
            echo $exc->getTraceAsString();
        }
    }

}

class PushArticleMessage extends ArticleResponse {

    private $expirationtime;
    private $articlearray;

    public function __construct() {
        parent::__construct(null);
    }

    public function getExpirationTime() {
        return $this->expirationtime;
    }

    public function setExpirationTime($expirationtime) {
        return $this->expirationtime = $expirationtime;
    }

    public function setArticleArray($articlearray) {
        $this->articlearray = $articlearray;
    }

    public function encode(SimpleXMLElement $doc) {
        try {
            $this->appendCdataNode($doc, "fromusername", parent::getFromUserName());
            $this->appendCdataNode($doc, "tousername", parent::getToUsername());
            $this->appendDataNode($doc, "createtime", parent::getCreateTime());
            $this->appendCdataNode($doc, "msgtype", "news");
            $this->appendDataNode($doc, "expirationtime", $this->getExpirationTime());
            $this->appendCdataNode($doc, "articlecount", count($this->articlearray));
            $node = $this->appendNode($doc, "articles");
            for ($i = 0; $i < count($this->articlearray); $i++) {
                $node = $this->appendNode($node, "item");
                $arr = $this->articlearray;
                $this->appendCdataNode($node, "title", $arr[$i]->getTitle());
                $this->appendCdataNode($node, "description", $arr[$i]->getDescription());
                $this->appendCdataNode($node, "picurl", $arr[$i]->getPicUrl());
                $this->appendCdataNode($node, "url", $arr[$i]->getUrl());
            }
        } catch (Exception $exc) {
            echo $exc->getTraceAsString();
        }
    }

}
