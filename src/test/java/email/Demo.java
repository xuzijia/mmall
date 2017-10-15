package email;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**   
*    
* 项目名称：mmall   
* 类名称：Demo   
* 类描述：
* 创建人：xuzijia   
* 创建时间：2017年10月13日 上午11:54:45   
* 修改人：xuzijia   
* 修改时间：2017年10月13日 上午11:54:45   
* 修改备注：   
* @version 1.0
*    
*/
public class Demo {
	public static void main(String[] args) throws MessagingException {  
        Properties props = new Properties();  
        // 开启debug调试  
        props.setProperty("mail.debug", "true");  
        // 发送服务器需要身份验证  
        props.setProperty("mail.smtp.auth", "true");  
        // 设置邮件服务器主机名  
        props.setProperty("mail.host", "smtp.163.com");  
        // 发送邮件协议名称  
        props.setProperty("mail.transport.protocol", "smtp");  
          
        // 设置环境信息  
        Session session = Session.getInstance(props);  
          
        // 创建邮件对象  
        Message msg = new MimeMessage(session);  
        msg.setSubject("JavaMail测试");  
        // 设置邮件内容  
        msg.setText("这是一封由JavaMail发送的邮件！");  
        // 设置发件人  
        msg.setFrom(new InternetAddress("13049257683@163.com"));  
          
        Transport transport = session.getTransport();  
        // 连接邮件服务器  
        transport.connect("smtp.163.com","13049257683@163.com","xuzijia123");  
        // 发送邮件  
        transport.sendMessage(msg, new Address[] {new InternetAddress("2295443695@qq.com")});  
        // 关闭连接  
        transport.close();  
    }  
}
