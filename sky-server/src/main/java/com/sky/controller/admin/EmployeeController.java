package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.properties.RedisProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.utils.RedisUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@Api(tags = "员工管理相关接口")
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisProperties redisProperties;
    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        redisUtil.set(token, employeeLoginVO.getId(),redisProperties.getLOGIN_TTL());
        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("退出")
    public Result logout(@RequestHeader("token") String token) {
        // 删除redis中的令牌
        redisUtil.del(token);
        return Result.success();
    }

    // 添加员工
    @PostMapping
    @ApiOperation("添加员工")
    public Result add(@RequestBody EmployeeDTO employeeDTO) {
        log.info("添加员工：{}", employeeDTO);
        employeeService.add(employeeDTO);
        return Result.success();
    }

    // 员工分页查询(页码,记录数,员工姓名)
    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询：{}", employeePageQueryDTO);
        PageResult pageResult = employeeService.page(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    //启用、禁用员工账号
    @PostMapping("/status/{status}")
    @ApiOperation("启用、禁用员工账号")
    public Result status(@RequestParam Long id, @PathVariable Integer status) {
        log.info("启用、禁用员工账号：id={},status={}", id, status);
        employeeService.status(id, status);
        return Result.success();
    }

    //根据id查询员工
    @GetMapping("{id}")
    @ApiOperation("根据id查询员工")
    public Result<Employee> get(@PathVariable Long id){
        log.info("根据id查询员工：id={}", id);
        Employee employee = employeeService.get(id);
        return Result.success(employee);
    }

    //修改员工
    @PutMapping
    @ApiOperation("修改员工")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("修改员工：{}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }
}
