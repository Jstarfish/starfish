package priv.starfish.mall.manager.base;

import org.apache.log4j.Logger;
import priv.starfish.mall.service.CmbService;

import javax.annotation.Resource;


public class SpringQuartz {
    public static Logger log = Logger.getLogger(SpringQuartz.class);


    @Resource
    CmbService cmbService;

    public void merchantSettleDay() {

        try {
            log.info("**** 开始生成结算信息 ****");
            cmbService.createSettleInfo1();
            log.info("**** 生成结算信息结束 ****");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void merchantsSettleTotal() {

        try {
            log.info("**** 开始进行周期结算****");
            cmbService.settleDo();
            log.info("**** 周期结算结束 ****");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



}
