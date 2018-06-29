package com.developer.hrg.noralsalehin.Helps;

/**
 * Created by hamid on 6/6/2018.
 */

public class Config {
    public static final  String ONLINE_URL = "http://keepwords.ir/noor/v1/";
    public static final  String OFFLINE_URL = "http://192.168.1.147/noor/v1/";

    public static final String CHANEL_THUMB_BASE_OFFLINE = "http://192.168.1.147/noor/uploads/chanel_thumb/";
    public static final String CHANEL_THUMB_BASE_ONLINE = "http://keepwords.ir/noor/uploads/chanel_thumb/";

    public static final String CHANEL_PIC_BASE_OFFLINE = "http://192.168.1.147/noor/uploads/chanel_pics/";
    public static final String CHANEL__PIC_BASE_ONLINE = "http://keepwords.ir/noor/uploads/chanel_pics/";

    public static final String MESSAGE_PIC_ADDRES = "http://192.168.1.147/noor/uploads/message/pic/";
    public static final String MESSAGE_THUMB_ADDRESS = "http://192.168.1.147/noor/uploads/message/pic_thumb/";


    public static final String TOPIC_GLOBAL = "global";
    public static final String PUSH_NOTIFICATION = "pushNotification";



    public static final String PUSH_NEW_CHANEL="newChanel";
    public static final String PUSH_NEW_MESSAGE = "newMessage";

    public static final int PUSH_NEW_CHANEL_FLAG = 1;
    public static final int PUSH_NEW_MESSAGE_FLAG = 2;

    public static final String TOPIC_CHANEL = "chanel_";
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;



   public class Folders  {
       public static final String IMAGES = "Images";
       public static final String AUDIOS = "Auidos";
       public static final String VIDEOS = "Videos";
       public static final String DOCUMENTS = "Documnets";
   }
}
