package ${apiControllerPackage};

import ${basePackage}.entity.${nameUpper};
import ${servicePackage}.${nameUpper}Service;
import ${basePackage}.utils.JsonUtils;
import ${basePackage}.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Validated
@RestController
@Api(tags = "后台-${nameZh}相关接口")
@RequestMapping("/api/admin/${nameLower}/")
public class ${nameUpper}Controller {
    private final ${nameUpper}Service service;

    @Autowired
    public ${nameUpper}Controller(${nameUpper}Service service) {
        this.service = service;
    }

    @ApiOperation("添加记录")
    @PostMapping("/add")
    public Result add(@RequestBody @NotNull(message = "参数不能为null") ${nameUpper} record) {
        log.info("添加:" + JsonUtils.object2json(record));
        return service.save(record);
    }

    @ApiOperation("根据记录id删除记录")
    @PostMapping("/delete")
    public Result delete(@RequestParam @Min(value = 1, message = "id参数异常") Long id) {
        log.info("删除id:" + id);
        return service.deleteById(id);
    }

    @ApiOperation("根据记录id批量删除记录")
    @PostMapping("/delete/batch")
    public Result deleteByIds(@RequestParam @ApiParam("多个id的集合") List<Long> ids) {
        log.info("批量删除ids:" + JsonUtils.object2json(ids));
        return service.deleteByIds(ids);
    }

    @ApiOperation("修改记录")
    @PostMapping("/update")
    public Result update(@RequestBody @NotNull(message = "参数不能为null") ${nameUpper} record) {
        log.info("修改记录:" + JsonUtils.object2json(record));
        return service.update(record);
    }

    @ApiOperation("根据id获得单条记录")
    @PostMapping("/findById")
    public Result findById(@RequestParam @Min(value = 1, message = "id参数异常") Long id) {
        log.info("查询id等于:" + id + "的记录");
        return service.findById(id);
    }

    @ApiOperation("根据id批量查找记录")
    @PostMapping("/find/batch")
    public Result findByIds(@RequestParam @ApiParam("多个id的集合") List<Long> ids) {
        log.info("批量查询ids:" + JsonUtils.object2json(ids));
        return service.findByIds(ids);
    }

    @ApiOperation("获取记录列表")
    @GetMapping("/list")
    public Result list(@RequestParam("page") @Range(min = 1, max = 999, message = "页码错误") Integer page,
                       @RequestParam(value = "keyword", required = false) @ApiParam("搜索关键字") String keyword) {
        log.info("查询列表，page等于" + page + " ,关键字等于" + keyword);
        return service.find(page, keyword);
    }
}
