<?php

class Request {

    public $Doc;  

    function __construct($xml) {
        $this->Doc = $xml;
    }

    public function getDoc() {
        return $this->Doc;
    }

    public function getFromUsername() {
        return $this->Doc->fromusername;
    }

    public function getMsgId() {
        return $this->Doc->msgid;
    }

    public function getToUsername() {
        return $this->Doc->tousername;
    }

    public function getCreateTime() {
        return $this->Doc->createtime;
    }

    public function getType() {
        return $this->Doc->msgtype;
    }

    public function getFromStudentNumber() {
        return $this->Doc->fromstudentnumber;
    }

}

class HelloRequest extends Request {

    function __construct($xml) {
        parent::__construct($xml);
    }

    public function getContent() {
        return $this->Doc->content;
    }

}

class TextRequest extends Request {

    function __construct($xml) {
        parent::__construct($xml);
    }

    public function getContent() {
        return $this->Doc->content;
    }

}

class VoiceRequest extends Request {

    function __construct($xml) {
        parent::__construct($xml);
    }

}

class ImageRequest extends Request {

    function __construct($xml) {
        parent::__construct($xml);
    }

    public function getPicUrl() {
        return $this->Doc->picurl;
    }

}

class LocationRequest extends Request {

    function __construct($xml) {
        parent::__construct($xml);
    }

    public function getLabel() {
        return $this->Doc->label;
    }

    public function getScale() {
        return $this->Doc->scale;
    }

    public function getLocation_X() {
        return $this->Doc->location_x;
    }

    public function getLocation_Y() {
        return $this->Doc->location_y;
    }

}

class LinkRequest extends Request {

    function __construct($xml) {
        parent::__construct($xml);
    }

    public function getTitle() {
        return $this->Doc->title;
    }

    public function getDescription() {
        return $this->Doc->description;
    }

    public function getUrl() {
        return $this->Doc->url;
    }

}

class EventRequest extends Request {

    function __construct($xml) {
        parent::__construct($xml);
    }

    public function getEvent() {
        return $this->Doc->event;
    }

    public function getEventKey() {
        return $this->Doc->eventkey;
    }

}

class UnknownRequest extends Request {

    function __construct($xml) {
        parent::__construct($xml);
    }

}
