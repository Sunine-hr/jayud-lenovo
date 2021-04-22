package com.jayud.mall.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.PromoteCompanyMapper;
import com.jayud.mall.model.bo.QueryPromoteCompanyForm;
import com.jayud.mall.model.bo.SavePromoteCompanyForm;
import com.jayud.mall.model.po.PromoteCompany;
import com.jayud.mall.model.vo.PromoteCompanyVO;
import com.jayud.mall.model.vo.PromoteOrderVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.IPromoteCompanyService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 推广公司表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-20
 */
@Service
public class PromoteCompanyServiceImpl extends ServiceImpl<PromoteCompanyMapper, PromoteCompany> implements IPromoteCompanyService {

    //文件服务访问前缀
    @Value("${file.absolute.path:}")
    private String fileAbsolutePath;

    @Value("${file.api.createQrCode:}")
    private String fileApiCreateQrCode;

    @Value("${front.access.address:}")
    private String frontAccessAddress;

    @Autowired
    PromoteCompanyMapper promoteCompanyMapper;

    @Autowired
    BaseService baseService;

    @Override
    public IPage<PromoteCompanyVO> findPromoteCompanyByPage(QueryPromoteCompanyForm form) {
        //定义分页参数
        Page<PromoteCompanyVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.asc("t.company_id"));
        IPage<PromoteCompanyVO> pageInfo = promoteCompanyMapper.findPromoteCompanyByPage(page, form);

        List<PromoteCompanyVO> records = pageInfo.getRecords();
        records.forEach(promoteCompanyVO -> {
            String qrCode = promoteCompanyVO.getQrCode();
            String qrCodeUrl = fileAbsolutePath + File.separator + qrCode;
            promoteCompanyVO.setQrCode(qrCodeUrl);
            Integer companyId = promoteCompanyVO.getCompanyId();
            Integer clientNumber = promoteCompanyMapper.selectClientNumber(companyId);
            promoteCompanyVO.setClientNumber(clientNumber);
        });

        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePromoteCompany(SavePromoteCompanyForm form) {
        AuthUser user = baseService.getUser();
        Integer companyId = form.getCompanyId();
        if(ObjectUtil.isEmpty(companyId)){
            //新增
            PromoteCompany promoteCompany = ConvertUtil.convert(form, PromoteCompany.class);
            promoteCompany.setCreateId(user.getId());
            promoteCompany.setCreateName(user.getName());
            this.saveOrUpdate(promoteCompany);

            Integer id = promoteCompany.getCompanyId();
            String h5address = frontAccessAddress + "?id=" + id;
            try {
                String relativePath = getRelativePath(h5address);
                promoteCompany.setQrCode(relativePath);
                this.saveOrUpdate(promoteCompany);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            PromoteCompanyVO promoteCompanyVO = promoteCompanyMapper.findPromoteCompanyByCompanyId(companyId);
            if(ObjectUtil.isEmpty(promoteCompanyVO)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "公司不存在");
            }
            //编辑
            PromoteCompany promoteCompany = ConvertUtil.convert(promoteCompanyVO, PromoteCompany.class);
            promoteCompany.setCompanyName(form.getCompanyName());
            promoteCompany.setContacts(form.getContacts());
            promoteCompany.setPhone(form.getPhone());
            promoteCompany.setCompanyAddress(form.getCompanyAddress());
            promoteCompany.setCreateId(user.getId());
            promoteCompany.setCreateName(user.getName());
            promoteCompany.setTitle(form.getTitle());
            this.saveOrUpdate(promoteCompany);
            Integer id = promoteCompany.getCompanyId();
            String h5address = frontAccessAddress + "?id=" + id;
            try {
                String relativePath = getRelativePath(h5address);
                promoteCompany.setQrCode(relativePath);
                this.saveOrUpdate(promoteCompany);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据h5的地址，生成二维码地址，保存到数据库
     * @param h5address h5地址
     * @return
     */
    public String getRelativePath(String h5address) throws IOException {
        String relativePath = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try{
            String uri = fileApiCreateQrCode;
            HttpPost httppost = new HttpPost(uri);

            //JAVA后台HTTP POST请求模拟表单FORM-DATA格式获取数据的方法
            StringBody url = new StringBody(h5address, ContentType.TEXT_PLAIN);
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("url", url)
                    .build();
            httppost.setEntity(reqEntity);
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String str = EntityUtils.toString(resEntity);
                    Map map = JSONUtil.toBean(str, Map.class);
                    Map data = MapUtil.get(map, "data", Map.class);//数据
                    relativePath = MapUtil.getStr(data, "relativePath");//相对路径
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return relativePath;
    }


    @Override
    public List<PromoteCompanyVO> findPromoteCompanyByParentId(Integer parentId) {
        return promoteCompanyMapper.findPromoteCompanyByParentId(parentId);
    }

    @Override
    public PromoteCompanyVO findPromoteCompanyByCompanyId(Integer companyId) {
        return promoteCompanyMapper.findPromoteCompanyByCompanyId(companyId);
    }

    @Override
    public List<PromoteCompanyVO> findPromoteCompanyParent() {
        return promoteCompanyMapper.findPromoteCompanyParent();
    }

    @Override
    public List<PromoteOrderVO> findPromoteOrderbyCompanyId(Integer companyId) {
        return promoteCompanyMapper.findPromoteOrderbyCompanyId(companyId);
    }
}
