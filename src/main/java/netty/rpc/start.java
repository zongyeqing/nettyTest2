package netty.rpc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 请填写类注释
 *
 * @author 宗业清
 * @since 2018年03月23日
 */
public class start {

    public static void main(String[] args) {
        ApplicationContext context =  new ClassPathXmlApplicationContext("classpath:config.xml");
        SpringAutoWireTest sp = context.getBean(SpringAutoWireTest.class);
        sp.test();
    }
}
