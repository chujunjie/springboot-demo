package com.example.springbootdemo;

import com.example.springbootdemo.base.bean.Article;
import com.example.springbootdemo.base.bean.Employee;
import com.example.springbootdemo.base.mapper.EmployeeMapper;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootDemoApplicationTests {

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    AmqpAdmin amqpAdmin;
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    JestClient jestClient;
    @Autowired
    JavaMailSenderImpl mailSender;

    /**
     * RabbitMQ
     */
    @Test
    public void create(){
        //创建exchange
        amqpAdmin.declareExchange(new DirectExchange("amqpAdmin.exchange"));
        //创建queue
        amqpAdmin.declareQueue(new Queue("amqpAdmin.queue",true));
        //设置绑定关系
        amqpAdmin.declareBinding(new Binding("amqpAdmin.queue",Binding.DestinationType.QUEUE,"amqpAdmin.exchange","amqp.haha",null));
    }
    @Test
    public void contextLoads(){
        /**
         * rabbitTemplate.send(exchange,routeKey,message);
         * message自定义构造；定义消息内容和消息头，*exchange选定交换器规则
         * rabbitTemplate.convertAndSend(exchange,routeKey,object);
         * object默认当成消息体，自动序列化对象给RabbitMQ
         * rabbitTemplate.receiveAndConvert("queueName");
         * 队列名，自动反序列化
         */
        Employee emp = employeeMapper.getEmpById(1);
        rabbitTemplate.convertAndSend("exchange.direct","atguigu.news",emp);
    }

    @Test
    public void contextLoads2(){
        Object o = rabbitTemplate.receiveAndConvert("atguigu.news");
        System.out.println(o.getClass());
        System.out.println(o.toString());
    }


    /**
     * ElasticSearch
     */
    //插入数据
    @Test
    public void esTest(){
        Article article = new Article();
        article.setId(1);
        article.setAuthor("chujunjie");
        article.setTitle("shijiebei");
        article.setContext("hello world");
        //构建一个索引
        Index index = new Index.Builder(article).index("atguigu").type("news").build();
        try {
            //执行该索引
            jestClient.execute(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //查询数据
    @Test
    public void esTest2(){
        //查询表达式
        String json = "{\n" +
                "\t\"query\" : {\n" +
                "\t\t\"match\" : {\n" +
                "\t\t\t\"context\" : \"hello\" \n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";
        //创建查询条件
        Search search = new Search.Builder(json).addIndex("atguigu").addType("news").build();
        try {
            //执行查询
            SearchResult result = jestClient.execute(search);
            System.out.println(result.getJsonString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 邮件任务
     */
    //简单邮件
    @Test
    public void emailTest(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("通知");
        message.setText("今晚7点开会");
        message.setTo("c.junjie@hotmail.com");
        message.setFrom("chujunjie1995@qq.com");
        mailSender.send(message);
    }
    //带附件
    @Test
    public void emailTest2() throws Exception{
        //1.创建复杂消息邮件
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //2.邮件设置
        helper.setSubject("通知");
        helper.setText("<b style='color:red'>今晚7点开会</b>",true);
        helper.addAttachment("模板.xlsx",new File("C:\\Users\\lenovo\\Desktop\\资产导入模板.xlsx"));
        helper.setTo("c.junjie@hotmail.com");
        helper.setFrom("chujunjie1995@qq.com");
        mailSender.send(message);
    }

}
