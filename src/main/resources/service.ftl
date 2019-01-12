package ${packageName};

import ${modelType};
import ${basePackage}.utils.Result;

import java.util.List;

/**
* @author Lany
*/
public interface ${modelNameUpper}Service {
    /**
     * 新增记录
     */
    Result save(${modelNameUpper} record);

    /**
     * 根据id删除记录
     */
    Result deleteById(Long id);

    /**
     * 根据id批量删除记录
     */
    Result deleteByIds(List<Long> ids);

    /**
    * 修改记录
    */
    Result update(${modelNameUpper} record);

    /**
    * 根据id查询记录
    */
    Result findById(Long id);

    /**
    * 根据id批量查询记录
    */
    Result findByIds(List<Long> ids);

    /**
    * 分页查询记录
    */
    Result find(int page, String keyword);
}
