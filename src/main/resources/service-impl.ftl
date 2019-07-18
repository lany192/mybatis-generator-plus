package ${packageName};

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import ${modelType};
import ${modelPackage}.example.${modelName}Query;
import ${mapperPackage}.${modelName}Mapper;
import ${servicePackage}.${modelName}Service;
import ${basePackage}.utils.ListUtils;
import ${basePackage}.utils.Result;
import ${basePackage}.utils.ResultBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

/**
* @author Lany
*/
@Slf4j
@Service
public class ${modelName}ServiceImpl implements ${modelName}Service {
    private final ${modelName}Mapper mapper;

    @Autowired
    public ${modelName}ServiceImpl(${modelName}Mapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 新增记录
     */
    @Override
    public Result save(${modelName} record) {
        if (record != null) {
            int id = mapper.insertSelective(record);
            log.info("添加成功,记录id：" + id);
            return ResultBuilder.message("添加成功");
        }
        return ResultBuilder.fail("添加失败");
    }

    /**
     * 根据id删除记录
     */
    @Override
    public Result deleteById(Long id) {
        if (1 == mapper.deleteByPrimaryKey(id)) {
            return ResultBuilder.message("删除成功");
        }
        return ResultBuilder.fail("删除失败");
    }

    /**
     * 根据id批量删除记录
     */
    @Override
    public Result deleteByIds(List<Long> ids) {
        if (!ListUtils.isEmpty(ids)) {
            ${modelName}Query query = new ${modelName}Query();
            query.or().andIdIn(ids);
            int count = mapper.deleteByExample(query);
            if (count == ids.size()) {
                return ResultBuilder.message("删除成功");
            }
        }
        return ResultBuilder.fail("删除失败");
    }

    /**
    * 修改记录
    */
    @Override
    public Result update(${modelName} record) {
        if (record != null && 1 == mapper.updateByPrimaryKeySelective(record)) {
            return ResultBuilder.message("修改成功");
        }
        return ResultBuilder.fail("修改失败");
    }

    /**
    * 根据id查询记录
    */
    @Override
    public Result findById(Long id) {
        ${modelName} record = mapper.selectByPrimaryKey(id);
        if (record != null) {
            return ResultBuilder.success(record);
        } else {
            return ResultBuilder.fail("找不到id为" + id + "的记录");
        }
    }

    /**
    * 根据id批量查询记录
    */
    @Override
    public Result findByIds(List<Long> ids) {
        if (!ListUtils.isEmpty(ids)) {
            ${modelName}Query query = new ${modelName}Query();
            query.or().andIdIn(ids);
            List<${modelName}> records = mapper.selectByExample(query);
            if (!ListUtils.isEmpty(records)) {
                return ResultBuilder.success(records);
            }
        }
        return ResultBuilder.fail("查询失败");
    }

    /**
    * 分页查询记录
    */
    @Override
    public Result find(int page, String keyword) {
        ${modelName}Query query = new ${modelName}Query();
        PageHelper.startPage(page, 30);
        List<${modelName}> records = mapper.selectByExample(query);
        return ResultBuilder.success(new PageInfo<>(records));
    }
}
