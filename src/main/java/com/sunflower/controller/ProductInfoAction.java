package com.sunflower.controller;

import com.github.pagehelper.PageInfo;
import com.sunflower.pojo.ProductInfo;
import com.sunflower.pojo.vo.ProductInfoVo;
import com.sunflower.service.ProductInfoService;
import com.sunflower.utils.FileNameUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProductInfoAction {

    //每页显示的记录数
    public static final int PAGE_SIZE = 5;
    //异步上传的图片的名称
    public String saveFileName = "";

    //切记：在控制层必然会有业务逻辑层对象
    @Autowired
    ProductInfoService productInfoService;

    //显示全部商品不分页
    @RequestMapping("/getAll")
    public String getAll(HttpServletRequest request) {
        List<ProductInfo> list = productInfoService.getAll();
        request.setAttribute("list", list);

        //测试是否从数据库中获得该集合
//        Iterator<ProductInfo> iterator = list.iterator();
//        while(iterator.hasNext()){
//            System.out.println(iterator.next().getpName());
//        }

        return "p11roduct";
    }

    //显示第一页的五条记录
    @RequestMapping("/split")
    public String split(HttpServletRequest request) {
        //得到第一页的数据
        PageInfo info = productInfoService.splitPage(1,PAGE_SIZE);
        request.setAttribute("info",info);
        return "product";
    }

    //ajax分页翻页处理
    @ResponseBody      //可以解析ajax请求，还可以绕过视图解析器，如果有返回值的话，将当前的返回值转为响应的json格式，返回到客户端
    @RequestMapping("/ajaxSplit")
    public void ajaxSplit(int page,HttpSession session){
        //取得当前page参数的页面的数据
        PageInfo info = productInfoService.splitPage(page, PAGE_SIZE);
        session.setAttribute("info",info);
    }

    //异步AJAX文件上传至服务器处理
    @ResponseBody
    @RequestMapping("/ajaxImg")  //路径名要和addproduct.jsp中的JavaScript中的异步ajax图片文件名称相对应
    public Object ajaxImg(MultipartFile pimage, HttpServletRequest request){  //pimage：该参数专门用于当前的上传的文件流对象的接收，必须要和addProduct.jsp中name一样
        //1.提取生成文件名称 UUID+上传图片的后缀.jpg   .png      pimage.getOriginalFilename()--->该方法就是获取该上传的图片的原始名称的后缀部分
        saveFileName = FileNameUtil.getUUIDFileName()+FileNameUtil.getFileType(pimage.getOriginalFilename());

        //2.获取项目中图片存储的路径: 通过request对象获取当前项目指定的路径
        String path = request.getServletContext().getRealPath("/image_big");

        //3.转存 D:\developer\workspace_items\mimiMall\image_big\23fiernihfhoindsojgds.jpg
        try {
            pimage.transferTo(new File(path+File.separator+saveFileName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //返回客户端JSON对象，封装图片的路径为了在页面显示立即回显
        JSONObject object = new JSONObject();
        object.put("imaurl",saveFileName);

        return object.toString();
    }


    //完成新增商品功能
    @RequestMapping("/save")
    public String save(ProductInfo info,HttpServletRequest request){ //所有的对象封装在ProductInfo实体类中
        info.setpImage(saveFileName);
        info.setpDate(new Date());
        //info对象中有表单提交上来的五个数据，有异步Ajax上传上来的图片名称数据，有上架时间的数据
        int num = -1;
        try {
            num = productInfoService.save(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0){
            //增加成功
            request.setAttribute("msg","增加成功");
        }else{
            request.setAttribute("msg","增加失败");
        }
        //在新增商品的图片上传后，要清空saveFileName变量中的内容，为了下次增加或者修改的异步ajax的上传处理
        saveFileName = "";
        //增加成功后应当重新连接数据库，所以应当跳转到分页显示的action上
        return "forward:/prod/split.action";
    }


    //完成商品更新的第一步：将根据id查到的商品回显在页面上
    @RequestMapping("/one")
    public String one(int pid, ProductInfoVo vo, Model model, HttpSession session){
        //调用业务逻辑层，通过商品的pid实现商品信息的返回
        ProductInfo info = productInfoService.getById(pid);
        model.addAttribute("prod",info); //为了实现信息的回传显示，将信息放在通信作用域中。
        session.setAttribute("prodVo",vo);//将多条件及页码放在session域中，更新处理结束后分页时读取条件和页码进行处理
        return "update";
    }


    //完成商品更新的第二步：商品真正的修改，以及提交
    @RequestMapping("/update")
    public String update(ProductInfo info,HttpServletRequest request){
        //进行判断。因为ajax异步图片上传，
        // 如果有上传过，则saveFileName里就会有上传上来的图片的名称；
        // 如果没有使用异步ajax上传过图片，则saveFileName=""，实体类info使用隐藏表单域提交上来的pImage原始图片的名称;
        if(!saveFileName.equals("")) {
            //
            info.setpImage(saveFileName);
        }
        //完成更新处理
        int num = -1;
        try {
            num = productInfoService.update(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(num > 0){
            //此时说明更新成功
            request.setAttribute("msg","更新成功");
        }else{
            request.setAttribute("msg","更新失败");
        }
        //处理完更新后，saveFileName有可能有数据，而下一次更新时要使用这个变量作为判断的依据，就会出错，所以必须要清空saveFileName。
        saveFileName = "";
        return "forward:/prod/split.action";  //更新之后跳转到重新分页的页面上，进行数据的展示
    }


    //完成商品的删除功能
    @RequestMapping("/delete")
    //此处不能加ResponseBody注解，是因为此处的删除操作并没有结束，不能返回给客户端，执行完该处删除操作之后，需要跳转到删除的ajax分页显示上去，才能够彻底完成删除操作
    public String delete(int pid,HttpServletRequest request){
        int num = -1;
        try {
            num = productInfoService.delete(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0) {
            request.setAttribute("msg","删除成功");
        }else{
            request.setAttribute("msg","删除失败");
        }
        //删除结束后跳至删除的Ajax分页显示
        return "forward:/prod/deleteAjaxSplit.action";

    }

    //批量删除商品，删除之后还会像单个删除功能一样，会跳转到删除ajax分页上
    @RequestMapping("/deleteBatch")
    //pids="1,4,5" ps[1,4,5]
    public String deleteBatch(String pids,HttpServletRequest request){
        //将上传上来的字符串截开，形成商品id的字符数组
        String []ps = pids.split(",");

        try {
            int num = productInfoService.deleteBatch(ps);
            if(num > 0) {
                request.setAttribute("msg","批量删除成功！");
            }else {
                request.setAttribute("msg","批量删除失败！");
            }
        } catch (Exception e) {
            request.setAttribute("msg","当前商品不可删除");
        }

        return "forward:/prod/deleteAjaxSplit.action";

    }


    //删除的Ajax分页显示
    @ResponseBody   //ResponseBody注解，使用在控制层（controller）的方法上。
    // 作用:将方法的返回值，以特定的格式写入到response的body区域，进而将数据返回给客户端。
    // 当方法上面没有写ResponseBody,底层会将方法的返回值封装为ModelAndView对象。
    // 如果返回的是字符串，就直接将字符串返回到客户端；如果是一个对象，会将对象转化为json串，然后写到客户端。
    @RequestMapping(value = "/deleteAjaxSplit",produces = "text/html;charset=UTF-8")    //弹出框是要显示中文，所以需要设置编码
    public Object deleteAjaxSplit(HttpServletRequest request) {  //需要session的获取以及读取msg中的消息
        //重新读取数据库中第一页的数据
        PageInfo info = productInfoService.splitPage(1, PAGE_SIZE);
        request.getSession().setAttribute("info", info);//将商品信息放在session中，而在success:function中的加载在请求域中是加载不出来的，所以要使用session域
        return request.getAttribute("msg");  //将刚刚删除与否记录下的的msg返回至客户端，然后在客户端中的success：function(msg)中
    }


    //多条件查询功能实现
    @ResponseBody
    @RequestMapping("/condition")
    public void condition(ProductInfoVo vo,HttpSession session) {
        List<ProductInfo> list = productInfoService.selectCondition(vo);
        session.setAttribute("list",list);
//        Iterator<ProductInfo> iterator = list.iterator();
//        while (iterator.hasNext()){
//            System.out.println(iterator.next());
//        }
    }


}


