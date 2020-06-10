package com.example.demo.send_file;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
public class File2Server {
    private static final String BOUNDARY = "-------45962402127348";
    private static final String FILE_ENCTYPE = "multipart/form-data";
    private  void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream (file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }
    public void dd(String name,MultipartFile file){
        String urlStr = "http://121.89.166.24/up_photo";
        Map<String, String> textMap = new HashMap<String, String>();
        textMap.put("fname", name);
//        textMap.put("aaa", "D:\\server.zip");
//        textMap.put("bbbb", "<?xml version='1.0' encoding='utf-8'?><root><parts><part number='设计Name' name='设计ShortDescription'  projectName='项目名'  version='设计版本' owner='设计创建人' type='设计类型' domain='所属域Name' status='设计状态'></part><part number='设计Name' name='设计ShortDescription'  projectName='项目名'  version='设计版本' owner='设计创建人' type='设计类型' domain='所属域Name' status='设计状态'></part><part number='设计Name' name='设计ShortDescription'  projectName='项目名'  version='设计版本' owner='设计创建人' type='设计类型' domain='所属域Name' status='设计状态'></part></parts></root>");
        Map<String, File> fileMap = new HashMap<String, File>();
        try {
            fileMap.put("photo", multipartFileToFile (file));
        } catch (Exception e) {
            e.printStackTrace ();
        }
        post(urlStr, textMap, fileMap);
    }
    public static void main(String[] args) {
        String filepath = "D:\\360安全浏览器下载\\plsqldev_2990.zip";
        String urlStr = "http://127.0.0.1/up_photo";
        Map<String, String> textMap = new HashMap<String, String>();
        textMap.put("fname", "1234");
        textMap.put("aaa", "D:\\server.zip");
        textMap.put("bbbb", "<?xml version='1.0' encoding='utf-8'?><root><parts><part number='设计Name' name='设计ShortDescription'  projectName='项目名'  version='设计版本' owner='设计创建人' type='设计类型' domain='所属域Name' status='设计状态'></part><part number='设计Name' name='设计ShortDescription'  projectName='项目名'  version='设计版本' owner='设计创建人' type='设计类型' domain='所属域Name' status='设计状态'></part><part number='设计Name' name='设计ShortDescription'  projectName='项目名'  version='设计版本' owner='设计创建人' type='设计类型' domain='所属域Name' status='设计状态'></part></parts></root>");
        Map<String, File> fileMap = new HashMap<String, File>();
        fileMap.put("photo", new File("C:\\Users\\liuya\\Pictures\\1.png"));
        post(urlStr, textMap, fileMap);

    }
    /**
     *
     * @param urlStr http请求路径
     * @param params 请求参数
     * @param images 上传文件
     * @return
     */
    public static InputStream post(String urlStr, Map<String, String> params,
                                   Map<String, File> images) {
        InputStream is = null;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setConnectTimeout(5000);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type", FILE_ENCTYPE + "; boundary="
                    + BOUNDARY);

            StringBuilder sb = null;
            DataOutputStream dos = new DataOutputStream(con.getOutputStream());;
            if (params != null) {
                sb = new StringBuilder();
                for (String s : params.keySet()) {
                    sb.append("--");
                    sb.append(BOUNDARY);
                    sb.append("\r\n");
                    sb.append("Content-Disposition: form-data; name=\"");
                    sb.append(s);
                    sb.append("\"\r\n\r\n");
                    sb.append(params.get(s));
                    sb.append("\r\n");
                }

                dos.write(sb.toString().getBytes());
            }

            if (images != null) {
                for (String s : images.keySet()) {
                    File f = images.get(s);
                    sb = new StringBuilder();
                    sb.append("--");
                    sb.append(BOUNDARY);
                    sb.append("\r\n");
                    sb.append("Content-Disposition: form-data; name=\"");
                    sb.append(s);
                    sb.append("\"; filename=\"");
                    sb.append(f.getName());
                    sb.append("\"\r\n");
                    sb.append("Content-Type: image/png");//这里注意！如果上传的不是图片，要在这里改文件格式，比如txt文件，这里应该是text/plain
                    sb.append("\r\n\r\n");
                    dos.write(sb.toString().getBytes());

                    FileInputStream fis = new FileInputStream(f);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = fis.read(buffer)) != -1) {
                        dos.write(buffer, 0, len);
                    }
                    dos.write("\r\n".getBytes());
                    fis.close();
                }

                sb = new StringBuilder();
                sb.append("--");
                sb.append(BOUNDARY);
                sb.append("--\r\n");
                dos.write(sb.toString().getBytes());
            }
            dos.flush();

            if (con.getResponseCode() == 200)
                is = con.getInputStream();

            dos.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }
}