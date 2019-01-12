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
    *
    * @param record 记录
    * @return 操作结果
    */
    Result save(${modelNameUpper} record);

    /**
    * 根据id删除记录
    *
    * @param id 记录id
    * @return 操作结果
    */
    Result deleteById(Long id);

    /**
    * 根据id批量删除记录
    *
    * @param ids 记录id集
    * @return 操作结果
    */
    Result deleteByIds(List<Long> ids);

    /**
    * 修改记录
    *
    * @param record 记录
    * @return 操作结果
    */
    Result update(${modelNameUpper} record);

    /**
    * 根据id查询记录
    *
    * @param id 记录id
    * @return 操作结果
    */
    Result findById(Long id);

    /**
    * 根据id批量查询记录
    *
    * @param ids 记录id集
    * @return 操作结果
    */
    Result findByIds(List<Long> ids);

    /**
    * 分页查询记录
    *
    * @param page 页码
    * @param keyword 搜索关键字
    * @return 操作结果
    */
    Result find(int page, String keyword);
}
