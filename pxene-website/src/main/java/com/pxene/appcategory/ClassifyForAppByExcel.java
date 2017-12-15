package com.pxene.appcategory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pxene.utils.StringUtils;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class ClassifyForAppByExcel {
	
	public static void main(String[] args) {
		ApplicationContext ioc=new ClassPathXmlApplicationContext("/conf/spring-mybatis.xml");
		ClassifyForApp classifyForApp = ioc.getBean(ClassifyForApp.class);
		jxl.Workbook readwb = null;  
        try    
        {   
            //构建Workbook对象, 只读Workbook对象   
            //直接从本地文件创建Workbook   
            InputStream instream = new FileInputStream("D:/test/applist (3).xls");   
            readwb = Workbook.getWorkbook(instream); 
            int numberOfSheets = readwb.getNumberOfSheets();
            System.out.println(numberOfSheets);
            for(int k=0;k<=5;k++){
            	 //Sheet的下标是从0开始   
                //获取第一张Sheet表   
                Sheet readsheet = readwb.getSheet(k);   
                //获取Sheet表中所包含的总列数   
                int rsColumns = readsheet.getColumns();   
                //获取Sheet表中所包含的总行数   
                int rsRows = readsheet.getRows();  
                //获取指定单元格的对象引用   
                for (int i = 0; i < rsRows; i++)   
                {   
                    for (int j = 0; j < rsColumns; j++)   
                    {   
                        Cell cell = readsheet.getCell(j, i);  
                        String appName=cell.getContents().toString();
                        System.out.println(appName);
                        if(StringUtils.removePunctAndSpace(appName).equals("")){
                        	continue;
                        }
                        String appType = classifyForApp.classifyApp(appName);
                       // String classifyApp = sendPost("",appname);
                        System.out.print("App名称: "+cell.getContents().toString() + " App类型:"+": "+appType);
                        
                    }   
                    System.out.println();   
                }  
            }
        } catch (Exception e) {   
            e.printStackTrace();   
        } finally {   
            readwb.close();   
        }   
	}
	
	 /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        url="http://localhost:8080/pxene-website-info/appInfo/classifyApp/"+param+".do";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            System.out.println(conn);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            InputStream inputStream = conn.getInputStream();
            System.out.println(inputStream);
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
}
