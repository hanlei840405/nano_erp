package com.nano.web.controller.system;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nano.domain.system.Function;
import com.nano.domain.system.Menu;
import com.nano.domain.system.User;
import com.nano.service.system.FunctionService;
import com.nano.service.system.MenuService;
import com.nano.service.system.UserService;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.system.FunctionVO;

/**
 * Created by Administrator on 2015/6/20.
 */
@Controller
@RequestMapping("system/function")
public class FunctionController {
    private static final Logger logger = LoggerFactory.getLogger(FunctionController.class);

    @Autowired
    private FunctionService functionService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/init", method = {RequestMethod.POST, RequestMethod.GET})
    public String init() {
        return "system/function/init";
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseVO<FunctionVO> save(FunctionVO vo) {
        ResponseVO<FunctionVO> responseVO = new ResponseVO<>();
        Function function;
        try{
			if (!StringUtils.isEmpty(vo.getId())) {
                function = functionService.get(vo.getId());
            }else{
                function = new Function();
            }
            BeanUtils.copyProperties(vo, function);
            User creator = userService.get(SecurityUtils.getSubject()
                    .getPrincipal().toString());
            function.setCreator(creator.getId());
			if (!StringUtils.isEmpty(vo.getId())) {
                functionService.update(function);
            }else{
                functionService.save(function);
            }
            responseVO.setYesOrNo(true);
            return responseVO;
        }catch (Exception e){
            logger.error(Marker.ANY_MARKER, e.getMessage());
            responseVO.setYesOrNo(false);
            return responseVO;
        }
    }

    @RequestMapping(value = "/queryFunctions", method = RequestMethod.POST)
    public
    @ResponseBody
    List<FunctionVO> queryFunctions(String menuId) {
        List<FunctionVO> vos = new ArrayList<>();
        Menu menu = menuService.get(menuId);
        List<Function> functions = functionService.queryFunctionsByMenu(menuId);
        for(Function function : functions){
            FunctionVO vo = new FunctionVO();
            BeanUtils.copyProperties(function,vo);
            vo.setMenuId(menu.getId());
            vo.setMenuName(menu.getName());
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseVO<FunctionVO> delete(String id) {
        ResponseVO<FunctionVO> responseVO = new ResponseVO<FunctionVO>();
        try {
            functionService.delete(id);
            responseVO.setYesOrNo(true);
            return responseVO;
        } catch (Exception e) {
            logger.error(Marker.ANY_MARKER, e.getMessage());
            responseVO.setYesOrNo(true);
            responseVO.setErrorMsg(e.getMessage());
            return responseVO;
        }
    }
}
