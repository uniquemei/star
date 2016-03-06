package com.fly.jiejing.entity;

import com.fly.jiejing.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/1.
 */
public class DataManager {

    private List<ImageAdvertisement> advertisementList;
  //  private List<Items> datas;
    private static DataManager dataManager=new DataManager();

    public static DataManager getDataManager() {
        return dataManager;
    }

    public DataManager() {
        advertisementList = new ArrayList<>();
        ImageAdvertisement advertisement = new ImageAdvertisement(R.mipmap.ad_top_1,"http://wap.1jiajie.com/trainAuntie1.html");
        advertisementList.add(advertisement);
        advertisement = new ImageAdvertisement(R.mipmap.ad_top_2,"http://wap.1jiajie.com/trainAuntie1.html");
        advertisementList.add(advertisement);
        advertisement = new ImageAdvertisement(R.mipmap.ad_top_3,"http://wap.1jiajie.com/trainAuntie1.html");
        advertisementList.add(advertisement);
        advertisement = new ImageAdvertisement(R.mipmap.ad_top_4,"http://wap.1jiajie.com/trainAuntie1.html");
        advertisementList.add(advertisement);
//        ImageAdvertisement advertisement = new ImageAdvertisement(R.mipmap.ad_top_1);
//        advertisementList.add(advertisement);
//        advertisement = new ImageAdvertisement(R.mipmap.ad_top_2);
//        advertisementList.add(advertisement);
//        advertisement = new ImageAdvertisement(R.mipmap.ad_top_3);
//        advertisementList.add(advertisement);
//        advertisement = new ImageAdvertisement(R.mipmap.ad_top_4);
//        advertisementList.add(advertisement);
//        advertisement = new ImageAdvertisement(R.mipmap.ad_top_5);
//        advertisementList.add(advertisement);
//
//        datas=new ArrayList<>();
//        Items item=new Items(0,1,"家庭保洁");//父，子，子的名字
//        datas.add(item);
//        item=new Items(0,2,"家电清洗");
//        datas.add(item);
//        item=new Items(0,3,"家居保养");
//        datas.add(item);
//        item=new Items(0,4,"洗护服务");
//        datas.add(item);
//        item=new Items(0,5,"水电维修");
//        datas.add(item);
//
//



    }


    public List<ImageAdvertisement> getAdvertisementList(){

        return advertisementList;
    }
//
//    public List<Items> getServeItem(){
//
//        return datas;
//    }
}
