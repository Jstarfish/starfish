package priv.starfish.mall.spring;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import priv.starfish.mall.service.MallService;

import javax.annotation.Resource;

/**
 * 测试ApplicationContext.getBean 获取bean
 * created by starfish on 2018-01-29 15:19
 */


public class SpringBeanTest {

    @Resource(name="mallService")
    private MallService mallService;

    @Test
    public void getBeanTest(){
        try {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:conf/applicationContext.xml");
        /*
        用类Class 控制针  小写名字没问题？？？？
        java.lang.NullPointerException
	at SpringBeanTest.getBeanTest(SpringBeanTest.java:25)

         */
            mallService = context.getBean(priv.starfish.mall.service.MallService.class);
            System.out.println(mallService);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
